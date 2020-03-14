/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.samplestructure;

import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPParadigm;
import databases.Rdbms;
import functionaljavaa.audit.SampleAudit;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPMath;
import databases.DataDataIntegrity;
import databases.TblsCnfg;
import databases.TblsData;
import databases.TblsDataAudit;
import databases.Token;
import functionaljavaa.changeofcustody.ChangeOfCustody;
import functionaljavaa.parameter.Parameter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import static lbplanet.utilities.LPPlatform.trapMessage;

/**
 *
 * @author Administrator
 */
public class DataSample {    
    /**
     *
     */
    public static final String SAMPLE_ENTIRE_STRUCTURE_ALL_FIELDS="ALL";
    /**
     *
     */
    public static final String CONFIG_SAMPLE_STATUSREVIEWED="sample_statusReviewed";

    /**
     *
     */
    public static final String CONFIG_SAMPLE_STATUSCANCELED="sample_statusCanceled";            
    
    /**
     *
     */
    public static final String DIAGNOSES_SUCCESS = "SUCCESS";
    
    /**
     *
     */
    public static final String SAMPLE_STATUS_LOGGED = "LOGGED";

    /**
     *
     */
    public static final String SAMPLE_STATUS_RECEIVED = "RECEIVED";

    /**
     *
     */
    public static final String SAMPLE_STATUS_NOT_STARTED = "NOT_STARTED";

    /**
     *
     */
    public static final String SAMPLE_STATUS_STARTED = "STARTED";

    /**
     *
     */
    public static final String SAMPLE_STATUS_INCOMPLETE = "INCOMPLETE";
    
    private static final String ERROR_TRAPPING_ERROR_INSERTING_SAMPLE_RECORD = "DataSample_errorInsertingSampleRecord";
    static final String ERROR_TRAPPING_DATA_SAMPLE_NOT_FOUND = "DataSample_SampleNotFound";

    String classVersion = "0.1";
    String errorCode ="";
    Object[] errorDetailVariables= new Object[0];
    
    /**
     *
     */
    public static String[] mandatoryFields = null;

    /**
     *
     */
    public static Object[] mandatoryFieldsValue = null;

    DataDataIntegrity labIntChecker = new DataDataIntegrity();
    DataSampleAnalysisStrategy smpAna;
    

    /**
     * Este es el constructor para DataSample
   * @param smpAna
     */
    public DataSample(DataSampleAnalysisStrategy smpAna){
        this.classVersion="0.1";
        this.smpAna=smpAna;
    }   

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleTemplate
     * @param sampleTemplateVersion
     * @param sampleFieldName
     * @param sampleFieldValue
     * @return
     */
    public Object[] logSampleDev( String schemaPrefix, Token token, String sampleTemplate, Integer sampleTemplateVersion, String[] sampleFieldName, Object[] sampleFieldValue) {
        return logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, sampleFieldName, sampleFieldValue, true, 1);
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleTemplate
     * @param sampleTemplateVersion
     * @param sampleFieldName
     * @param sampleFieldValue
     * @return
     */
    public Object[] logSample(String schemaPrefix, Token token, String sampleTemplate, Integer sampleTemplateVersion, String[] sampleFieldName, Object[] sampleFieldValue) {
        return logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, sampleFieldName, sampleFieldValue, false, 1);
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleTemplate
     * @param sampleTemplateVersion
     * @param sampleFieldName
     * @param sampleFieldValue
     * @param numSamplesToLog
     * @return
     */
    public Object[] logSample(String schemaPrefix, Token token, String sampleTemplate, Integer sampleTemplateVersion, String[] sampleFieldName, Object[] sampleFieldValue, Integer numSamplesToLog) {
        return logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, sampleFieldName, sampleFieldValue, false, numSamplesToLog);
    }


