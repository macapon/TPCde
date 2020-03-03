/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.modulegenoma;

import com.labplanet.servicios.modulegenoma.TblsGenomaData;
import static functionaljavaa.modulegenoma.GenomaUtilities.*;
import databases.DataDataIntegrity;
import databases.Rdbms;
import databases.Token;
import java.util.Arrays;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class GenomaDataStudyFamily {
    
public Object[] createStudyFamily( String schemaPrefix, Token token, String studyName, String familyName, String[] individuals, String[] fieldsName, Object[] fieldsValue, Boolean devMode){
    
    Object[] projStudyToChanges=GenomaDataStudy.isStudyOpenToChanges(schemaPrefix, token, studyName);    
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(projStudyToChanges[0].toString())) return projStudyToChanges;
   
    String classVersionProj = "0.1";
    String[] mandatoryFields = null;
    Object[] mandatoryFieldsValue = fieldsValue;
    String[] javaDocFields = new String[0];
    Object[] javaDocValues = new Object[0];
    String javaDocLineName = "";
    DataDataIntegrity labIntChecker = new DataDataIntegrity();
    if (fieldsName==null) fieldsName=new String[0];
    if (fieldsValue==null) fieldsValue=new Object[0];

    if (devMode){
        StackTraceElement[] elementsDev = Thread.currentThread().getStackTrace();
        javaDocLineName = "BEGIN";
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_LINE_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, javaDocLineName);
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_CLASS_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, classVersionProj);
        LPPlatform.addJavaClassDoc(javaDocFields, javaDocValues, elementsDev);
    }    
        String actionName = "Insert";
        
        String schemaDataName = LPPlatform.SCHEMA_DATA;
        String schemaConfigName = LPPlatform.SCHEMA_CONFIG;
        
        schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, schemaDataName);    
        schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, schemaConfigName); 
        
        mandatoryFields = labIntChecker.getTableMandatoryFields(schemaDataName, TblsGenomaData.StudyFamily.TBL.getName(), actionName);
        
        
    if (devMode){
        StackTraceElement[] elementsDev = Thread.currentThread().getStackTrace();
        javaDocLineName = "CHECK sampleFieldName and sampleFieldValue match in length";
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_LINE_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, javaDocLineName);
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_CLASS_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, classVersionProj);
        LPPlatform.addJavaClassDoc(javaDocFields, javaDocValues, elementsDev);
    }    
    if (!devMode){
        String[] diagnosesProj = LPArray.checkTwoArraysSameLength(fieldsName, fieldsValue);
        if (fieldsName.length!=fieldsValue.length){
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            diagnosesProj[0]= elements[1].getClassName() + "." + elements[1].getMethodName();
            diagnosesProj[1]= classVersionProj;
            diagnosesProj[2]= "Code Line " + (elements[1].getLineNumber());   
            diagnosesProj[3]=LPPlatform.LAB_FALSE;
            diagnosesProj[4]="ERROR:Field names and values arrays with different length";
            diagnosesProj[5]="The values in FieldName are:"+ Arrays.toString(fieldsName)+". and in FieldValue are:"+Arrays.toString(fieldsValue);
            return diagnosesProj;
        }
    }    
    if (devMode){
        StackTraceElement[] elementsDev = Thread.currentThread().getStackTrace();
        javaDocLineName = "CHECK sampleFieldName and sampleFieldValue match in length";
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_LINE_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, javaDocLineName);
        javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_CLASS_FLDNAME);
        javaDocValues = LPArray.addValueToArray1D(javaDocValues, classVersionProj);
        LPPlatform.addJavaClassDoc(javaDocFields, javaDocValues, elementsDev);
    }    
    Object[] diagnosesProj = new Object[0];
    if (!devMode){        
        if (LPArray.duplicates(fieldsName)){
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Detected any field duplicated in FieldName, the values are: <*1*>", new String[]{Arrays.toString(fieldsName)});
        }

        StringBuilder mandatoryFieldsMissingBuilder = new StringBuilder();
        for (Integer inumLines=0;inumLines<mandatoryFields.length;inumLines++){
            String currField = mandatoryFields[inumLines];
            boolean contains = Arrays.asList(fieldsName).contains(currField.toLowerCase());
            if (!contains){
                Object[] sampleDefaultFieldValues = labIntChecker.getTableFieldsDefaulValues(schemaDataName, TblsGenomaData.StudyFamily.TBL.getName(), actionName); 
                if (mandatoryFieldsMissingBuilder.length()>0){mandatoryFieldsMissingBuilder.append(",");}
                
                mandatoryFieldsMissingBuilder.append(currField);
                
            }else{
                Integer valuePosic = Arrays.asList(fieldsName).indexOf(currField);
                mandatoryFieldsValue[inumLines] = fieldsValue[valuePosic]; 
                if ("config_code".equals(currField)){String configCode = fieldsValue[Arrays.asList(fieldsName).indexOf(currField)].toString();}
            }        
        }            
        if (mandatoryFieldsMissingBuilder.length()>0){
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "newProjectMissingMandatoryFields", new String[]{studyName, mandatoryFieldsMissingBuilder.toString(), schemaPrefix});
        }        
