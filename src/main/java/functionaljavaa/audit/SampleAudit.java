/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.audit;

import databases.Rdbms;
import databases.TblsApp;
import databases.TblsData;
import databases.TblsDataAudit;
import databases.Token;
import functionaljavaa.parameter.Parameter;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPSession;
import java.util.Arrays;
import functionaljavaa.requirement.Requirement;
import functionaljavaa.samplestructure.DataSampleStages;
import lbplanet.utilities.LPDate;

/**
 * 
 * @author Fran Gomez
 * @version 0.1
 */
public class SampleAudit {
    
    /**
     *
     */
    
    public enum SampleAuditEvents{

        /**
         * 
         */
        SAMPLE_LOGGED,

        /**
         *
         */
        SAMPLE_RECEIVED,

        /**
         *
         */
        SET_SAMPLING_DATE,

        /**
         *
         */
        SAMPLE_CHANGE_SAMPLING_DATE,

        /**
         *
         */
        SAMPLE_RECEPTION_COMMENT_ADD,

        /**
         *
         */
        SAMPLE_RECEPTION_COMMENT_REMOVE,

        /**
         *
         */
        SAMPLE_EVALUATE_STATUS
        ,

        /**
         *
         */
        SAMPLE_REVIEWED,

        /**
         *
         */
        LOG_SAMPLE_ALIQUOT,

        /**
         *
         */
        LOG_SAMPLE_SUBALIQUOT,

        /**
         *
         */
        SAMPLESTAGE_MOVETONEXT,

        /**
         *
         */
        SAMPLESTAGE_MOVETOPREVIOUS,

        /**
         *
         */
        UPDATE_LAST_ANALYSIS_USER_METHOD
        , 

        /**
         *
         */
        CHAIN_OF_CUSTODY_STARTED, 

        /**
         *
         */
        CHAIN_OF_CUSTODY_COMPLETED, 

        /**
         *
         */
        MICROORGANISM_ADDED, 

        /**
         *
         */
        SAMPLE_SET_INCUBATION_STARTED, 

        /**
         *
         */
        SAMPLE_SET_INCUBATION_ENDED, 

        /**
         *
         */
        SAMPLE_CANCELED, 

        /**
         *
         */
        SAMPLE_UNCANCELED};   

    /**
     *
     */
    public enum SampleAnalysisAuditEvents{ 

        /**
         *
         */
        SAMPLE_ANALYSIS_REVIEWED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_EVALUATE_STATUS, 

        /**
         *
         */
        SAMPLE_ANALYSIS_ANALYST_ASSIGNMENT, 

        /**
         *
         */
        SAMPLE_ANALYSIS_ADDED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_CANCELED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_UNCANCELED};
    
    /**
     *
     */
    public enum SampleAnalysisResultAuditEvents{ 

        /**
         *
         */
        BACK_FROM_CANCEL, 

        /**
         *
         */
        SAMPLE_ANALYSIS_RESULT_ENTERED, 

        /**
         *
         */
        UOM_CHANGED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_RESULT_CANCELED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_RESULT_UNCANCELED, 

        /**
         *
         */
        SAMPLE_ANALYSIS_RESULT_REVIEWED};
    String classVersion = "0.1";
    Integer auditId=0;
      
