/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import databases.Rdbms;
import databases.TblsApp;
import databases.Token;
import functionaljavaa.incident.AppIncident;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class IncidentAPI extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken";
    
    
    public enum IncidentAPIEndpoints{
        /**
         *
         */
        NEW_INCIDENT("NEW_INCIDENT", "incidentTitle|incidentDetail", "", "incidentNewIncident_success",  
            new LPAPIArguments[]{ new LPAPIArguments(ParamsList.INCIDENT_TITLE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 6 ),
                new LPAPIArguments(ParamsList.INCIDENT_DETAIL.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7 ) }),
        CONFIRM_INCIDENT("CONFIRM_INCIDENT", "incidentId|note", "", "incidentConfirmIncident_success",  
            new LPAPIArguments[]{ new LPAPIArguments(ParamsList.INCIDENT_ID.getParamName(), LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6 ),
                new LPAPIArguments(ParamsList.NOTE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7)}),
        CLOSE_INCIDENT("CLOSE_INCIDENT", "incidentId|note", "", "incidentClosedIncident_success",  
            new LPAPIArguments[]{ new LPAPIArguments(ParamsList.INCIDENT_ID.getParamName(), LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6 ),
                new LPAPIArguments(ParamsList.NOTE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7)}),
        REOPEN_INCIDENT("REOPEN_INCIDENT", "incidentId|note", "", "incidentReopenIncident_success",  
            new LPAPIArguments[]{ new LPAPIArguments(ParamsList.INCIDENT_ID.getParamName(), LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6 ),
                new LPAPIArguments(ParamsList.NOTE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7)}),
        ADD_NOTE_INCIDENT("ADD_NOTE_INCIDENT", "incidentId|note", "", "incidentAddNoteToIncident_success",  
            new LPAPIArguments[]{ new LPAPIArguments(ParamsList.INCIDENT_ID.getParamName(), LPAPIArguments.ArgumentType.INTEGER.toString(), true, 6 ),
                new LPAPIArguments(ParamsList.NOTE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7),
            new LPAPIArguments(ParamsList.NEW_STATUS.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), false, 7)}),
        ;
        private IncidentAPIEndpoints(String name, String mandatoryParams, String optionalParams, String successMessageCode, LPAPIArguments[] argums){
            this.name=name;
            this.mandatoryParams=mandatoryParams;
            this.optionalParams=optionalParams;
            this.successMessageCode=successMessageCode;
            this.arguments=argums;
            
        } 
        public String getName(){
            return this.name;
        }
        public String getMandatoryParams(){
            return this.mandatoryParams;
        }
        public String getSuccessMessageCode(){
            return this.successMessageCode;
        }           
        private String[] getEndpointDefinition(){
            return new String[]{this.name, this.mandatoryParams, this.optionalParams, this.successMessageCode};
        }
       /**
         * @return the arguments
         */
        public LPAPIArguments[] getArguments() {
            return arguments;
        }     
        private final String name;
        private final String mandatoryParams; 
        private final String optionalParams; 
        private final String successMessageCode;       
        public  LPAPIArguments[] arguments;
    }
    
    public enum IncidentAPIfrontendEndpoints{
        USER_OPEN_INCIDENTS("USER_OPEN_INCIDENTS", "", "", ""),
        INCIDENT_DETAIL_FOR_GIVEN_INCIDENT("INCIDENT_DETAIL_FOR_GIVEN_INCIDENT", "incidentId", "", ""),
        ;
        private IncidentAPIfrontendEndpoints(String name, String mandatoryParams, String optionalParams, String successMessageCode){
            this.name=name;
            this.mandatoryParams=mandatoryParams;
            this.optionalParams=optionalParams;
            this.successMessageCode=successMessageCode;
        } 
        public String getName(){
            return this.name;
        }
        public String getMandatoryParams(){
            return this.mandatoryParams;
        }
        private String[] getEndpointDefinition(){
            return new String[]{this.name, this.mandatoryParams};
        }        
        private final String name;
        private final String mandatoryParams; 
        private final String optionalParams; 
        private final String successMessageCode;
    }

    public enum ParamsList{
        INCIDENT_ID("incidentId"),
        INCIDENT_TITLE("incidentTitle"),
        INCIDENT_DETAIL("incidentDetail"),
        NOTE("note"),
        NEW_STATUS("newStatus"),
        ;
        private ParamsList(String requestName){
            this.requestName=requestName;
        } 
        public String getParamName(){
            return this.requestName;
        }        
        private final String requestName;
    }
    
    //public static final String ENDPOINT_NEW_INCIDENT="INCIDENT_NEW";
    //public static final String MANDATORY_PARAMS_NEW_INCIDENT="incidentTitle|incidentDetail";
    
    //public static final String PARAMETER_PROGRAM_INCIDENT_TITLE="incidentTitle";
    //public static final String PARAMETER_PROGRAM_INCIDENT_DETAIL="incidentDetail";
    
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)            throws IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);        
        
        String language = LPFrontEnd.setLanguage(request); 
        String[] errObject = new String[]{"Servlet IncidentAPI at " + request.getServletPath()};   

        String[] mandatoryParams = new String[]{""};
        Object[] areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
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
        mandatoryParams = null;                        

