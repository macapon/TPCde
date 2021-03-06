/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.modulegenoma;

import com.labplanet.servicios.modulegenoma.TblsGenomaDataAudit;
import databases.Rdbms;
import databases.TblsApp;
//import databases.TblsData;
//import databases.TblsDataAudit;
import databases.Token;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPSession;
import java.util.Arrays;
import functionaljavaa.requirement.Requirement;
import lbplanet.utilities.LPDate;

/**
 * 
 * @author Fran Gomez
 * @version 0.1
 */
public class GenomaDataAudit {
    
    /**
     *
     */
    public enum ProjectAuditEvents{

        NEW_PROJECT, ACTIVATE_PROJECT, DEACTIVATE_PROJECT, PROJECT_ADD_USER, PROJECT_REMOVE_USER, PROJECT_CHANGE_USER_ROLE, PROJECT_USER_ACTIVATE, PROJECT_USER_DEACTIVATE, STUDY_ADDED
    }

    public enum StudyAuditEvents{

        NEW_STUDY, ACTIVATE_STUDY, DEACTIVATE_STUDY, STUDY_ADD_USER, STUDY_REMOVE_USER, STUDY_CHANGE_USER_ROLE, STUDY_USER_ACTIVATE, STUDY_USER_DEACTIVATE,
        NEW_STUDY_INDIVIDUAL, ACTIVATE_STUDY_INDIVIDUAL, DEACTIVATE_STUDY_INDIVIDUAL, UPDATE_STUDY_INDIVIDUAL,
        NEW_STUDY_FAMILY, ACTIVATE_STUDY_FAMILY, DEACTIVATE_STUDY_FAMILY, UPDATE_STUDY_FAMILY, STUDY_FAMILY_ADDED_INDIVIDUAL, STUDY_FAMILY_REMOVED_INDIVIDUAL,
        ADD_VARIABLE_SET_TO_STUDY_OBJECT, STUDY_OBJECT_SET_VARIABLE_VALUE,
        NEW_STUDY_INDIVIDUAL_SAMPLE, ACTIVATE_STUDY_INDIVIDUAL_SAMPLE, DEACTIVATE_STUDY_INDIVIDUAL_SAMPLE, UPDATE_STUDY_INDIVIDUAL_SAMPLE,
        NEW_STUDY_SAMPLES_SET, ACTIVATE_STUDY_SAMPLES_SET, DEACTIVATE_STUDY_SAMPLES_SET, UPDATE_STUDY_SAMPLES_SET, STUDY_SAMPLES_SET_ADDED_SAMPLE, STUDY_SAMPLES_SET_REMOVED_SAMPLE,
    } 
    /**
     *
     */
    String classVersion = "0.1";
    Integer auditId=0;
      
    /**
     *
     */
    public static final String PARAMETER_BUNDLE_SAMPLE_AUDIT_REVISION_MODE= "sampleAuditRevisionMode";
    public static final String PARAMETER_BUNDLE_SAMPLE_AUDIT_CHILD_REVISION_REQUIRED= "sampleAuditChildRevisionRequired";
    

/**
 * Add one record in the audit table when altering any of the levels belonging to the sample structure when not linked to any other statement.
 * @param schemaPrefix String - Procedure Name
 * @param action String - Action being performed
 * @param tableName String - table where the action was performed into the Sample structure
 * @param tableId Integer - Id for the object where the action was performed.
 * @param auditlog Object[] - All data that should be stored in the audit as part of the action being performed
     * @param project
     * @param study
     * @param token
     * @param parentAuditId 
     * @return  
 */    
    public static Object[] projectAuditAdd(String schemaPrefix, Token token, String action, String tableName, String tableId, 
                        String project, String study, Object[] auditlog, Integer parentAuditId) {
        String[] fieldNames = new String[]{TblsGenomaDataAudit.Project.FLD_DATE.getName()};
        Object[] fieldValues = new Object[]{LPDate.getCurrentTimeStamp()};
        
        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_PROCEDURE.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_PROCEDURE_VERSION.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }        
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_ACTION_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, action);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_TABLE_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableName);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_TABLE_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableId);
        if (project!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_PROJECT.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, project);
        }    
        if (study!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_STUDY.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, study);
        }    
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_FIELDS_UPDATED.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Arrays.toString(auditlog));
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_USER_ROLE.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getUserRole());

        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_PERSON.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getPersonName());
        if (token.getAppSessionId()!=null){
            Object[] appSession = LPSession.addProcessSession( LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), Integer.valueOf(token.getAppSessionId()), new String[]{TblsApp.AppSession.FLD_DATE_STARTED.getName()});
       
    //        Object[] appSession = labSession.getAppSession(appSessionId, new String[]{"date_started"});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(appSession[0].toString())){
                return appSession;
            }else{

                fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_APP_SESSION_ID.getName());
                fieldValues = LPArray.addValueToArray1D(fieldValues, Integer.valueOf(token.getAppSessionId()));            
            }
        }
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_TRANSACTION_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
        if (parentAuditId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Project.FLD_PARENT_AUDIT_ID.getName());
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

        return Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsGenomaDataAudit.Project.TBL.getName(), 
                fieldNames, fieldValues);
    }

