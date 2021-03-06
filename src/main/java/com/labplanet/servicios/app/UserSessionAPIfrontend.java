/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;



import static com.labplanet.servicios.app.InvestigationAPI.MANDATORY_PARAMS_MAIN_SERVLET;
import databases.Rdbms;
import databases.TblsApp;
import databases.TblsDataAudit;
import databases.Token;
import static functionaljavaa.audit.AuditUtilities.getProcAuditTablesList;
import static functionaljavaa.audit.AuditUtilities.getUserSessionProceduresList;
import static functionaljavaa.audit.AuditUtilities.userSessionExistAtProcLevel;
import static functionaljavaa.testingscripts.LPTestingOutFormat.getAttributeValue;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPJson;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class UserSessionAPIfrontend extends HttpServlet {
    public enum UserSessionAPIfrontendEndpoints{
        /**
         *
         */
        USER_SESSIONS("OPEN_INVESTIGATIONS", "",new LPAPIArguments[]{new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_USER_SESSION_ID, LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6),}),
        USER_SESSION_AUDIT_HISTORY("INVESTIGATION_RESULTS_PENDING_DECISION", "",new LPAPIArguments[]{new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_USER_SESSION_ID, LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6),}),
        ;
        private UserSessionAPIfrontendEndpoints(String name, String successMessageCode, LPAPIArguments[] argums){
            this.name=name;
            this.successMessageCode=successMessageCode;
            this.arguments=argums;  
        } 
        public  HashMap<HttpServletRequest, Object[]> testingSetAttributesAndBuildArgsArray(HttpServletRequest request, Object[][] contentLine, Integer lineIndex){  
            HashMap<HttpServletRequest, Object[]> hm = new HashMap();
            Object[] argValues=new Object[0];
            for (LPAPIArguments curArg: this.arguments){                
                argValues=LPArray.addValueToArray1D(argValues, curArg.getName()+":"+getAttributeValue(contentLine[lineIndex][curArg.getTestingArgPosic()], contentLine));
                request.setAttribute(curArg.getName(), getAttributeValue(contentLine[lineIndex][curArg.getTestingArgPosic()], contentLine));
            }  
            hm.put(request, argValues);            
            return hm;
        }        
        public String getName(){
            return this.name;
        }
        public String getSuccessMessageCode(){
            return this.successMessageCode;
        }           

        /**
         * @return the arguments
         */
        public LPAPIArguments[] getArguments() {
            return arguments;
        }     
        private final String name;
        private final String successMessageCode;  
        private final LPAPIArguments[] arguments;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)            throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);

        String language = LPFrontEnd.setLanguage(request); 

        Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            LPFrontEnd.servletReturnResponseError(request, response, 
                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
            return;          
        }             
        String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
        String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);                   
        
        Token token = new Token(finalToken);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(token.getUserName())){
                LPFrontEnd.servletReturnResponseError(request, response, 
                        LPPlatform.API_ERRORTRAPING_INVALID_TOKEN, null, language);              
                return;                             
        }
        UserSessionAPIfrontendEndpoints endPoint = null;
        try{
            endPoint = UserSessionAPIfrontendEndpoints.valueOf(actionName.toUpperCase());
        }catch(Exception e){
            LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
            return;                   
        }
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());   
        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}          

        switch (endPoint){
            case USER_SESSION_AUDIT_HISTORY:
            case USER_SESSIONS:              
                String[] fieldsToRetrieve=TblsApp.AppSession.getAllFieldNames();
                Object[][] incidentsNotClosed=Rdbms.getRecordFieldsByFilter(LPPlatform.SCHEMA_APP,TblsApp.AppSession.TBL.getName(), 
                        new String[]{TblsApp.AppSession.FLD_SESSION_ID.getName()}, 
                        new Object[]{argValues[0]}, 
                        fieldsToRetrieve, new String[]{TblsApp.AppSession.FLD_SESSION_ID.getName()+" desc"});
                JSONArray userSessionArr = new JSONArray();
                if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(incidentsNotClosed[0][0].toString())){
                    JSONArray procAuditArr = new JSONArray();
                    for (Object[] currUsrSession: incidentsNotClosed){
                        Integer sessionId=-1;
                        if (LPArray.valueInArray(fieldsToRetrieve, TblsApp.AppSession.FLD_SESSION_ID.getName()))
                            sessionId=(Integer) currUsrSession[LPArray.valuePosicInArray(fieldsToRetrieve, TblsApp.AppSession.FLD_SESSION_ID.getName())];
                        JSONObject userSessionObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currUsrSession);
                        String[] userSessionProceduresList = getUserSessionProceduresList(fieldsToRetrieve, currUsrSession);
                        for (String curProc: userSessionProceduresList){
                            JSONObject procAuditJson = new JSONObject();
                            procAuditJson.put("procedure", curProc);
                            if (!userSessionExistAtProcLevel(curProc, sessionId)){
                                procAuditJson.put("proc_audit_records", "No actions performed during this session on this procedure");
                            }else{
                                Object[] procAuditTablesList = getProcAuditTablesList(LPPlatform.buildSchemaName(curProc.replace(String.valueOf((char)34), ""), LPPlatform.SCHEMA_DATA_AUDIT));
                                JSONObject procAuditTableJson = new JSONObject();
                                if (LPPlatform.LAB_FALSE.equalsIgnoreCase(procAuditTablesList[0].toString()))
                                    procAuditJson.put("proc_audit_records", curProc+". ERROR. No tables in audit schema");
                                else{
                                    String[] procAuditTablesFieldsToRetrieve=new String[]{TblsDataAudit.Sample.FLD_APP_SESSION_ID.getName(), TblsDataAudit.Sample.FLD_AUDIT_ID.getName(), TblsDataAudit.Sample.FLD_ACTION_NAME.getName()};
                                    for (Object curTable: procAuditTablesList){
                                        Object[][] dataAuditCurTableInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(curProc, LPPlatform.SCHEMA_DATA_AUDIT), curTable.toString(), 
                                            new String[]{TblsDataAudit.Sample.FLD_APP_SESSION_ID.getName()}, new Object[]{sessionId}, 
                                            procAuditTablesFieldsToRetrieve, 
                                            new String[]{TblsDataAudit.Sample.FLD_AUDIT_ID.getName()});
                                        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(dataAuditCurTableInfo[0][0].toString())){
                                            JSONArray procAuditCurTableArr = new JSONArray();
                                            JSONArray auditCurTableArr = new JSONArray();
                                            JSONObject procAuditCurTableJson = new JSONObject();
                                            for (Object[] curTblAuditRec: dataAuditCurTableInfo){  
                                                JSONObject procAuditTablesJson=LPJson.convertArrayRowToJSONObject(procAuditTablesFieldsToRetrieve, curTblAuditRec);
                                                auditCurTableArr.add(procAuditTablesJson);
                                            }
                                            procAuditCurTableJson.put("audit_records", auditCurTableArr);
                                            procAuditCurTableJson.put("table", curTable);
                                            procAuditCurTableArr.add(procAuditCurTableJson);
                                            procAuditJson.put("proc_audit_records", procAuditCurTableArr);
                                        }
                                    }
                                }    
                            }
                            procAuditArr.add(procAuditJson);
                        }
                        userSessionObj.put("audit_actions", procAuditArr);
