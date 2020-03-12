/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.moduleenvironmentalmonitoring;

import com.labplanet.servicios.modulegenoma.TblsGenomaData;
import databases.Rdbms;
import databases.Token;
import static functionaljavaa.modulegenoma.GenomaConfigVariablesQueries.getVariableSetVariablesProperties;
import functionaljavaa.modulegenoma.GenomaDataAudit;
import static functionaljavaa.modulegenoma.GenomaDataStudy.isStudyOpenToChanges;
import java.util.Arrays;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPMath;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class DataStudyObjectsVariableValues {
    
    public enum VariableTypes{Categorical, Integer};
    
    public static Object[] addVariableSetToObject(String schemaPrefix, Token token, String studyName, String variableSetName, String ownerTable, String ownerId){
        Object[] diagn=new Object[0];
        Object[] isStudyOpenToChanges=isStudyOpenToChanges(schemaPrefix, token, studyName);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isStudyOpenToChanges[0].toString())) return isStudyOpenToChanges;
        
        Object[][] variableSetContent=getVariableSetVariablesProperties(schemaPrefix, variableSetName);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(variableSetContent[0].toString())) return variableSetContent;
        String[] fieldHeaders=new String[0];
        for (Object[] currVar: variableSetContent){
            if (fieldHeaders.length==0){
                for (int iFields=0;iFields<currVar.length;iFields++)
                    fieldHeaders=LPArray.addValueToArray1D(fieldHeaders, currVar[iFields].toString());                
            }else{
                Object[] fieldVarProperties=new Object[0];
                for (int iFields=0;iFields<currVar.length;iFields++)
                    fieldVarProperties=LPArray.addValueToArray1D(fieldVarProperties, currVar[iFields]);                
                String[] fieldsName=new String[]{TblsGenomaData.StudyVariableValues.FLD_STUDY.getName(), TblsGenomaData.StudyVariableValues.FLD_OWNER_TABLE.getName(), TblsGenomaData.StudyVariableValues.FLD_OWNER_ID.getName(),
                    TblsGenomaData.StudyVariableValues.FLD_VARIABLE_SET.getName()};
                fieldsName=LPArray.addValueToArray1D(fieldsName, fieldHeaders);
                Object[] fieldsValue=new Object[]{studyName, ownerTable, ownerId, variableSetName};
                fieldsValue=LPArray.addValueToArray1D(fieldsValue, fieldVarProperties);
                diagn=Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyVariableValues.TBL.getName(), 
                    fieldsName, fieldsValue);            
                if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(diagn[0].toString())) 
                    GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.ADD_VARIABLE_SET_TO_STUDY_OBJECT.toString(), TblsGenomaData.StudyVariableValues.TBL.getName(), currVar.toString(), 
                        studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ":"), null);                
            }
        }        
        return diagn; //LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "not implemented yet!", null);
    }
    public static Object[] objectVariableSetValue(String schemaPrefix, Token token, String studyName, String ownerTable, String ownerId, String variableSetName, String variableName, String newValue){
        Object[] diagn=new Object[0];
        Object[] isStudyOpenToChanges=isStudyOpenToChanges(schemaPrefix, token, studyName);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isStudyOpenToChanges[0].toString())) return isStudyOpenToChanges;
        
        String[] fieldsToRetrieve=new String[]{TblsGenomaData.StudyVariableValues.FLD_ID.getName(), TblsGenomaData.StudyVariableValues.FLD_NAME.getName(), TblsGenomaData.StudyVariableValues.FLD_TYPE.getName(), TblsGenomaData.StudyVariableValues.FLD_REQUIRED.getName(), 
            TblsGenomaData.StudyVariableValues.FLD_ALLOWED_VALUES.getName()};
        
        String[] fieldsName=new String[]{TblsGenomaData.StudyVariableValues.FLD_STUDY.getName(), TblsGenomaData.StudyVariableValues.FLD_OWNER_TABLE.getName(), TblsGenomaData.StudyVariableValues.FLD_OWNER_ID.getName(),
            TblsGenomaData.StudyVariableValues.FLD_VARIABLE_SET.getName(), TblsGenomaData.StudyVariableValues.FLD_NAME.getName()};
        Object[] fieldsValue=new Object[]{studyName, ownerTable, ownerId, variableSetName, variableName};
        Object[][] objectVariablePropInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyVariableValues.TBL.getName(),
                fieldsName, fieldsValue, fieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(objectVariablePropInfo[0].toString())) return objectVariablePropInfo;
        
        if (objectVariablePropInfo.length!=1) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Found more than one record, <*1*> for the query <*2*> on <*3*>", 
            new Object[]{objectVariablePropInfo.length, Arrays.toString(fieldsName), schemaPrefix});
        
        String fieldType = objectVariablePropInfo[0][2].toString();
        if (VariableTypes.Categorical.toString().equalsIgnoreCase(fieldType)){
            String[] allowedValuesArr = LPNulls.replaceNull(objectVariablePropInfo[0][4]).toString().split("\\|");
            if (!LPArray.valueInArray(allowedValuesArr, newValue)) 
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The value <*1*> is not one of the accepted values <*2*> for variable <*3*> in procedure <*4*>", 
                    new Object[]{newValue, Arrays.toString(allowedValuesArr), variableName, schemaPrefix});
        }else if (VariableTypes.Integer.toString().equalsIgnoreCase(fieldType)){
            if (!LPMath.isNumeric(newValue))return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The value <*1*> is not numeric for variable <*2*> in procedure <*3*>", 
                    new Object[]{newValue, variableName, schemaPrefix});
        }else 
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "not recognized variable type "+fieldType, null);
        String[] updFieldsName=new String[]{TblsGenomaData.StudyVariableValues.FLD_VALUE.getName()};
        Object[] updFieldsValue=new Object[]{newValue};
        diagn=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsGenomaData.StudyVariableValues.TBL.getName(), 
            updFieldsName, updFieldsValue, new String[]{TblsGenomaData.StudyVariableValues.FLD_ID.getName()}, new Object[]{Integer.valueOf(objectVariablePropInfo[0][0].toString())});            
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(diagn[0].toString())) 
            GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.STUDY_OBJECT_SET_VARIABLE_VALUE.toString(), TblsGenomaData.StudyVariableValues.TBL.getName(), newValue, 
                studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(updFieldsName, updFieldsValue, ":"), null);                
        
        return diagn;
        
    }
}
