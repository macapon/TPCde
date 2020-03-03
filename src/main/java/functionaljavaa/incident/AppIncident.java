/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.incident;

import databases.Rdbms;
import databases.TblsApp;
import databases.TblsAppAudit;
import databases.Token;
import functionaljavaa.audit.AppIncidentAudit;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class AppIncident {
    
    private enum IncidentStatuses{
        LOGGED, CONFIRMED, INPROGRESS, WAIT_USER_CONFIRMATION, CLOSED
    }
    enum IncidentAuditEvents{NEW_INCIDENT_CREATED, CONFIRMED_INCIDENT};
    
    
    public static Object[] newIncident(Token token, String incTitle, String incDetail, String sessionInfo){ 
        String[] updFieldName=new String[]{TblsApp.Incident.FLD_DATE_CREATION.getName(), TblsApp.Incident.FLD_TITLE.getName(), TblsApp.Incident.FLD_DETAIL.getName(),
                TblsApp.Incident.FLD_USER_NAME.getName(), TblsApp.Incident.FLD_USER_ROLE.getName(), TblsApp.Incident.FLD_PERSON_NAME.getName(),
                TblsApp.Incident.FLD_STATUS.getName(), TblsApp.Incident.FLD_SESSION_INFO.getName()};
        Object[] updFieldValue=new Object[]{LPDate.getCurrentTimeStamp(), incTitle, incDetail,
                token.getUserName(), token.getUserRole(), token.getPersonName(), 
                IncidentStatuses.LOGGED.toString(), sessionInfo};
        Object[] diagnostic=Rdbms.insertRecordInTable(LPPlatform.SCHEMA_APP, TblsApp.Incident.TBL.getName(), 
            updFieldName, updFieldValue);
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnostic[0].toString())){
            String incIdStr=diagnostic[diagnostic.length-1].toString();
            AppIncidentAudit.IncidentAuditAdd("", token, IncidentAuditEvents.NEW_INCIDENT_CREATED.toString(), TblsAppAudit.Incident.TBL.getName(), Integer.valueOf(incIdStr), incIdStr,  
                        LPArray.joinTwo1DArraysInOneOf1DString(updFieldName, updFieldValue, ":"), null);
        }
        return diagnostic;        
    }
    
    public static Object[] confirmIncident(Token token, Integer incidentId){  
        String[] updFieldName=new String[]{TblsApp.Incident.FLD_STATUS.getName(), TblsApp.Incident.FLD_DATE_CONFIRMED.getName(), TblsApp.Incident.FLD_PERSON_CONFIRMED.getName()};
        Object[] updFieldValue=new Object[]{IncidentStatuses.CONFIRMED.toString(), LPDate.getCurrentTimeStamp(), token.getPersonName()};
        Object[] diagnostic=Rdbms.updateRecordFieldsByFilter(LPPlatform.SCHEMA_APP, TblsApp.Incident.TBL.getName(), 
            updFieldName, updFieldValue, new String[]{TblsApp.Incident.FLD_ID.getName()}, new Object[]{incidentId});
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnostic[0].toString())){            
            AppIncidentAudit.IncidentAuditAdd("", token, IncidentAuditEvents.CONFIRMED_INCIDENT.toString(), TblsAppAudit.Incident.TBL.getName(), incidentId, incidentId.toString(),  
                        LPArray.joinTwo1DArraysInOneOf1DString(updFieldName, updFieldValue, ":"), null);
        }
        return diagnostic;    
    }

}