/*        Object[] diagnosis = Rdbms.existsRecord(schemaConfigName, tableName, new String[]{LPPlatform.SCHEMA_CONFIG,"config_version"}, new Object[]{projectTemplate, projectTemplateVersion});
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosis[0].toString())){	
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            diagnosesProj[0]= elements[1].getClassName() + "." + elements[1].getMethodName();
            diagnosesProj[1]= classVersionProj;
            diagnosesProj[2]= "Code Line " + (elements[1].getLineNumber());
            diagnosesProj[3]=LPPlatform.LAB_FALSE;
            diagnosesProj[4]="ERROR:Sample Config Code NOT FOUND";
            diagnosesProj[5]="The sample config code "+projectTemplate+" in its version "+projectTemplateVersion+" was not found in the schema "+schemaConfigName+". Detail:"+diagnosis[5];
            return diagnosesProj;
        }
*/
/*        String[] specialFields = labIntChecker.getStructureSpecialFields(schemaDataName, "projectStructure", actionName);
        String[] specialFieldsFunction = labIntChecker.getStructureSpecialFieldsFunction(schemaDataName, "projectStructure", actionName);
        
        String specialFieldsCheck = "";
        Integer specialFieldIndex = -1;
        for (Integer inumLines=0;inumLines<fieldsName.length;inumLines++){
            String currField = tableName+"." + fieldsName[inumLines];
            String currFieldValue = fieldsValue[inumLines].toString();
            boolean contains = Arrays.asList(specialFields).contains(currField);
            if (contains){                    
                    specialFieldIndex = Arrays.asList(specialFields).indexOf(currField);
                    String aMethod = specialFieldsFunction[specialFieldIndex];
                    Method method = null;
                    try {
                        Class<?>[] paramTypes = {Rdbms.class, String[].class, String.class, String.class, Integer.class};
                        method = getClass().getDeclaredMethod(aMethod, paramTypes);
                    } catch (NoSuchMethodException | SecurityException ex) {
                        String errorCode = "LabPLANETPlatform_SpecialFunctionReturnedEXCEPTION";
                        Object[] errorDetailVariables = new Object[0];
                        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, ex.getMessage());
                        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                        
                    }
                    Object specialFunctionReturn = method.invoke(this, null, schemaPrefix, projectTemplate, projectTemplateVersion);      
                    if (specialFunctionReturn.toString().contains("ERROR")){
                        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                        diagnosesProj[0]= elements[1].getClassName() + "." + elements[1].getMethodName();
                        diagnosesProj[1]= classVersionProj;
                        diagnosesProj[2]= "Code Line " + (elements[1].getLineNumber());
                        diagnosesProj[3]=LPPlatform.LAB_FALSE;
                        diagnosesProj[4]=specialFunctionReturn.toString();
                        diagnosesProj[5]="The field " + currField + " is considered special and its checker (" + aMethod + ") returned the Error above";
                        return diagnosesProj;                            
                    }                
                    
            }
        }
*/
        if (LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_NAME.getName())==-1){
           fieldsName=LPArray.addValueToArray1D(fieldsName, TblsGenomaData.StudyFamily.FLD_NAME.getName());
           fieldsValue=LPArray.addValueToArray1D(fieldsValue, familyName);
        }else
           fieldsValue[LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_NAME.getName())] = familyName;
        if (LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_STUDY.getName())==-1){
           fieldsName=LPArray.addValueToArray1D(fieldsName, TblsGenomaData.StudyFamily.FLD_STUDY.getName());
           fieldsValue=LPArray.addValueToArray1D(fieldsValue, studyName);
        }else
           fieldsValue[LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_STUDY.getName())] = studyName;
        if (LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_ON.getName())==-1){
           fieldsName=LPArray.addValueToArray1D(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_ON.getName());
           fieldsValue=LPArray.addValueToArray1D(fieldsValue, LPDate.getCurrentTimeStamp());
        }else
           fieldsValue[LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_ON.getName())] = LPDate.getCurrentTimeStamp();
        if (LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_BY.getName())==-1){
           fieldsName=LPArray.addValueToArray1D(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_BY.getName());
           fieldsValue=LPArray.addValueToArray1D(fieldsValue, token.getPersonName());
        }else
           fieldsValue[LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_CREATED_BY.getName())] = token.getPersonName();
        if (LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_ACTIVE.getName())==-1){
           fieldsName=LPArray.addValueToArray1D(fieldsName, TblsGenomaData.StudyFamily.FLD_ACTIVE.getName());
           fieldsValue=LPArray.addValueToArray1D(fieldsValue, GenomaBusinessRules.activateOnCreation(schemaPrefix, LPPlatform.SCHEMA_DATA, TblsGenomaData.StudyFamily.TBL.getName()));
        }else
           fieldsValue[LPArray.valuePosicInArray(fieldsName, TblsGenomaData.StudyFamily.FLD_ACTIVE.getName())] = GenomaBusinessRules.activateOnCreation(schemaPrefix, LPPlatform.SCHEMA_DATA, TblsGenomaData.StudyFamily.TBL.getName());        