Object[] logSample( String schemaPrefix, Token token, String sampleTemplate, Integer sampleTemplateVersion, String[] sampleFieldName, Object[] sampleFieldValue, Boolean devMode, Integer numSamplesToLog) {
    Object[] diagnoses = new Object[7];
        String actionName = "Insert";
        
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);    
        String schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG); 
        
        String sampleLevel = TblsData.Sample.TBL.getName();

        mandatoryFields = labIntChecker.getTableMandatoryFields(schemaDataName, sampleLevel, actionName);
        
        String sampleStatusFirst = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), sampleLevel+"_statusFirst");     

        sampleFieldName = LPArray.addValueToArray1D(sampleFieldName, TblsData.Sample.FLD_STATUS.getName());
        sampleFieldValue = LPArray.addValueToArray1D(sampleFieldValue, sampleStatusFirst);
        Object[] fieldNameValueArrayChecker = LPParadigm.fieldNameValueArrayChecker(sampleFieldName, sampleFieldValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(fieldNameValueArrayChecker[0].toString())){return fieldNameValueArrayChecker;}        
        // spec is not mandatory but when any of the fields involved is added to the parameters 
        //  then it turns mandatory all the fields required for linking this entity.
        Integer fieldIndexSpecCode = Arrays.asList(sampleFieldName).indexOf(TblsData.Sample.FLD_SPEC_CODE.getName());
        Integer fieldIndexSpecCodeVersion = Arrays.asList(sampleFieldName).indexOf(TblsData.Sample.FLD_SPEC_CODE_VERSION.getName());
        Integer fieldIndexSpecVariationName = Arrays.asList(sampleFieldName).indexOf(TblsData.Sample.FLD_SPEC_VARIATION_NAME.getName());
        if ((fieldIndexSpecCode!=-1) || (fieldIndexSpecCodeVersion!=-1) || (fieldIndexSpecVariationName!=-1)){
            mandatoryFields = LPArray.addValueToArray1D(mandatoryFields, TblsData.Sample.FLD_SPEC_CODE.getName());
            mandatoryFields = LPArray.addValueToArray1D(mandatoryFields, TblsData.Sample.FLD_SPEC_CODE_VERSION.getName());
            mandatoryFields = LPArray.addValueToArray1D(mandatoryFields, TblsData.Sample.FLD_SPEC_VARIATION_NAME.getName());
        }

        mandatoryFieldsValue = new Object[mandatoryFields.length];
        StringBuilder mandatoryFieldsMissingBuilder = new StringBuilder();
        for (Integer inumLines=0;inumLines<mandatoryFields.length;inumLines++){
            String currField = mandatoryFields[inumLines];
            boolean contains = Arrays.asList(sampleFieldName).contains(currField.toLowerCase());
            if (!contains){
                if (mandatoryFieldsMissingBuilder.length()>0){mandatoryFieldsMissingBuilder.append(",");}
                
                mandatoryFieldsMissingBuilder.append(currField);
            }else{
                Integer valuePosic = Arrays.asList(sampleFieldName).indexOf(currField);
                mandatoryFieldsValue[inumLines] = sampleFieldValue[valuePosic]; 
            }        
        }            
        if (mandatoryFieldsMissingBuilder.length()>0){
           errorCode = "DataSample_MissingMandatoryFields";
           errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, mandatoryFieldsMissingBuilder.toString());
           return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);    
        }               
        
        Object[] diagnosis = Rdbms.existsRecord(schemaConfigName, TblsCnfg.Sample.TBL.getName(), 
                new String[]{TblsCnfg.Sample.FLD_CODE.getName(), TblsCnfg.Sample.FLD_CODE_VERSION.getName()}, new Object[]{sampleTemplate, sampleTemplateVersion});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnosis[0].toString()))
           return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_MissingConfigCode", new Object[]{sampleTemplate, sampleTemplateVersion, schemaConfigName, diagnosis[5]});    
        String[] specialFields = labIntChecker.getStructureSpecialFields(schemaDataName, sampleLevel+"_"+"sampleStructure", actionName);
        String[] specialFieldsFunction = labIntChecker.getStructureSpecialFieldsFunction(schemaDataName, sampleLevel+"_"+"sampleStructure", actionName);
        Integer specialFieldIndex = -1;
        
        for (Integer inumLines=0;inumLines<sampleFieldName.length;inumLines++){
            String currField = TblsData.Sample.TBL.getName()+"." + sampleFieldName[inumLines];
            boolean contains = Arrays.asList(specialFields).contains(currField);
            if (contains){                    
                    specialFieldIndex = Arrays.asList(specialFields).indexOf(currField);
                    String aMethod = specialFieldsFunction[specialFieldIndex];
                    Method method = null;
                    try {
                        Class<?>[] paramTypes = {Rdbms.class, String[].class, String.class, String.class, Integer.class};
                        method = getClass().getDeclaredMethod(aMethod, paramTypes);
                    } catch (NoSuchMethodException | SecurityException ex) {
                            errorCode = "LabPLANETPlatform_SpecialFunctionReturnedEXCEPTION";
                            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, ex.getMessage());
                            return trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
                    }
                    Object specialFunctionReturn=null;      
                    try {
                        if (method!=null){ specialFunctionReturn = method.invoke(this, null, schemaPrefix, sampleTemplate, sampleTemplateVersion);}
                    } catch (IllegalAccessException | NullPointerException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(DataSample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if ( (specialFunctionReturn==null) || (specialFunctionReturn!=null && specialFunctionReturn.toString().contains("ERROR")) )
                        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_SpecialFunctionReturnedERROR", new Object[]{currField, aMethod, LPNulls.replaceNull(specialFunctionReturn)});                            
            }
        }        
        sampleFieldName = LPArray.addValueToArray1D(sampleFieldName, TblsData.Sample.FLD_CONFIG_CODE.getName());    
        sampleFieldValue = LPArray.addValueToArray1D(sampleFieldValue, sampleTemplate);
        sampleFieldName = LPArray.addValueToArray1D(sampleFieldName, TblsData.Sample.FLD_CONFIG_CODE_VERSION.getName());    
        sampleFieldValue = LPArray.addValueToArray1D(sampleFieldValue, sampleTemplateVersion); 
        
        if (LPArray.valuePosicInArray(sampleFieldName, TblsData.Sample.FLD_CUSTODIAN.getName())==-1){
            ChangeOfCustody coc = new ChangeOfCustody();
            Object[] changeOfCustodyEnable = coc.isChangeOfCustodyEnable(schemaDataName, TblsData.Sample.TBL.getName());
            if (LPPlatform.LAB_TRUE.equalsIgnoreCase(changeOfCustodyEnable[0].toString())){
                sampleFieldName = LPArray.addValueToArray1D(sampleFieldName, TblsData.Sample.FLD_CUSTODIAN.getName());    
                sampleFieldValue = LPArray.addValueToArray1D(sampleFieldValue, token.getPersonName());             
            }
        }
        DataSampleStages smpStages = new DataSampleStages(schemaPrefix);
        Object[][] firstStage=smpStages.getFirstStage();
        if (firstStage.length>0){
          for (Object[] curFld: firstStage){
                sampleFieldName = LPArray.addValueToArray1D(sampleFieldName, curFld[0].toString());    
                sampleFieldValue = LPArray.addValueToArray1D(sampleFieldValue, curFld[1]);                         
          }
        }

        if (numSamplesToLog==null){numSamplesToLog=1;}
        
        for (int iNumSamplesToLog=0; iNumSamplesToLog<numSamplesToLog; iNumSamplesToLog++ ){        
            diagnoses = Rdbms.insertRecordInTable(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue);
            if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
                errorCode = ERROR_TRAPPING_ERROR_INSERTING_SAMPLE_RECORD;
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, diagnoses[diagnoses.length-2]);
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
            }                                

            Object[] fieldsOnLogSample = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, ":");
            diagnoses = LPArray.addValueToArray1D(diagnoses, diagnoses[diagnoses.length-1]);

            if (Rdbms.TBL_NO_KEY.equalsIgnoreCase(diagnoses[diagnoses.length-1].toString())){return diagnoses;}
            
            Integer sampleId = Integer.parseInt(diagnoses[diagnoses.length-1].toString());
            smpStages.dataSampleStagesTimingCapture(schemaPrefix, sampleId, firstStage[firstStage.length-1][1].toString(), DataSampleStages.SampleStageTimingCapturePhases.START.toString());
            
            SampleAudit smpAudit = new SampleAudit();            
            Object[] sampleAuditAdd = smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_LOGGED.toString(), TblsData.Sample.TBL.getName(), sampleId, 
                                        sampleId, null, null, fieldsOnLogSample, token, null);
            Integer transactionId = null;
            Integer preAuditId=Integer.valueOf(sampleAuditAdd[sampleAuditAdd.length-1].toString());
            //DataSampleAnalysis dataSmpAna = new DataSampleAnalysis();
            this.smpAna.autoSampleAnalysisAdd(schemaPrefix, token, sampleId, sampleFieldName, sampleFieldValue, SAMPLE_STATUS_LOGGED, preAuditId);
            
            autoSampleAliquoting(schemaPrefix, token, sampleId, sampleFieldName, sampleFieldValue, SAMPLE_STATUS_LOGGED, transactionId, preAuditId);            
        }
        return diagnoses;  
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @return
     */
    public Object[] sampleReception( String schemaPrefix, Token token, Integer sampleId) {
        String receptionStatus = SAMPLE_STATUS_RECEIVED;        
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 
    
        Object[][] currSampleStatus = Rdbms.getRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, 
                                                    new Object[]{sampleId}, 
                                                    new String[]{TblsData.Sample.FLD_STATUS.getName(), TblsData.Sample.FLD_RECEIVED_BY.getName(), TblsData.Sample.FLD_RECEIVED_ON.getName(), 
                                                        TblsData.Sample.FLD_STATUS.getName()});
        if (LPPlatform.LAB_FALSE==currSampleStatus[0][0]){
            errorCode = ERROR_TRAPPING_DATA_SAMPLE_NOT_FOUND;
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, sampleId);
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, schemaPrefix);
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
        }
        if ( (currSampleStatus[0][1]!=null) && (currSampleStatus[0][1].toString().length()>0) ) { 
            errorCode = "DataSample_SampleAlreadyReceived";
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, sampleId.toString());
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, currSampleStatus[0][2]);
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
        }
        String currentStatus = (String) currSampleStatus[0][0];

        String[] sampleFieldName = new String[]{TblsData.Sample.FLD_STATUS.getName(), TblsData.Sample.FLD_STATUS_PREVIOUS.getName(), TblsData.Sample.FLD_RECEIVED_BY.getName(), TblsData.Sample.FLD_RECEIVED_ON.getName()};    
        Object[] sampleFieldValue = new Object[]{receptionStatus, currentStatus, token.getPersonName(), LPDate.getCurrentTimeStamp()};

        Object[] diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue, 
                                                new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getPersonName());

            SampleAudit smpAudit = new SampleAudit();       
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_RECEIVED.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
        }    
        return diagnoses;
}
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @return
     */
    public Object[] setSamplingDate( String schemaPrefix, Token token, Integer sampleId){
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);         

        String[] sampleFieldName = new String[]{TblsData.Sample.FLD_SAMPLING_DATE.getName()};
        Object[] sampleFieldValue = new Object[]{LPDate.getCurrentTimeStamp()};

        Object[] diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue, new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            errorCode = "DataSample_SamplingDateChangedSuccessfully";
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, sampleId.toString());
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, schemaDataName);
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, Arrays.toString(LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, ", ")));        
            diagnoses = LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);

            String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getPersonName());

            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SET_SAMPLING_DATE.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
        }    
        return diagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param newDate
     * @return
     */
    public Object[] changeSamplingDate( String schemaPrefix, Token token, Integer sampleId, Date newDate){
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 

        String[] sampleFieldName = new String[]{TblsData.Sample.FLD_SAMPLING_DATE.getName()};
        Object[] sampleFieldValue = new Object[]{newDate};

        Object[] diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue, new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){            
            diagnoses = LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "DataSample_SamplingDateChangedSuccessfully", 
                    new Object[]{sampleId, schemaDataName, Arrays.toString(LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, ", "))});

            String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getPersonName());

            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_CHANGE_SAMPLING_DATE.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
        }    
        return diagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param comment
     * @return
     */
    public Object[] sampleReceptionCommentAdd( String schemaPrefix, Token token, Integer sampleId, String comment){
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 

        String[] sampleFieldName = new String[]{TblsData.Sample.FLD_SAMPLING_COMMENT.getName()};
        Object[] sampleFieldValue = new Object[]{comment};

        Object[] diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue, 
                new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){        
            diagnoses = LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "DataSample_SampleReceptionCommentAdd", 
                new Object[]{sampleId, schemaDataName, Arrays.toString(LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, ", "))});                

            String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getPersonName());
            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_RECEPTION_COMMENT_ADD.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
        } 
        return diagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @return
     */
    public Object[] sampleReceptionCommentRemove( String schemaPrefix, Token token, Integer sampleId) {
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 

        String[] sampleFieldName = new String[]{TblsData.Sample.FLD_SAMPLING_COMMENT.getName()};
        Object[] sampleFieldValue = new Object[]{""};

        Object[] diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), sampleFieldName, sampleFieldValue, 
                new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            diagnoses = LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "DataSample_SampleReceptionCommentRemoved", 
                new Object[]{sampleId, schemaDataName, Arrays.toString(LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, ", "))});                

            String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getPersonName());
            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_RECEPTION_COMMENT_REMOVE.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
        } 
        return diagnoses;
    }


    /**
     *
     * @param schemaPrefix
     * @param token
     * @param testId
     * @param analyst
     */
    public void _sampleAssignAnalyst( String schemaPrefix, Token token, Integer testId, String analyst){
        // Not implemented yet
    }

        

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param preAuditId
     * @param parentAuditAction
     * @return
     */
    public static Object[] sampleEvaluateStatus( String schemaPrefix, Token token, Integer sampleId, String parentAuditAction, Integer preAuditId){ 
        String statuses="NOT_STARTED|STARTED|INCOMPLETE";
        String auditActionName = SampleAudit.SampleAuditEvents.SAMPLE_EVALUATE_STATUS.toString();
        if (parentAuditAction!=null){auditActionName = parentAuditAction + ":"+auditActionName;}

        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 

        String sampleStatusFirst = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statusFirst");
        String sampleStatusInReceived = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statusReceived");

        Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()},
                new Object[]{sampleId}, new String[]{TblsData.Sample.FLD_STATUS.getName()});
        if ( (sampleStatusFirst.equalsIgnoreCase(sampleInfo[0][0].toString())) || (sampleStatusInReceived.equalsIgnoreCase(sampleInfo[0][0].toString()))){
            String[] fieldsForAudit = new String[0];
            fieldsForAudit = LPArray.addValueToArray1D(fieldsForAudit, TblsData.Sample.FLD_STATUS.getName()+": keep status "+sampleInfo[0][0].toString());
            SampleAudit smpAudit = new SampleAudit();        
            smpAudit.sampleAuditAdd(schemaPrefix, auditActionName, TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, preAuditId);              
            return new Object[]{LPPlatform.LAB_TRUE};
        }    
        String sampleStatusIncomplete = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statusIncomplete");
        String sampleStatusComplete = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statusComplete");

        String smpAnaNewStatus="";    
        Object[] diagnoses =  Rdbms.existsRecord(schemaDataName, TblsData.SampleAnalysis.TBL.getName(), 
                                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName(),TblsData.Sample.FLD_STATUS.getName()+" in|"}, 
                                            new Object[]{sampleId, statuses});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){smpAnaNewStatus=sampleStatusIncomplete;}
        else{smpAnaNewStatus=sampleStatusComplete;}

        diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), 
                new String[]{TblsData.Sample.FLD_STATUS.getName()}, new Object[]{smpAnaNewStatus},
                new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            String[] fieldsForAudit = new String[0];
            fieldsForAudit = LPArray.addValueToArray1D(fieldsForAudit, TblsData.Sample.FLD_STATUS.getName()+":"+smpAnaNewStatus);
            SampleAudit smpAudit = new SampleAudit();        
            smpAudit.sampleAuditAdd(schemaPrefix, auditActionName, TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, preAuditId);        
        }      
        return diagnoses;
    }
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @return 
     */
    public Object[] sampleReview( String schemaPrefix, Token token, Integer sampleId){
        Object[] diagnoses = new Object[7];
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA); 
            
        String sampleStatusCanceled = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), CONFIG_SAMPLE_STATUSCANCELED);
        String sampleStatusReviewed = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), CONFIG_SAMPLE_STATUSREVIEWED);
        Object[] sampleAuditRevision=SampleAudit.sampleAuditRevisionPass(schemaPrefix, sampleId);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleAuditRevision[0].toString())) return sampleAuditRevision;
        Object[][] objectInfo = Rdbms.getRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), 
                                        new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId},
                                        new String[]{TblsData.Sample.FLD_STATUS.getName(), TblsData.Sample.FLD_STATUS_PREVIOUS.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName()});
        String currStatus = (String) objectInfo[0][0];               
        if ( (!(sampleStatusCanceled.equalsIgnoreCase(currStatus))) && (!(sampleStatusReviewed.equalsIgnoreCase(currStatus))) && (sampleId!=null) ){
            diagnoses = Rdbms.updateRecordFieldsByFilter(schemaDataName, TblsData.Sample.TBL.getName(), 
                                                                new String[]{TblsData.Sample.FLD_STATUS.getName(), TblsData.Sample.FLD_STATUS_PREVIOUS.getName()}, new Object[]{sampleStatusReviewed, currStatus}, 
                                                                new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});                                                        
            if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
                String[] fieldsForAudit = new String[0];
                fieldsForAudit = LPArray.addValueToArray1D(fieldsForAudit, TblsData.Sample.FLD_STATUS.getName()+":"+sampleStatusCanceled);
                fieldsForAudit = LPArray.addValueToArray1D(fieldsForAudit, TblsData.Sample.FLD_STATUS_PREVIOUS.getName()+":"+currStatus);
                SampleAudit smpAudit = new SampleAudit();
                smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.SAMPLE_REVIEWED.toString(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);                            
            }                        
        }else{
            diagnoses = LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_SampleNotReviewable", 
                    new Object[]{LPNulls.replaceNull(sampleId), schemaDataName, currStatus});                       
        }
        return diagnoses;        
    }
    /**
     *
     * @param schemaPrefix
     * @param template
     * @param templateVersion
     * @return
     */
    
    private String specialFieldCheckSampleStatus( String schemaPrefix, String template, Integer templateVersion){                      
        String myDiagnoses = "";        
        String schemaConfigName = LPPlatform.SCHEMA_CONFIG;
        
        schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, schemaConfigName); 
        
        Integer specialFieldIndex = Arrays.asList(mandatoryFields).indexOf(TblsData.Sample.FLD_STATUS.getName());
        String status = mandatoryFieldsValue[specialFieldIndex].toString();     
        if (status.length()==0){myDiagnoses = "ERROR: The parameter status cannot be null"; return myDiagnoses;}
        
        Object[] diagnosis = Rdbms.existsRecord(schemaConfigName, TblsCnfg.SampleRules.TBL.getName(), new String[] {TblsCnfg.SampleRules.FLD_CODE.getName(), TblsCnfg.SampleRules.FLD_CODE_VERSION.getName()}, new Object[] {template, templateVersion});
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnosis[0].toString())){
            myDiagnoses = "ERROR: The sample_rule record for "+template+" does not exist in schema"+schemaConfigName+". ERROR: "+diagnosis[5];}
        else{    
            String[] fieldNames = new String[]{TblsCnfg.SampleRules.FLD_CODE.getName()};
            Object[] fieldValues = new Object[]{template};
            String[] fieldFilter = new String[] {TblsCnfg.SampleRules.FLD_CODE.getName(), TblsCnfg.SampleRules.FLD_CODE_VERSION.getName(), 
                TblsCnfg.SampleRules.FLD_STATUSES.getName(), TblsCnfg.SampleRules.FLD_DEFAULT_STATUS.getName()};            
            Object[][] records = Rdbms.getRecordFieldsByFilter(schemaConfigName, TblsCnfg.SampleRules.TBL.getName(), 
                    fieldNames, fieldValues, fieldFilter);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(records[0][0].toString())){
                myDiagnoses = "ERROR: Problem on getting sample rules for " + template + " exists but the rule record is missing in the schema "+schemaConfigName;            
                return myDiagnoses;
            }
            String statuses = records[0][2].toString();
            if (LPArray.valueInArray(statuses.split("\\|", -1), status)){
                myDiagnoses = DIAGNOSES_SUCCESS;                            
            }else{
                myDiagnoses = "ERROR: The status " + status + " is not of one the defined status (" + statuses + " for the template " + template + " exists but the rule record is missing in the schema "+schemaConfigName;                                            
            }            
        }        
        return myDiagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param template
     * @param templateVersion
     * @return
     */
    private String specialFieldCheckSampleSpecCode( String schemaPrefix, String template, Integer templateVersion){ 
        String schemaConfigName = LPPlatform.SCHEMA_CONFIG;
        
        schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, schemaConfigName); 

        Integer specialFieldIndex = Arrays.asList(mandatoryFields).indexOf(TblsData.Sample.FLD_SPEC_CODE.getName());
        String specCode = (String) mandatoryFieldsValue[specialFieldIndex];     
        if (specCode.length()==0)return "ERROR: The parameter spec_code cannot be null";  

        specialFieldIndex = Arrays.asList(mandatoryFields).indexOf(TblsData.Sample.FLD_SPEC_CODE_VERSION.getName());
        Integer specCodeVersion = (Integer) mandatoryFieldsValue[specialFieldIndex];     
        if (specCodeVersion==null) return "ERROR: The parameter spec_code_version cannot be null"; 

        specialFieldIndex = Arrays.asList(mandatoryFields).indexOf(TblsData.Sample.FLD_SPEC_VARIATION_NAME.getName());
        String specVariationName = (String) mandatoryFieldsValue[specialFieldIndex];     
        if (specVariationName.length()==0)return "ERROR: The parameter spec_variation_name cannot be null"; 
                
        Object[] diagnosis = Rdbms.existsRecord(schemaConfigName, TblsCnfg.SpecLimits.TBL.getName(), 
                new String[] {TblsData.Sample.FLD_SPEC_CODE.getName(), "config_version", TblsData.Sample.FLD_SPEC_VARIATION_NAME.getName()}, 
                new Object[] {specCode, specCodeVersion, specVariationName});
        
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnosis[0].toString()))
            return "ERROR: The sample_rule record for "+template+" does not exist in schema"+schemaConfigName+". ERROR: "+diagnosis[5];
        
        return DIAGNOSES_SUCCESS; 
    }
    /**
     *  Automate the sample analysis assignment as to be triggered by any sample action.<br>
     *      Assigned to the actions: LOGSAMPLE.
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param sampleFieldName
     * @param sampleFieldValue
     * @param eventName
     * @param preAuditId
     * @param transactionId
     */
    public void autoSampleAliquoting( String schemaPrefix, Token token, Integer sampleId, String[] sampleFieldName, Object[] sampleFieldValue, String eventName, Integer transactionId, Integer preAuditId){
        LPParadigm.fieldNameValueArrayChecker(sampleFieldName, sampleFieldValue);
// This code is commented because the method, at least by now, return void instead of anything else        
    }
       
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param smpAliqFieldName
     * @param smpAliqFieldValue
     * @return
     */
    public Object[] logSampleAliquot( String schemaPrefix, Token token, Integer sampleId, String[] smpAliqFieldName, Object[] smpAliqFieldValue) {    
        //String parentTableName = TblsData.Sample.TBL.getName();        

        Object[] fieldNameValueArrayChecker = LPParadigm.fieldNameValueArrayChecker(smpAliqFieldName, smpAliqFieldValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(fieldNameValueArrayChecker[0].toString())){return fieldNameValueArrayChecker;}        

        BigDecimal aliqVolume = BigDecimal.ZERO;
        String aliqVolumeuom = "";

        String actionEnabledSampleAliquotVolumeRequired = Parameter.getParameterBundle(schemaPrefix.replace("\"", "")+"-data", "sampleAliquot_volumeRequired");   
        if (actionEnabledSampleAliquotVolumeRequired.toUpperCase().contains(LPPlatform.BUSINESS_RULES_VALUE_ENABLED)){
            String[] mandatorySampleFields = new String[]{TblsData.Sample.FLD_VOLUME_FOR_ALIQ.getName(), TblsData.Sample.FLD_VOLUME_FOR_ALIQ_UOM.getName()};
            String[] mandatorySampleAliqFields = new String[]{"volume", "volume_uom"};
            Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                    new String[] {TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, mandatorySampleFields);
            if ( (sampleInfo[0][0]!=null) && (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())) ) return LPArray.array2dTo1d(sampleInfo);

            if (sampleInfo[0][1]==null) 
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_sampleAliquoting_volumeCannotBeNegativeorZero", 
                    new Object[]{"null", sampleId, schemaPrefix});                 
                      
            BigDecimal smpVolume = new BigDecimal(sampleInfo[0][0].toString());           
            String smpVolumeuom = (String) sampleInfo[0][1];  

            aliqVolume = new BigDecimal(smpAliqFieldValue[LPArray.valuePosicInArray(smpAliqFieldName, smpAliqFieldName[0])].toString());         
            aliqVolumeuom = (String) smpAliqFieldValue[LPArray.valuePosicInArray(mandatorySampleAliqFields, smpAliqFieldName[1])];

            Object[] diagnoses = LPMath.extractPortion(schemaPrefix, smpVolume, smpVolumeuom, sampleId, aliqVolume, aliqVolumeuom, -999);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnoses[0].toString())){return diagnoses;}    

            aliqVolume = new BigDecimal(diagnoses[diagnoses.length-1].toString());

            smpVolume = smpVolume.add(aliqVolume.negate());
            String[] smpVolFldName = new String[]{TblsData.Sample.FLD_VOLUME_FOR_ALIQ.getName()};
            Object[] smpVolFldValue = new Object[]{smpVolume};
            Object[] updateSampleVolume = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                    smpVolFldName, smpVolFldValue, new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateSampleVolume[0].toString())){
                return updateSampleVolume;}    
            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAuditAdd(schemaPrefix, SampleAudit.SampleAuditEvents.LOG_SAMPLE_ALIQUOT.toString(), TblsData.Sample.TBL.getName(), sampleId, 
                    sampleId, null, null, 
                    LPArray.joinTwo1DArraysInOneOf1DString(smpVolFldName, smpVolFldValue, ":"), token, null);        
        }
        smpAliqFieldName = LPArray.addValueToArray1D(smpAliqFieldName, TblsData.SampleAliq.FLD_SAMPLE_ID.getName());
        smpAliqFieldValue = LPArray.addValueToArray1D(smpAliqFieldValue, sampleId);
        smpAliqFieldName = LPArray.addValueToArray1D(smpAliqFieldName, TblsData.SampleAliq.FLD_VOLUME_FOR_ALIQ.getName());
        smpAliqFieldValue = LPArray.addValueToArray1D(smpAliqFieldValue, aliqVolume);
        smpAliqFieldName = LPArray.addValueToArray1D(smpAliqFieldName, TblsData.SampleAliq.FLD_VOLUME_FOR_ALIQ_UOM.getName());
        smpAliqFieldValue = LPArray.addValueToArray1D(smpAliqFieldValue, aliqVolumeuom);
        smpAliqFieldName = LPArray.addValueToArray1D(smpAliqFieldName,TblsData.SampleAliq.FLD_CREATED_BY.getName());
        smpAliqFieldValue = LPArray.addValueToArray1D(smpAliqFieldValue, token.getPersonName());
        smpAliqFieldName = LPArray.addValueToArray1D(smpAliqFieldName, TblsData.SampleAliq.FLD_CREATED_ON.getName());  
        smpAliqFieldValue = LPArray.addValueToArray1D(smpAliqFieldValue, LPDate.getCurrentTimeStamp());

        Object[] diagnoses = Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAliq.TBL.getName(), smpAliqFieldName, smpAliqFieldValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            errorCode = ERROR_TRAPPING_ERROR_INSERTING_SAMPLE_RECORD;
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, diagnoses[diagnoses.length-2]);
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
        }
        if (Rdbms.TBL_NO_KEY.equalsIgnoreCase(diagnoses[diagnoses.length-1].toString())){            
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Object created but aliquot id cannot be get back to continue with the logic", errorDetailVariables);
        }
        Integer aliquotId = Integer.parseInt(diagnoses[diagnoses.length-1].toString());
        Object[] fieldsOnLogSample = LPArray.joinTwo1DArraysInOneOf1DString(smpAliqFieldName, smpAliqFieldValue, ":");
        SampleAudit smpAudit = new SampleAudit();

        smpAudit.sampleAliquotingAuditAdd(schemaPrefix, token, SampleAudit.SampleAuditEvents.LOG_SAMPLE_ALIQUOT.toString(), TblsData.SampleAliq.TBL.getName(), aliquotId, null, aliquotId,
                sampleId, null, null, 
                fieldsOnLogSample, null);
        Integer transactionId = null;

        return diagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param aliquotId
     * @param smpSubAliqFieldName
     * @param smpSubAliqFieldValue
     * @return
     */
    public Object[] logSampleSubAliquot(String schemaPrefix, Token token, Integer aliquotId, String[] smpSubAliqFieldName, Object[] smpSubAliqFieldValue) {
        Object[] fieldNameValueArrayChecker = LPParadigm.fieldNameValueArrayChecker(smpSubAliqFieldName, smpSubAliqFieldValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(fieldNameValueArrayChecker[0].toString())){return fieldNameValueArrayChecker;}          

        Integer sampleId = 0;
        String[] mandatoryAliquotFields = new String[]{TblsData.SampleAliq.FLD_SAMPLE_ID.getName()};
        String actionEnabledSampleSubAliquotVolumeRequired = Parameter.getParameterBundle(schemaPrefix.replace("\"", "")+"-data", "sampleSubAliquot_volumeRequired");             

        if (actionEnabledSampleSubAliquotVolumeRequired.toUpperCase().contains(LPPlatform.BUSINESS_RULES_VALUE_ENABLED)){
            mandatoryAliquotFields = LPArray.addValueToArray1D(mandatoryAliquotFields, TblsData.SampleAliq.FLD_VOLUME_FOR_ALIQ.getName());
            mandatoryAliquotFields = LPArray.addValueToArray1D(mandatoryAliquotFields, TblsData.SampleAliq.FLD_VOLUME_FOR_ALIQ_UOM.getName());

            String[] mandatorySampleSubAliqFields = new String[]{TblsData.SampleAliq.FLD_VOLUME.getName(), TblsData.SampleAliq.FLD_VOLUME_UOM.getName()};
            Object[][] aliquotInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA),  TblsData.SampleAliq.TBL.getName(), 
                    new String[] {TblsData.SampleAliq.FLD_ALIQUOT_ID.getName()}, new Object[]{aliquotId}, mandatoryAliquotFields);
             if ( (aliquotInfo[0][0]!=null) && (LPPlatform.LAB_FALSE.equalsIgnoreCase(aliquotInfo[0][0].toString())) ){
                return LPArray.array2dTo1d(aliquotInfo);}    
            for (String fv: mandatorySampleSubAliqFields){
                if (LPArray.valuePosicInArray(smpSubAliqFieldName, fv) == -1) 
                    return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_sampleSubAliquoting_volumeAndUomMandatory", 
                        new Object[]{"sampleAliquot_volumeRequired", Arrays.toString(smpSubAliqFieldName), aliquotId, schemaPrefix});                
            }
            if (aliquotInfo[0][1]==null) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_sampleAliquoting_volumeCannotBeNegativeorZero", 
                                                    new Object[]{"null", sampleId, schemaPrefix});                
            sampleId = (Integer) aliquotInfo[0][0];
            BigDecimal aliqVolume = new BigDecimal(aliquotInfo[0][1].toString());           
            String aliqVolumeuom = (String) aliquotInfo[0][2];  

            BigDecimal subAliqVolume = new BigDecimal(smpSubAliqFieldValue[LPArray.valuePosicInArray(smpSubAliqFieldName, smpSubAliqFieldName[0])].toString());         
            String subAliqVolumeuom = (String) smpSubAliqFieldValue[LPArray.valuePosicInArray(mandatorySampleSubAliqFields, smpSubAliqFieldName[1])];

            Object[] diagnoses = LPMath.extractPortion(schemaPrefix, aliqVolume, aliqVolumeuom, sampleId, subAliqVolume, subAliqVolumeuom, aliquotId);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnoses[0].toString())){return diagnoses;}    
            subAliqVolume = new BigDecimal(diagnoses[diagnoses.length-1].toString());

            aliqVolume = aliqVolume.add(subAliqVolume.negate());
            String[] smpVolFldName = new String[]{TblsData.SampleAliq.FLD_VOLUME_FOR_ALIQ.getName()};
            Object[] smpVolFldValue = new Object[]{aliqVolume};
            Object[] updateSampleVolume = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAliq.TBL.getName(), 
                    smpVolFldName, smpVolFldValue, new String[]{TblsData.SampleAliq.FLD_ALIQUOT_ID.getName()}, new Object[]{aliquotId});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateSampleVolume[0].toString())){
                return updateSampleVolume;}    
            SampleAudit smpAudit = new SampleAudit();
            smpAudit.sampleAliquotingAuditAdd(schemaPrefix, token, SampleAudit.SampleAuditEvents.LOG_SAMPLE_SUBALIQUOT.toString(), TblsData.SampleAliq.TBL.getName(), aliquotId, null, aliquotId, 
                    sampleId, null, null, 
                    LPArray.joinTwo1DArraysInOneOf1DString(smpVolFldName, smpVolFldValue, ":"), null);        
        }
        if (!actionEnabledSampleSubAliquotVolumeRequired.toUpperCase().contains(LPPlatform.BUSINESS_RULES_VALUE_ENABLED)){
            Object[][] aliquotInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAliq.TBL.getName(), new String[] {TblsData.SampleAliq.FLD_ALIQUOT_ID.getName()}, new Object[]{aliquotId}, mandatoryAliquotFields);
            sampleId = (Integer) aliquotInfo[0][0];
        }
        smpSubAliqFieldName = LPArray.addValueToArray1D(smpSubAliqFieldName, TblsData.SampleAliqSub.FLD_SAMPLE_ID.getName());
        smpSubAliqFieldValue = LPArray.addValueToArray1D(smpSubAliqFieldValue, sampleId);
        smpSubAliqFieldName = LPArray.addValueToArray1D(smpSubAliqFieldName, TblsData.SampleAliqSub.FLD_ALIQUOT_ID.getName());
        smpSubAliqFieldValue = LPArray.addValueToArray1D(smpSubAliqFieldValue, aliquotId);
        smpSubAliqFieldName = LPArray.addValueToArray1D(smpSubAliqFieldName, TblsData.SampleAliqSub.FLD_CREATED_BY.getName());
        smpSubAliqFieldValue = LPArray.addValueToArray1D(smpSubAliqFieldValue, token.getPersonName());
        smpSubAliqFieldName = LPArray.addValueToArray1D(smpSubAliqFieldName, TblsData.SampleAliqSub.FLD_CREATED_ON.getName());  
        smpSubAliqFieldValue = LPArray.addValueToArray1D(smpSubAliqFieldValue, LPDate.getCurrentTimeStamp());

        Object[] diagnoses = Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAliqSub.TBL.getName(), smpSubAliqFieldName, smpSubAliqFieldValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
            errorCode = ERROR_TRAPPING_ERROR_INSERTING_SAMPLE_RECORD;
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, diagnoses[diagnoses.length-2]);
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
        }
        Integer subaliquotId = Integer.parseInt(diagnoses[diagnoses.length-1].toString());
        Object[] fieldsOnLogSample = LPArray.joinTwo1DArraysInOneOf1DString(smpSubAliqFieldName, smpSubAliqFieldValue, ":");
        SampleAudit smpAudit = new SampleAudit();
        smpAudit.sampleAliquotingAuditAdd(schemaPrefix, token, SampleAudit.SampleAuditEvents.LOG_SAMPLE_SUBALIQUOT.toString(), TblsData.SampleAliqSub.TBL.getName(), subaliquotId, subaliquotId, aliquotId,
                sampleId, null, null, 
                fieldsOnLogSample, null);
        Integer transactionId = null;

        return diagnoses;
    }

    /**
     *
     * @param schemaPrefix
     * @param sampleId
     * @param sampleFieldToRetrieve
     * @param sampleAnalysisFieldToRetrieve
     * @param sampleAnalysisFieldToSort
     * @param sarFieldToRetrieve
     * @param sarFieldToSort
     * @param sampleAuditFieldToRetrieve
     * @param sampleAuditResultFieldToSort
     * @return
     */
    public static String sampleEntireStructureData(String schemaPrefix, Integer sampleId, String sampleFieldToRetrieve, String sampleAnalysisFieldToRetrieve, String sampleAnalysisFieldToSort,
            String sarFieldToRetrieve, String sarFieldToSort, String sampleAuditFieldToRetrieve, String sampleAuditResultFieldToSort){
        
        return sampleEntireStructureDataPostgres(schemaPrefix, sampleId, sampleFieldToRetrieve, sampleAnalysisFieldToRetrieve, sampleAnalysisFieldToSort,
                sarFieldToRetrieve, sarFieldToSort, sampleAuditFieldToRetrieve, sampleAuditResultFieldToSort);
    }
    private static String sampleEntireStructureDataPostgres(String schemaPrefix, Integer sampleId, String sampleFieldToRetrieve, String sampleAnalysisFieldToRetrieve, String sampleAnalysisFieldToSort,
            String sarFieldToRetrieve, String sarFieldToSort, String sampleAuditFieldToRetrieve, String sampleAuditResultFieldToSort){
        
        String [] sampleFieldToRetrieveArr = new String[0];  
            if (SAMPLE_ENTIRE_STRUCTURE_ALL_FIELDS.equalsIgnoreCase(sampleFieldToRetrieve)){                
                sampleFieldToRetrieve = "*";
            }else{
                if (sampleFieldToRetrieve!=null){sampleFieldToRetrieveArr=sampleFieldToRetrieve.split("\\|");                   
                }else {sampleFieldToRetrieveArr=new String[0];}            
                sampleFieldToRetrieveArr = LPArray.addValueToArray1D(sampleFieldToRetrieveArr, new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName(), TblsData.Sample.FLD_STATUS.getName()});
                sampleFieldToRetrieve = LPArray.convertArrayToString(sampleFieldToRetrieveArr, ", ", "");       
            }
        String [] sampleAnalysisFieldToRetrieveArr = new String[0];        
            if (SAMPLE_ENTIRE_STRUCTURE_ALL_FIELDS.equalsIgnoreCase(sampleAnalysisFieldToRetrieve))
                sampleAnalysisFieldToRetrieve="*";