    /**
     *
     */
    public static final String PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE= "sampleAuditRevisionMode";
    public static final String PARAMETER_BUNDLE_SAMPLE_AUDIT_AUTHOR_CAN_REVIEW_TOO= "sampleAuditAuthorCanBeReviewerToo";
    public static final String PARAMETER_BUNDLE_SAMPLE_AUDIT_CHILD_REVISION_REQUIRED= "sampleAuditChildRevisionRequired";
    
    

/**
 * Add one record in the audit table when altering any of the levels belonging to the sample structure when not linked to any other statement.
 * @param schemaPrefix String - Procedure Name
 * @param action String - Action being performed
 * @param tableName String - table where the action was performed into the Sample structure
 * @param tableId Integer - Id for the object where the action was performed.
     * @param sampleId
 * @param testId Integer - testId
 * @param resultId Integer - resultId
 * @param auditlog Object[] - All data that should be stored in the audit as part of the action being performed
     * @param token
@param parentAuditId paranet audit id when creating a child-record
     * @return  
 */    
    public Object[] sampleAuditAdd(String schemaPrefix, String action, String tableName, Integer tableId, 
                        Integer sampleId, Integer testId, Integer resultId, Object[] auditlog, Token token, Integer parentAuditId) {
        String[] fieldNames = new String[]{TblsDataAudit.Sample.FLD_DATE.getName()};
        Object[] fieldValues = new Object[]{LPDate.getCurrentTimeStamp()};
        
        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE_VERSION.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }        
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_ACTION_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, action);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TABLE_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableName);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TABLE_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableId);
        if (sampleId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_SAMPLE_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, sampleId);
        }    
        if (testId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TEST_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, testId);
        }    
        if (resultId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_RESULT_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, resultId);
        }    
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_FIELDS_UPDATED.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Arrays.toString(auditlog));
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_USER_ROLE.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getUserRole());

        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PERSON.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getPersonName());
        if (token.getAppSessionId()!=null){
            Object[] appSession = LPSession.addProcessSession( LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), Integer.valueOf(token.getAppSessionId()), new String[]{TblsApp.AppSession.FLD_DATE_STARTED.getName()});
       
    //        Object[] appSession = labSession.getAppSession(appSessionId, new String[]{"date_started"});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(appSession[0].toString())){
                return appSession;
            }else{

                fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_APP_SESSION_ID.getName());
                fieldValues = LPArray.addValueToArray1D(fieldValues, Integer.valueOf(token.getAppSessionId()));            
            }
        }
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TRANSACTION_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
        if (parentAuditId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PARENT_AUDIT_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, parentAuditId);
        }    
        
/*        String jsonString = null;
        jsonString = sampleJsonString(schemaPrefix+"-data", sampleId);
        if ((jsonString!=null)){
        //if (!jsonString.isEmpty()){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "picture_after");
            fieldValues = LPArray.addValueToArray1D(fieldValues, jsonString);            
        }
*/        
//        fieldNames = LPArray.addValueToArray1D(fieldNames, "user");
//        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);        

        return Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(), 
                fieldNames, fieldValues);
    }

    /**
     *
     * @param schemaPrefix
     * @param action
     * @param tableName
     * @param tableId
     * @param aliquotId
     * @param sampleId
     * @param testId
     * @param resultId
     * @param auditlog
     * @param userName
     * @param userRole
     * @param sessionId
     */
    public void sampleAuditAddObsolete(String schemaPrefix, String action, String tableName, Integer tableId, Integer aliquotId, Integer sampleId, Integer testId, Integer resultId, Object[] auditlog, String userName, String userRole, Integer sessionId) {
        
        String[] fieldNames = new String[0];
        Object[] fieldValues = new Object[0];
        
        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE_VERSION.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }        
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_ACTION_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, action);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TABLE_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableName);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TABLE_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableId);
        if (sessionId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "session_id");
            fieldValues = LPArray.addValueToArray1D(fieldValues, sessionId);
        }    
        if (aliquotId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "aliquot_id");
            fieldValues = LPArray.addValueToArray1D(fieldValues, aliquotId);
        }    
        if (sampleId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_SAMPLE_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, sampleId);
        }    
        if (testId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TEST_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, testId);
        }    
        if (resultId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_RESULT_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, resultId);
        }    
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_FIELDS_UPDATED.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Arrays.toString(auditlog));
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_USER_ROLE.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, userRole);

        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PERSON.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TRANSACTION_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
                