/*        Object[] procActionRequiresUserConfirmation = LPPlatform.procActionRequiresUserConfirmation(schemaPrefix, actionName);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresUserConfirmation[0].toString())){     
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_USER_TO_CHECK);    
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_PSWD_TO_CHECK);    
        }
        Object[] procActionRequiresEsignConfirmation = LPPlatform.procActionRequiresEsignConfirmation(schemaPrefix, actionName);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresEsignConfirmation[0].toString())){                                                      
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_ESIGN_TO_CHECK);    
        }        
*/        
        if (mandatoryParams!=null){
            areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, mandatoryParams);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response, 
                       LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
               return;                   
            }     
        }
        
/*        if ( (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresUserConfirmation[0].toString())) &&     
             (!LPFrontEnd.servletUserToVerify(request, response, token.getUserName(), token.getUsrPw())) ){return;}

        if ( (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresEsignConfirmation[0].toString())) &&    
             (!LPFrontEnd.servletEsignToVerify(request, response, token.geteSign())) ){return;}        
*/        
        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;} 
        try (PrintWriter out = response.getWriter()) {

/*            Object[] actionEnabled = LPPlatform.procActionEnabled(schemaPrefix, token, actionName);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionEnabled[0].toString())){
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionEnabled);
                return ;                           
            }            
            actionEnabled = LPPlatform.procUserRoleActionEnabled(schemaPrefix, token.getUserRole(), actionName);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionEnabled[0].toString())){            
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionEnabled);
                return ;                           
            }            */
            IncidentAPIEndpoints endPoint = null;
            Object[] actionDiagnoses = null;
            try{
                endPoint = IncidentAPIEndpoints.valueOf(actionName.toUpperCase());
            }catch(Exception e){
                LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                return;                   
            }
            areMandatoryParamsInResponse = LPHttp.areEndPointMandatoryParamsInApiRequest(request, endPoint.getArguments());
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response,
                        LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);
                return;
            }                
            Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());  
            Integer incId=null;
/*            String incIdStr=request.getParameter(ParamsList.INCIDENT_ID.getParamName());
            if (incIdStr!=null && incIdStr.length()>0) incId=Integer.valueOf(incIdStr);
            String note=request.getParameter(ParamsList.NOTE.getParamName());
            String newStatus=request.getParameter(ParamsList.NEW_STATUS.getParamName());*/
            switch (endPoint){
                case NEW_INCIDENT:
                    actionDiagnoses = AppIncident.newIncident(token, argValues[0].toString(), argValues[1].toString(), "");
                    String incIdStr=actionDiagnoses[actionDiagnoses.length-1].toString();
                    if (incIdStr!=null && incIdStr.length()>0) incId=Integer.valueOf(incIdStr);
                    break;
                case CONFIRM_INCIDENT:
                    incId=(Integer) argValues[0];
                    AppIncident inc=new AppIncident(incId);
                    actionDiagnoses = inc.confirmIncident(token, incId, argValues[1].toString());
                    break;
                case ADD_NOTE_INCIDENT:
                    incId=(Integer) argValues[0];
                    inc=new AppIncident(incId);
                    String newNote=argValues[2].toString();
                    actionDiagnoses = inc.addNoteIncident(token, incId, argValues[1].toString(), newNote);
                    break;                    
                case CLOSE_INCIDENT:
                    incId=(Integer) argValues[0];
                    inc=new AppIncident(incId);
                    actionDiagnoses = inc.closeIncident(token, incId, argValues[1].toString());
                    break;                    
                case REOPEN_INCIDENT:
                    incId=(Integer) argValues[0];
                    inc=new AppIncident(incId);
                    actionDiagnoses = inc.reopenIncident(token, incId, argValues[1].toString());
                    break;                    
            }    
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionDiagnoses[0].toString())){  
//                Rdbms.rollbackWithSavePoint();
//                if (!con.getAutoCommit()){
//                    con.rollback();
//                    con.setAutoCommit(true);}                
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionDiagnoses);   
            }else{
                //actionDiagnoses[0]=firstN;
                RelatedObjects rObj=RelatedObjects.getInstance();
                rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsApp.Incident.TBL.getName(), "incident", incId);                
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), new Object[]{incId}, rObj.getRelatedObject());
                rObj.killInstance();
                LPFrontEnd.servletReturnSuccess(request, response, dataSampleJSONMsg);
            }           
        }catch(Exception e){   
 /*           try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(sampleAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
*/            
            Rdbms.closeRdbms();                   
            errObject = new String[]{e.getMessage()};
            Object[] errMsg = LPFrontEnd.responseError(errObject, language, null);
            response.sendError((int) errMsg[0], (String) errMsg[1]);           
        } finally {
            // release database resources
            try {                
                //con.close();
                Rdbms.closeRdbms();   
            } catch (Exception ignore) {
            }
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
        } catch (IOException ex) {
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
        } catch (IOException ex) {
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

}