/*                        
                        Integer investFldPosic=LPArray.valuePosicInArray(fieldsToRetrieve, TblsApp.AppSession.FLD_SESSION_ID.getName());
                        if (investFldPosic>-1){
                            Integer investigationId=Integer.valueOf(currInvestigation[investFldPosic].toString());
                            fieldsToRetrieve=TblsProcedure.InvestObjects.getAllFieldNames();
                            incidentsNotClosed=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE),TblsProcedure.InvestObjects.TBL.getName(), 
                                    new String[]{TblsProcedure.InvestObjects.FLD_INVEST_ID.getName()}, 
                                    new Object[]{investigationId}, 
                                    fieldsToRetrieve, new String[]{TblsProcedure.InvestObjects.FLD_ID.getName()});
                            JSONArray investObjectsJArr = new JSONArray();
                            if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(incidentsNotClosed[0][0].toString())){
                                for (Object[] currInvestObject: incidentsNotClosed){
                                    JSONObject investObjectsJObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currInvestObject);
                                    investObjectsJArr.add(investObjectsJObj);
                                }
                            }                        
                            userSessionJObj.put(TblsProcedure.InvestObjects.TBL.getName(), investObjectsJArr);
                        }
*/
                        userSessionArr.add(userSessionObj);
                    }
                }
                Rdbms.closeRdbms();  
                LPFrontEnd.servletReturnSuccess(request, response, userSessionArr);
                return;  