/*        String jsonString = null;
        jsonString = sampleJsonString(schemaPrefix+"-data", sampleId);
        if ((jsonString!=null)){
        //if (!jsonString.isEmpty()){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "picture_after");
            fieldValues = LPArray.addValueToArray1D(fieldValues, jsonString);            
        }
*/        
//        fieldNames = LPArray.addValueToArray1D(fieldNames, "user");
//        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);        
           Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(),
                   fieldNames, fieldValues);
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param action
     * @param tableName
     * @param tableId
     * @param subaliquotId
     * @param aliquotId
     * @param sampleId
     * @param testId
     * @param resultId
     * @param auditlog
     * @param parentAuditId
     */
    public void sampleAliquotingAuditAdd( String schemaPrefix, Token token, String action, String tableName, Integer tableId, Integer subaliquotId, Integer aliquotId, Integer sampleId, Integer testId, Integer resultId, Object[] auditlog, Integer parentAuditId) {
        
        String[] fieldNames = new String[]{TblsDataAudit.Sample.FLD_DATE.getName(), TblsDataAudit.Sample.FLD_ACTION_NAME.getName(), TblsDataAudit.Sample.FLD_TABLE_NAME.getName(),
          TblsDataAudit.Sample.FLD_TABLE_ID.getName(), TblsDataAudit.Sample.FLD_FIELDS_UPDATED.getName(), TblsDataAudit.Sample.FLD_USER_ROLE.getName(),
          TblsDataAudit.Sample.FLD_PERSON.getName(), TblsDataAudit.Sample.FLD_TRANSACTION_ID.getName()};
        Object[] fieldValues = new Object[]{LPDate.getCurrentTimeStamp(), action, tableName, tableId, Arrays.toString(auditlog), token.getUserRole(), token.getPersonName(), Rdbms.getTransactionId()};

        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_PROCEDURE_VERSION.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }                
        if (token.getAppSessionId()!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "session_id");
            fieldValues = LPArray.addValueToArray1D(fieldValues, token.getAppSessionId());
        }    
        if (subaliquotId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "subaliquot_id");
            fieldValues = LPArray.addValueToArray1D(fieldValues, subaliquotId);
        }    
        if (aliquotId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "aliquot_id");
            fieldValues = LPArray.addValueToArray1D(fieldValues, aliquotId);
        }    
        if (sampleId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_SAMPLE_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, sampleId);
        }    
        if (testId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TEST_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, testId);
        }    
        if (resultId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_RESULT_ID.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, resultId);
        }                   
/*        String jsonString = null;
        jsonString = sampleJsonString(schemaPrefix+"-data", sampleId);
        if ((jsonString!=null)){
        //if (!jsonString.isEmpty()){
            fieldNames = LPArray.addValueToArray1D(fieldNames, "picture_after");
            fieldValues = LPArray.addValueToArray1D(fieldValues, jsonString);            
        }
*/        
//        fieldNames = LPArray.addValueToArray1D(fieldNames, "user");
//        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);        
           Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(),
                   fieldNames, fieldValues);
    }
    
    