/*                {                
                for (TblsData.SampleAnalysis obj: TblsData.SampleAnalysis.values()){
                    if (!"TBL".equalsIgnoreCase(obj.name()))
                    sampleAnalysisFieldToRetrieveArr=LPArray.addValueToArray1D(sampleAnalysisFieldToRetrieveArr, obj.getName());
                }               } */
            else{
                if (sampleAnalysisFieldToRetrieve!=null){sampleAnalysisFieldToRetrieveArr=sampleAnalysisFieldToRetrieve.split("\\|");                   
                }else {sampleAnalysisFieldToRetrieveArr=new String[0];}
                sampleAnalysisFieldToRetrieveArr = LPArray.addValueToArray1D(sampleAnalysisFieldToRetrieveArr, new String[]{TblsData.SampleAnalysis.FLD_TEST_ID.getName(), TblsData.SampleAnalysis.FLD_STATUS.getName()});
                sampleAnalysisFieldToRetrieve = LPArray.convertArrayToString(sampleAnalysisFieldToRetrieveArr, ", ", "");
            }
            if (sampleAnalysisFieldToSort==null){sampleAnalysisFieldToSort=TblsData.SampleAnalysis.FLD_TEST_ID.getName();}                                       
        String[] sarFieldToRetrieveArr = new String[0];
            if (SAMPLE_ENTIRE_STRUCTURE_ALL_FIELDS.equalsIgnoreCase(sarFieldToRetrieve))
                sarFieldToRetrieve="*";
            /*{                
                for (TblsData.SampleAnalysisResult obj: TblsData.SampleAnalysisResult.values()){
                    if (!"TBL".equalsIgnoreCase(obj.name()))
                    sarFieldToRetrieveArr=LPArray.addValueToArray1D(sarFieldToRetrieveArr, obj.getName());
                }                
            }*/
            else{
                if (sarFieldToRetrieve!=null){sarFieldToRetrieveArr=sarFieldToRetrieve.split("\\|");                   
                }else {sarFieldToRetrieveArr=new String[0];}
                sarFieldToRetrieveArr = LPArray.addValueToArray1D(sarFieldToRetrieveArr, new String[]{TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName(), TblsData.SampleAnalysisResult.FLD_STATUS.getName()});
                sarFieldToRetrieve = LPArray.convertArrayToString(sarFieldToRetrieveArr, ", ", "");
            }
            if (sarFieldToSort==null){sarFieldToSort=TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName();}                                        
        String[] sampleAuditFieldToRetrieveArr = new String[0];
            if (SAMPLE_ENTIRE_STRUCTURE_ALL_FIELDS.equalsIgnoreCase(sampleAuditFieldToRetrieve))
                sampleAuditFieldToRetrieve="*";
            /*{                
                for (TblsDataAudit.Sample obj: TblsDataAudit.Sample.values()){
                    if (!"TBL".equalsIgnoreCase(obj.name()))
                    sampleAuditFieldToRetrieveArr=LPArray.addValueToArray1D(sampleAuditFieldToRetrieveArr, obj.getName());
                }                
            }*/
            else{
                if (sampleAuditFieldToRetrieve!=null){sampleAuditFieldToRetrieveArr=sampleAuditFieldToRetrieve.split("\\|");                   
                }else {sampleAuditFieldToRetrieveArr=new String[0];}
                sampleAuditFieldToRetrieveArr = LPArray.addValueToArray1D(sampleAuditFieldToRetrieveArr, 
                        new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName(), TblsDataAudit.Sample.FLD_TRANSACTION_ID.getName(), 
                             TblsDataAudit.Sample.FLD_ACTION_NAME.getName(), TblsDataAudit.Sample.FLD_PERSON.getName(), TblsDataAudit.Sample.FLD_USER_ROLE.getName()});
                sampleAuditFieldToRetrieve = LPArray.convertArrayToString(sampleAuditFieldToRetrieveArr, ", ", "");
            }
            if (sampleAuditResultFieldToSort==null){sampleAuditResultFieldToSort=TblsDataAudit.Sample.FLD_AUDIT_ID.getName();}                                    
        try {
            String schemaData = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);
            String schemaDataAudit = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT);
            String sqlSelect=" select ";
            String sqlFrom=" from ";
            String sqlOrderBy=" order by ";
            String qry = "";
            qry = qry  + "select row_to_json(sQry)from "
                    +" ( "+sqlSelect+" "+sampleFieldToRetrieve+", "
                    +" ( "+sqlSelect+" COALESCE(array_to_json(array_agg(row_to_json(saQry))),'[]') from  "
                    +"( "+sqlSelect+" "+sampleAnalysisFieldToRetrieve+", "
                    +"( "+sqlSelect+" COALESCE(array_to_json(array_agg(row_to_json(sarQry))),'[]') from "
                    +"( "+sqlSelect+" "+sarFieldToRetrieve+" from "+schemaData+".sample_analysis_result sar where sar.test_id=sa.test_id "
                    +sqlOrderBy+sarFieldToSort+"     ) sarQry    ) as sample_analysis_result "
                    +sqlFrom+schemaData+".sample_analysis sa where sa.sample_id=s.sample_id "
                    +sqlOrderBy+sampleAnalysisFieldToSort+"      ) saQry    ) as sample_analysis,"
                    + "( "+sqlSelect+" COALESCE(array_to_json(array_agg(row_to_json(sauditQry))),'[]') from  "
                    +"( "+sqlSelect+" "+sampleAuditFieldToRetrieve
                    +sqlFrom+schemaDataAudit+".sample saudit where saudit.sample_id=s.sample_id "
                    +sqlOrderBy+sampleAuditResultFieldToSort+"      ) sauditQry    ) as sample_audit "
                    +sqlFrom+schemaData+".sample s where s.sample_id in ("+ "?"+" ) ) sQry   ";
            
            CachedRowSet prepRdQuery = Rdbms.prepRdQuery(qry, new Object[]{sampleId});
            
            
            String finalString = "";
            if (prepRdQuery.getString(1)==null){
                return LPPlatform.LAB_FALSE;
            }
            return prepRdQuery.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataSample.class.getName()).log(Level.SEVERE, null, ex);
            return LPPlatform.LAB_FALSE;
        }        
    }

}
