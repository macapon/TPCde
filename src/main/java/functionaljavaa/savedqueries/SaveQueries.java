/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.savedqueries;

import databases.Rdbms;
import databases.TblsData;
import databases.Token;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class SaveQueries {

enum SavedQueriesAPIErrorMessages{
//    AAA_FILE_NAME("errorTrapping"),
//    INCIDENT_CURRENTLY_NOT_ACTIVE("incidentCurrentlyNotActive"),
//    INCIDENT_ALREADY_ACTIVE("incidentAlreadyActive"),
    ;
    private SavedQueriesAPIErrorMessages(String sname){
        name=sname;
    } 
    public String getErrorCode(){
        return this.name;
    }
    private final String name;
}

    public static Object[] newSavedQuery(Token token, String schemaPrefix, String name, String definition, String[] fldNames, Object[] fldValues){ 
        String[] updFieldName=new String[]{TblsData.SavedQueries.FLD_NAME.getName(), TblsData.SavedQueries.FLD_DEFINITION.getName(), TblsData.SavedQueries.FLD_OWNER.getName()};
        Object[] updFieldValue=new Object[]{name, definition, token.getPersonName()};
        
        if ( fldNames!=null && fldValues!=null && fldNames[0].length()>0 && fldValues[0].toString().length()>0){
            updFieldName=LPArray.addValueToArray1D(updFieldName, fldNames);
            updFieldValue=LPArray.addValueToArray1D(updFieldValue, fldValues);
        }
        
        Object[] diagnostic=Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SavedQueries.TBL.getName(), 
            updFieldName, updFieldValue);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnostic[0].toString())) return diagnostic; 
        String investIdStr=diagnostic[diagnostic.length-1].toString();
        //Object[] investigationAuditAdd = ProcedureInvestigationAudit.investigationAuditAdd(schemaPrefix, token, InvestigationAuditEvents.NEW_INVESTIGATION_CREATED.toString(), TblsData.SavedQueries.TBL.getName(), Integer.valueOf(investIdStr), investIdStr,  
        //        LPArray.joinTwo1DArraysInOneOf1DString(updFieldName, updFieldValue, ":"), null, null);
        return diagnostic;               
    }

}