/*        fieldsName = LPArray.addValueToArray1D(fieldsName, LPPlatform.SCHEMA_CONFIG);    
        fieldsValue = LPArray.addValueToArray1D(fieldsValue, projectTemplate);
        fieldsName = LPArray.addValueToArray1D(fieldsName, "config_version");    
        fieldsValue = LPArray.addValueToArray1D(fieldsValue, projectTemplateVersion); 
*/
        
        Object[] fieldsOnLogSample = LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":");
        diagnosesProj = Rdbms.insertRecordInTable(schemaDataName, TblsGenomaData.StudyFamily.TBL.getName(), fieldsName, fieldsValue);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosesProj[0].toString()))
            for (String currIndiv: individuals)
                studyFamilyAddIndividual(schemaPrefix, token, studyName, familyName, currIndiv);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosesProj[0].toString()))
            GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.NEW_STUDY_FAMILY.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
                studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":"), null);
        return diagnosesProj;  
    }    
    if (devMode){
        StackTraceElement[] elementsDev = Thread.currentThread().getStackTrace();
        javaDocLineName = "END";
        Integer specialFieldIndex = Arrays.asList(javaDocFields).indexOf(LPPlatform.JAVADOC_LINE_FLDNAME);
        if (specialFieldIndex==-1){
            javaDocFields = LPArray.addValueToArray1D(javaDocFields, LPPlatform.JAVADOC_LINE_FLDNAME);         javaDocValues = LPArray.addValueToArray1D(javaDocValues, javaDocLineName);         
        }else{    
            javaDocValues[specialFieldIndex] = javaDocLineName;             
        }
        LPPlatform.addJavaClassDoc(javaDocFields, javaDocValues, elementsDev);
    }
    return diagnosesProj; 
}    

public Object[] studyFamilyActivate( String schemaPrefix, Token token, String studyName, String familyName){

    String[] fieldsName=new String[]{TblsGenomaData.StudyFamily.FLD_ACTIVE.getName()};
    Object[] fieldsValue=new Object[]{true};
    
    Object[] diagnosesProj = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyFamily.TBL.getName(), 
            fieldsName, fieldsValue, new String[]{TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{familyName});
    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosesProj[0].toString()))
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.ACTIVATE_STUDY_FAMILY.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":"), null);
    return diagnosesProj;      
}    

public Object[] studyFamilyDeActivate(String schemaPrefix, Token token, String studyName, String familyName){
    Object[] projStudyToChanges=GenomaDataStudy.isStudyOpenToChanges(schemaPrefix, token, studyName);    
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(projStudyToChanges[0].toString())) return projStudyToChanges;
    
    String[] fieldsName=new String[]{TblsGenomaData.StudyFamily.FLD_ACTIVE.getName()};
    Object[] fieldsValue=new Object[]{false};
    
    Object[] diagnosesProj = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyFamily.TBL.getName(), 
            fieldsName, fieldsValue, 
            new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName(), TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{studyName, familyName});
    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosesProj[0].toString()))
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.DEACTIVATE_STUDY_FAMILY.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":"), null);
    return diagnosesProj;      
}   