/*            case USER_SESSION_AUDIT_HISTORY:
                String statusClosed=Parameter.getParameterBundle(schemaPrefix+"-"+LPPlatform.SCHEMA_DATA, "programCorrectiveAction_statusClosed");
                JSONArray jArray = new JSONArray(); 
                if (!isProgramCorrectiveActionEnable(schemaPrefix)){
                  JSONObject jObj=new JSONObject();
                  jArray.add(jObj.put(TblsProcedure.ProgramCorrectiveAction.TBL.getName(), "program corrective action not active!"));
                }
                else{
                  Object[][] investigationResultsPendingDecision = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE), TblsProcedure.ProgramCorrectiveAction.TBL.getName(), 
                          new String[]{TblsProcedure.ProgramCorrectiveAction.FLD_STATUS.getName()+"<>"}, 
                          new String[]{statusClosed}, 
                          TblsProcedure.ProgramCorrectiveAction.getAllFieldNames(), new String[]{TblsProcedure.ProgramCorrectiveAction.FLD_PROGRAM_NAME.getName()});
                  if (LPPlatform.LAB_FALSE.equalsIgnoreCase(investigationResultsPendingDecision[0][0].toString()))LPFrontEnd.servletReturnSuccess(request, response, new JSONArray());


                  for (Object[] curRow: investigationResultsPendingDecision){
                    JSONObject jObj=LPJson.convertArrayRowToJSONObject(TblsProcedure.ProgramCorrectiveAction.getAllFieldNames(), curRow);
                    jArray.add(jObj);
                  }
                }
                Rdbms.closeRdbms();                    
                LPFrontEnd.servletReturnSuccess(request, response, jArray);
                break;                
            case INVESTIGATION_DETAIL_FOR_GIVEN_INVESTIGATION:
                Integer investigationId=null;
                String investigationIdStr=LPNulls.replaceNull(argValues[0]).toString();
                if (investigationIdStr!=null && investigationIdStr.length()>0) investigationId=Integer.valueOf(investigationIdStr);

                fieldsToRetrieve=TblsApp.AppSession.getAllFieldNames();
                incidentsNotClosed=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE),TblsApp.AppSession.TBL.getName(), 
                        new String[]{TblsApp.AppSession.FLD_ID.getName()}, 
                        new Object[]{investigationId}, 
                        fieldsToRetrieve, new String[]{TblsApp.AppSession.FLD_ID.getName()+" desc"});
                investigationJArr = new JSONArray();
                if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(incidentsNotClosed[0][0].toString())){
                    for (Object[] currInvestigation: incidentsNotClosed){
                        JSONObject investigationJObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currInvestigation);
                        
                        fieldsToRetrieve=TblsProcedure.InvestObjects.getAllFieldNames();
                        incidentsNotClosed=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE),TblsProcedure.InvestObjects.TBL.getName(), 
                                new String[]{TblsProcedure.InvestObjects.FLD_INVEST_ID.getName()}, 
                                new Object[]{investigationId}, 
                                fieldsToRetrieve, new String[]{TblsProcedure.InvestObjects.FLD_ID.getName()});
                        JSONArray investObjectsJArr = new JSONArray();
                        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(incidentsNotClosed[0][0].toString())){
                            for (Object[] currInvestObject: incidentsNotClosed){
                                JSONObject investObjectsJObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currInvestObject);
                                investObjectsJArr.add(investObjectsJObj);
                            }
                        }
                        investigationJObj.put(TblsProcedure.InvestObjects.TBL.getName(), investObjectsJArr);
                        investigationJArr.add(investigationJObj);
                    }
                }
                Rdbms.closeRdbms();  
                LPFrontEnd.servletReturnSuccess(request, response, investigationJArr);
                return;*/
        default: 
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException | ServletException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException | ServletException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */  
}