/**
 * Add one record in the audit table when altering any of the levels belonging to the sample structure when not linked to any other statement.
 * @param schemaPrefix String - Procedure Name
 * @param action String - Action being performed
 * @param tableName String - table where the action was performed into the Sample structure
 * @param tableId Integer - Id for the object where the action was performed.
 * @param auditlog Object[] - All data that should be stored in the audit as part of the action being performed
     * @param project
     * @param study
     * @param token
     * @param parentAuditId 
     * @return  
 */    
    public static Object[] studyAuditAdd(String schemaPrefix, Token token, String action, String tableName, String tableId, 
                           String study, String project, Object[] auditlog, Integer parentAuditId) {
        String[] fieldNames = new String[]{TblsGenomaDataAudit.Study.FLD_DATE.getName()};
        Object[] fieldValues = new Object[]{LPDate.getCurrentTimeStamp()};
        
        Object[][] procedureInfo = Requirement.getProcedureBySchemaPrefix(schemaPrefix);
        if (!(LPPlatform.LAB_FALSE.equalsIgnoreCase(procedureInfo[0][0].toString()))){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_PROCEDURE.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][0]);
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_PROCEDURE_VERSION.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, procedureInfo[0][1]);        
        }        
        
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_ACTION_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, action);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_TABLE_NAME.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableName);
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_TABLE_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, tableId);
        if (project!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_PROJECT.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, project);
        }    
        if (study!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_STUDY.getName());
            fieldValues = LPArray.addValueToArray1D(fieldValues, study);
        }    
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_FIELDS_UPDATED.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Arrays.toString(auditlog));
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_USER_ROLE.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getUserRole());

        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_PERSON.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, token.getPersonName());
        if (token.getAppSessionId()!=null){
            Object[] appSession = LPSession.addProcessSession( LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), Integer.valueOf(token.getAppSessionId()), new String[]{TblsApp.AppSession.FLD_DATE_STARTED.getName()});
       
    //        Object[] appSession = labSession.getAppSession(appSessionId, new String[]{"date_started"});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(appSession[0].toString())){
                return appSession;
            }else{

                fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_APP_SESSION_ID.getName());
                fieldValues = LPArray.addValueToArray1D(fieldValues, Integer.valueOf(token.getAppSessionId()));            
            }
        }
        fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_TRANSACTION_ID.getName());
        fieldValues = LPArray.addValueToArray1D(fieldValues, Rdbms.getTransactionId());            
        if (parentAuditId!=null){
            fieldNames = LPArray.addValueToArray1D(fieldNames, TblsGenomaDataAudit.Study.FLD_PARENT_AUDIT_ID.getName());
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

        return Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA_AUDIT), TblsGenomaDataAudit.Study.TBL.getName(), 
                fieldNames, fieldValues);
    }
    
}

