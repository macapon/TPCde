/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbplanet.utilities;

import databases.Rdbms;
import databases.TblsApp;
import databases.TblsAppAudit;
import databases.TblsDataAudit;
import java.time.LocalDateTime;

/**
 * Create one new app.app_session
 * @author Administrator
 */
public class LPSession {
    private LPSession(){    throw new IllegalStateException("Utility class");}    
   
    /**
     *
     * @param fieldsName
     * @param fieldsValue
     * @param remoteAddr
     * @return
     */
    public static Object[] newAppSession( String[] fieldsName, Object[] fieldsValue, String remoteAddr){        
        LocalDateTime localDateTime=LPDate.getCurrentTimeStamp();
        
        String tableName = TblsApp.AppSession.TBL.getName();
        
        fieldsName = LPArray.addValueToArray1D(fieldsName, TblsApp.AppSession.FLD_DATE_STARTED.getName());
        fieldsValue = LPArray.addValueToArray1D(fieldsValue, localDateTime);

        fieldsName = LPArray.addValueToArray1D(fieldsName, TblsApp.AppSession.FLD_IP_ADDRESS.getName());
        fieldsValue = LPArray.addValueToArray1D(fieldsValue, remoteAddr);
                
                
        return Rdbms.insertRecordInTable(LPPlatform.SCHEMA_APP, tableName, fieldsName, fieldsValue);            
    }
    
    /**
     *  get App Session and get record field values by appSessionId
     * @param appSessionId
     * @param fieldsToRetrieve
     * @return
     */
    public static Object[] getAppSession( Integer appSessionId, String[] fieldsToRetrieve){
        String tableName = TblsApp.AppSession.TBL.getName();
        if (fieldsToRetrieve==null){
            fieldsToRetrieve = LPArray.addValueToArray1D(fieldsToRetrieve, TblsApp.AppSession.FLD_SESSION_ID.getName());
            fieldsToRetrieve = LPArray.addValueToArray1D(fieldsToRetrieve, TblsApp.AppSession.FLD_DATE_STARTED.getName());
        }
        
        Object[][] recordFieldsBySessionId = Rdbms.getRecordFieldsByFilter(LPPlatform.SCHEMA_APP, tableName, 
                new String[]{TblsApp.AppSession.FLD_SESSION_ID.getName()}, new Object[]{appSessionId}, fieldsToRetrieve);
        return LPArray.array2dTo1d(recordFieldsBySessionId);
    }
    
    /**
     * PendingList! - Trap errorsInDatabase for DataIntegrity
     * IdeaList!       - Let the AppSession know in which procedures any action was performed by adding one field to concatenate the procedureNames
     * When the user authenticates then one appSession is created but no ProcessSessions yet due to no action performed yet.<br>
     * This function will replicate to the ProcessSession the session once one action is audited in order to let that any action
     * on this procedure was performed as part of this given appSession.
     * @param processName
     * @param appSessionId
     * @param fieldsNamesToInsert
     * @return
     */
    public static Object[] addProcessSession( String processName, Integer appSessionId, String[] fieldsNamesToInsert){
        addProcessToAppSession(processName, appSessionId);
        String tableName = TblsDataAudit.Session.TBL.getName();
        String schemaAuditName = LPPlatform.buildSchemaName(processName, LPPlatform.SCHEMA_DATA_AUDIT);       
        
        Object[][] recordFieldsBySessionId = Rdbms.getRecordFieldsByFilter(schemaAuditName, tableName, 
                new String[]{TblsDataAudit.Session.FLD_SESSION_ID.getName()}, new Object[]{appSessionId}, fieldsNamesToInsert);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(recordFieldsBySessionId[0][0].toString())){
            Object[] appSession = getAppSession(appSessionId, fieldsNamesToInsert);
            if (!LPArray.valueInArray(fieldsNamesToInsert, TblsDataAudit.Session.FLD_SESSION_ID.getName())){
                fieldsNamesToInsert = LPArray.addValueToArray1D(fieldsNamesToInsert, TblsDataAudit.Session.FLD_SESSION_ID.getName());
                appSession = LPArray.addValueToArray1D(appSession, appSessionId);
            }
            return Rdbms.insertRecordInTable(schemaAuditName, tableName, fieldsNamesToInsert, appSession);
        }
        
        return LPArray.array2dTo1d(recordFieldsBySessionId);
    }
    
    /**
     * One user can be assigned to multiple processes, keep the track about which are the processes for which the user
     *  performed any action at the app_session level is useful to simplify the way to get data across the procedures and audits.
     * @param processName
     * @param appSessionId
     * @return
     */    
    public static Object[] addProcessToAppSession(String processName, Integer appSessionId){
        Object[][] recordFieldsBySessionId = Rdbms.getRecordFieldsByFilter(LPPlatform.SCHEMA_APP, TblsApp.AppSession.TBL.getName(), 
                new String[]{TblsApp.AppSession.FLD_SESSION_ID.getName()}, new Object[]{appSessionId}, 
                new String[]{TblsApp.AppSession.FLD_PROCEDURES.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(recordFieldsBySessionId[0][0].toString()))        
            return LPArray.array2dTo1d(recordFieldsBySessionId);
        String[] sessionProcsArr=LPNulls.replaceNull(recordFieldsBySessionId[0][0]).toString().split("\\|");
        String procListValue=LPNulls.replaceNull(recordFieldsBySessionId[0][0]).toString();
        if (procListValue.length()>0) procListValue=procListValue+"|";
        procListValue=procListValue+processName.replace("-"+LPPlatform.SCHEMA_DATA_AUDIT, "");
        if (!LPArray.valueInArray(sessionProcsArr, processName))
            return Rdbms.updateRecordFieldsByFilter(LPPlatform.SCHEMA_APP, TblsApp.AppSession.TBL.getName(), 
                    new String[]{TblsApp.AppSession.FLD_PROCEDURES.getName()}, new Object[]{procListValue}, 
                    new String[]{TblsApp.AppSession.FLD_SESSION_ID.getName()}, new Object[]{appSessionId});
        return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "The procedure<*1*>already exists for the session<*2*>",new Object[]{processName, appSessionId} );
    }

    public static Object[] addAppSession(Integer appSessionId, String[] fieldsNamesToInsert){
        String tableName = TblsAppAudit.Session.TBL.getName();        
        
        Object[][] recordFieldsBySessionId = Rdbms.getRecordFieldsByFilter(LPPlatform.SCHEMA_APP_AUDIT, tableName, 
                new String[]{TblsAppAudit.Session.FLD_SESSION_ID.getName()}, new Object[]{appSessionId}, fieldsNamesToInsert);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(recordFieldsBySessionId[0][0].toString())){
            Object[] appSession = getAppSession(appSessionId, fieldsNamesToInsert);
            if (!LPArray.valueInArray(fieldsNamesToInsert, TblsAppAudit.Session.FLD_SESSION_ID.getName())){
                fieldsNamesToInsert = LPArray.addValueToArray1D(fieldsNamesToInsert, TblsAppAudit.Session.FLD_SESSION_ID.getName());
                appSession = LPArray.addValueToArray1D(appSession, appSessionId);
            }
            return Rdbms.insertRecordInTable(LPPlatform.SCHEMA_APP_AUDIT, tableName, fieldsNamesToInsert, appSession);
        }
        return LPArray.array2dTo1d(recordFieldsBySessionId);
    }

    
    
    
}