public Object[] studyFamilyIndividualUpdate( String schemaPrefix, Token token, String studyName, String familyName, String[] fieldsName, Object[] fieldsValue){
    Object[] projStudyToChanges=GenomaDataStudy.isStudyOpenToChanges(schemaPrefix, token, studyName);    
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(projStudyToChanges[0].toString())) return projStudyToChanges;

    Object[] specialFieldsPresent=GenomaBusinessRules.specialFieldsInUpdateArray(schemaPrefix, LPPlatform.SCHEMA_DATA, TblsGenomaData.StudyFamily.TBL.getName(), fieldsName);
    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(specialFieldsPresent[0].toString()))
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, specialFieldsPresent[specialFieldsPresent.length-1].toString(), null);
    Object[] diagnosesProj = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyFamily.TBL.getName(), 
            fieldsName, fieldsValue, 
            new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName(), TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{studyName, familyName});
    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosesProj[0].toString()))
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.UPDATE_STUDY_FAMILY.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":"), null);
    return diagnosesProj;      
} 

public static Object[] studyFamilyAddIndividual(String schemaPrefix, Token token, String studyName, String familyName, String individualId) {
    Object[] isStudyFamilyOpenToChanges=isStudyFamilyOpenToChanges(schemaPrefix, token, studyName, familyName);
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isStudyFamilyOpenToChanges[0].toString())) return isStudyFamilyOpenToChanges;
    
    Object[] updateFamilyIndividuals=addObjectToUnstructuredField(schemaPrefix, LPPlatform.SCHEMA_DATA, TblsGenomaData.StudyFamily.TBL.getName(), 
            new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName(), TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{studyName, familyName}, 
            TblsGenomaData.StudyFamily.FLD_UNSTRUCT_CONTENT.getName(), individualId.toString(), individualId.toString());  
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        return updateFamilyIndividuals;
    }
    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.STUDY_FAMILY_ADDED_INDIVIDUAL.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(new String[]{TblsGenomaData.StudyFamily.FLD_UNSTRUCT_CONTENT.getName()}, new Object[]{updateFamilyIndividuals[updateFamilyIndividuals.length-1]}, ":"), null);
    }
    return updateFamilyIndividuals;
}

public static Object[] studyFamilyRemoveIndividual(String schemaPrefix, Token token, String studyName, String familyName, String individualId) {
    
    Object[] isStudyFamilyOpenToChanges=isStudyFamilyOpenToChanges(schemaPrefix, token, studyName, familyName);
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isStudyFamilyOpenToChanges[0].toString())) return isStudyFamilyOpenToChanges;

    Object[] updateFamilyIndividuals=removeObjectToUnstructuredField(schemaPrefix, LPPlatform.SCHEMA_DATA, TblsGenomaData.StudyFamily.TBL.getName(), 
            new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName(), TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{studyName, familyName}, 
            TblsGenomaData.StudyFamily.FLD_UNSTRUCT_CONTENT.getName(), TblsGenomaData.StudyIndividual.TBL.getName(), individualId.toString(), individualId.toString());  
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        return updateFamilyIndividuals;
    }
    
    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.STUDY_FAMILY_REMOVED_INDIVIDUAL.toString(), TblsGenomaData.StudyFamily.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(new String[]{TblsGenomaData.StudyFamily.FLD_UNSTRUCT_CONTENT.getName()}, new Object[]{updateFamilyIndividuals[updateFamilyIndividuals.length-1]}, ":"), null);
    }
    return updateFamilyIndividuals;
}

public static Object[] isStudyFamilyOpenToChanges(String schemaPrefix, Token token, String studyName, String familyName){
        Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyFamily.TBL.getName(),
            new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName(), TblsGenomaData.StudyFamily.FLD_NAME.getName()}, new Object[]{studyName, familyName}, new String[]{TblsGenomaData.StudyFamily.FLD_ACTIVE.getName()});
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString()))
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The study family <*1*> does not exist in procedure <*2*>", new Object[]{studyName, schemaPrefix});
    if (!Boolean.valueOf(LPNulls.replaceNull(sampleInfo[0][0]).toString()))
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The study family <*1*> is already inactive in procedure <*2*>", new Object[]{studyName, schemaPrefix});
    return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "<*1*> is open to changes in procedure <*2*>", new Object[]{studyName, schemaPrefix});
}
    
}