/*    public void sampleAuditAdd( String schemaPrefix, String action, String tableName, Integer tableId, Integer sampleId, Integer testId, Integer resultId, Object[] auditlog, String userName, String userRole) {
        String auditTableName = TABLE_NAME_DATA_AUDIT_SAMPLE;
        String schemaName = LPPlatform.SCHEMA_DATA_AUDIT;                
        schemaName = LPPlatform.buildSchemaName(schemaPrefix, schemaName);                
        
        String[] fieldNames = new String[0];
        Object[] fieldValues = new Object[0];
        
        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_PROCEDURE);
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_PROCEDURE_VERSION);
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }        
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_ACTION_NAME);
        fieldValues = LPArray.addValueToArray1D(fieldValues, action);
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_TABLE_NAME);
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableName);
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_TABLE_ID);
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableId);
        if (sampleId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_SAMPLE_ID);
            fieldValues = LPArray.addValueToArray1D(fieldValues, sampleId);
        }    
        if (testId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_TEST_ID);
            fieldValues = LPArray.addValueToArray1D(fieldValues, testId);
        }    
        if (resultId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_RESULT_ID);
            fieldValues = LPArray.addValueToArray1D(fieldValues, resultId);
        }    
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_FIELDS_UPDATED);
        fieldValues = LPArray.addValueToArray1D(fieldValues, Arrays.toString(auditlog));
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_USER_ROLE);
        fieldValues = LPArray.addValueToArray1D(fieldValues, userRole);

        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_PERSON);
        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, FIELD_NAME_DATA_AUDIT_SAMPLE_TRANSACTION_ID);
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
                
//        String jsonString = null;
//        jsonString = sampleJsonString(schemaPrefix+"-data", sampleId);
//        if ((jsonString!=null)){
//        //if (!jsonString.isEmpty()){
//            fieldNames = LPArray.addValueToArray1D(fieldNames, "picture_after");
//            fieldValues = LPArray.addValueToArray1D(fieldValues, jsonString);            
//        }
        
//        fieldNames = LPArray.addValueToArray1D(fieldNames, "user");
//        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);        
           Rdbms.insertRecordInTable(schemaName, auditTableName, fieldNames, fieldValues);
    }
*/
 /**
 * Not recommended. reduced version of
 * @param schemaPrefix String - Procedure Name
 * @param action String - Action being performed
 * @param tableName String - table where the action was performed into the Sample structure
 * @param tableId Integer - Id for the object where the action was performed.
 * @param auditlog Object[] - All data that should be stored in the audit as part of the action being performed
 * @param userName String - User who performed the action.
 */    
    public void sampleAuditAddObsolete( String schemaPrefix, String action, String tableName, Integer tableId, Object[] auditlog, String userName){
        
        String[] fieldNames = new String[]{TblsDataAudit.Sample.FLD_ACTION_NAME.getName(), TblsDataAudit.Sample.FLD_TABLE_NAME.getName(),
          TblsDataAudit.Sample.FLD_TABLE_ID.getName(), TblsDataAudit.Sample.FLD_SAMPLE_ID.getName()};
        Object[] fieldValues = new Object[]{action, tableName, tableId, tableId};
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsDataAudit.Sample.FLD_TRANSACTION_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
//        fieldNames = LPArray.addValueToArray1D(fieldNames, "user");
//        fieldValues = LPArray.addValueToArray1D(fieldValues, userName);        
           Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(),
                   fieldNames, fieldValues);
    }

    /**
     *
     * @param schemaPrefix
     * @param auditId
     * @param personName
     * @return
     */
    public static Object[] sampleAuditSetAuditRecordAsReviewed(String schemaPrefix, Integer auditId, String personName){
        String auditAuthorCanBeReviewerMode = Parameter.getParameterBundle("config", schemaPrefix, "procedure", PARAMETER_BUNDLE_SAMPLE_AUDIT_AUTHOR_CAN_REVIEW_TOO, null);  
        if (!"TRUE".equalsIgnoreCase(auditAuthorCanBeReviewerMode)){
            Object[][] auditInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(), 
                new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName()}, new Object[]{auditId}, 
                new String[]{TblsDataAudit.Sample.FLD_PERSON.getName()}, new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName()});
            if (LPPlatform.LAB_TRUE.equalsIgnoreCase(auditInfo[0][0].toString())) return LPArray.array2dTo1d(auditInfo);
            if (personName.equalsIgnoreCase(auditInfo[0][0].toString())) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Same person cannot review its own actions", new Object[]{});
        }
        return Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(), 
            new String[]{TblsDataAudit.Sample.FLD_REVIEWED.getName(), TblsDataAudit.Sample.FLD_REVIEWED_BY.getName(), TblsDataAudit.Sample.FLD_REVIEWED_ON.getName()}, 
            new Object[]{true, personName, LPDate.getCurrentTimeStamp()}, 
            new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName()}, new Object[]{auditId});
    }
    
    /**
     *
     * @param schemaPrefix
     * @param sampleId
     * @return
     */
    public static Object[] sampleAuditRevisionPass(String schemaPrefix, Integer sampleId){
      String[] auditRevisionModesRequired=new String[]{"ENABLE", "DISABLE"};
      String auditRevisionMode = Parameter.getParameterBundle("config", schemaPrefix, "procedure", PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, null);  
      String auditRevisionChildRequired = Parameter.getParameterBundle("config", schemaPrefix, "procedure", PARAMETER_BUNDLE_SAMPLE_AUDIT_CHILD_REVISION_REQUIRED, null);   
      if (auditRevisionMode==null || auditRevisionMode.length()==0) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "sampleAuditRevisionMode_ParameterMissing", 
                new Object[]{PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, schemaPrefix});
      String[] auditRevisionModeArr= auditRevisionMode.split("\\|");
      Boolean auditRevisionModeRecognized=false;
      for (String curModeRequired: auditRevisionModesRequired){
        if (LPArray.valuePosicInArray(auditRevisionModeArr, curModeRequired)>-1) auditRevisionModeRecognized= true; 
      }
      if (!auditRevisionModeRecognized)return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "sampleAuditRevisionMode_ParameterMissing", 
                new Object[]{PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, schemaPrefix});
      if (LPArray.valuePosicInArray(auditRevisionModeArr, "DISABLE")>-1)return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "sampleAuditRevisionMode_Disable", 
                new Object[]{PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, schemaPrefix});
      if (LPArray.valuePosicInArray(auditRevisionModeArr, "STAGES")>-1){
        DataSampleStages smpStages = new DataSampleStages(schemaPrefix);
        if (!smpStages.isSampleStagesEnable())return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "sampleAuditRevisionMode_StagesDetectedButSampleStagesNotEnable", 
                new Object[]{PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, schemaPrefix});
        Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                new String[]{TblsData.Sample.FLD_CURRENT_STAGE.getName()});
        String sampleCurrentStage=sampleInfo[0][0].toString();
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleCurrentStage)) return LPArray.array2dTo1d(sampleInfo);
        if (LPArray.valuePosicInArray(auditRevisionModeArr, sampleInfo[0][0].toString())==-1) return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "currentSampleStageNotRequiresSampleAuditRevision", 
                new Object[]{sampleCurrentStage, sampleId, PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE, schemaPrefix});
      }
      String[] whereFieldName=new String[]{TblsDataAudit.Sample.FLD_SAMPLE_ID.getName()};
      Object[] whereFieldValue=new Object[]{sampleId, false};
    
      if ("FALSE".equalsIgnoreCase(auditRevisionChildRequired))
          whereFieldName=LPArray.addValueToArray1D(whereFieldName, TblsDataAudit.Sample.FLD_PARENT_AUDIT_ID.getName()+" is null");
      Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsDataAudit.Sample.TBL.getName(), 
              whereFieldName, whereFieldValue, 
              new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName(), TblsDataAudit.Sample.FLD_REVIEWED.getName()});
      Object[] sampleInfoReviewed1D=LPArray.getColumnFromArray2D(sampleInfo, 1);
      Boolean pendingRecords=true;
      for (Object[] curSampleInfo: sampleInfo){
        if (!"true".equalsIgnoreCase(curSampleInfo[1].toString())) {
          return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "auditRecordsPendingRevision", 
          new Object[]{sampleId, schemaPrefix});
        }
      }      
//      Object[] sampleAuditReviewedValues=LPArray.getUniquesArray(sampleInfoReviewed1D);
//      if ( (sampleAuditReviewedValues.length!=1) || ( (sampleAuditReviewedValues.length==1) && !("true".equalsIgnoreCase(sampleAuditReviewedValues[0].toString())) ) )
      return new Object[]{LPPlatform.LAB_TRUE, "All reviewed"};
    }  
}