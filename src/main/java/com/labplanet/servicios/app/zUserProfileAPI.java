/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPPlatform;
import databases.Rdbms;
import databases.TblsApp;
import databases.TblsApp.Users;
import databases.Token;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class zUserProfileAPI extends HttpServlet {
    
    public enum UserProfileAPIEndpoints{
        /**
         *
         */
        UPDATE_ESIGN("UPDATE_ESIGN", "incidentTitle|incidentDetail", "", "incidentNewIncident_success"),
        ;
        private UserProfileAPIEndpoints(String name, String mandatoryParams, String optionalParams, String successMessageCode){
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
        public String getSuccessMessageCode(){
            return this.successMessageCode;
        }           
        private String[] getEndpointDefinition(){
            return new String[]{this.name, this.mandatoryParams, this.optionalParams, this.successMessageCode};
        }
     
        private final String name;
        private final String mandatoryParams; 
        private final String optionalParams; 
        private final String successMessageCode;       
    }
    /**
     *
     */
    public static final String MANDATORY_PARAMS_MAIN_SERVLET = GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME+"|"+GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);
        String language = LPFrontEnd.setLanguage(request);         
        try (PrintWriter out = response.getWriter()) {
            Object[] areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                 LPFrontEnd.servletReturnResponseError(request, response, 
                         LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                 return;          
             }               
            String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
            String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);            
                           
            Token token = new Token(finalToken);
            String[] mandatoryParams = null;
           Object[] procActionRequiresUserConfirmation = LPPlatform.procActionRequiresUserConfirmation(LPPlatform.SCHEMA_APP, actionName);
           if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresUserConfirmation[0].toString())){     
               mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_USER_TO_CHECK);    
               mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_PSWD_TO_CHECK);    
           }
           Object[] procActionRequiresEsignConfirmation = LPPlatform.procActionRequiresEsignConfirmation(LPPlatform.SCHEMA_APP, actionName);
           if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresEsignConfirmation[0].toString())){                                                      
               mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_ESIGN_TO_CHECK);    
           }        
           if (mandatoryParams!=null){
               areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, mandatoryParams);
               if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                   LPFrontEnd.servletReturnResponseError(request, response, 
                           LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                   return;                  
               }     
           }
           if ( (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresUserConfirmation[0].toString())) &&     
                (!LPFrontEnd.servletUserToVerify(request, response, token.getUserName(), token.getUsrPw())) ){return;}

           if ( (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresEsignConfirmation[0].toString())) &&    
                (!LPFrontEnd.servletEsignToVerify(request, response, token.geteSign())) ){return;}

           if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}  
//        Connection con = Rdbms.createTransactionWithSavePoint();     
           
 /*       if (con==null){
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The Transaction cannot be created, the action should be aborted");
             return;
        }
        
        try {
            con.rollback();
            con.setAutoCommit(true);    
        } catch (SQLException ex) {
            Logger.getLogger(EnvMonAPI.class.getName()).log(Level.SEVERE, null, ex);
        }        
*/           
            UserProfileAPIEndpoints endPoint = null;
            try{
                endPoint = UserProfileAPIEndpoints.valueOf(actionName.toUpperCase());
            }catch(Exception e){
                LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                return;                   
            }
            areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, endPoint.getMandatoryParams().split("\\|"));
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response,
                        LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);
                return;
            } 
            Object[] userActionDiagnostic = new Object[]{LPPlatform.LAB_FALSE};
            Object[] messageDynamicData=new Object[]{};
            RelatedObjects rObj=RelatedObjects.getInstance();
            switch (endPoint){
                case UPDATE_ESIGN: 
                    areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, new String[]{"newEsignPhrase"});
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                    
                    String newEsignPhrase = request.getParameter("newEsignPhrase"); 
                    userActionDiagnostic = Rdbms.updateRecordFieldsByFilter(LPPlatform.SCHEMA_APP, Users.TBL.getName(), 
                            new String[]{Users.FLD_ESIGN.getName()}, new Object[]{newEsignPhrase}, new String[]{Users.FLD_USER_NAME.getName()}, new Object[]{token.getUserName()});
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsApp.Users.TBL.getName(), TblsApp.Users.TBL.getName(), token.getUserName());
                    messageDynamicData=new Object[]{token.getUserName()};
                    break;
                default:
                    LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    return;                                        
            }
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(userActionDiagnostic[0].toString())){  
/*                Rdbms.rollbackWithSavePoint();
                if (!con.getAutoCommit()){
                    con.rollback();
                    con.setAutoCommit(true);}                */
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, userActionDiagnostic);   
            }else{
                rObj.killInstance();
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), messageDynamicData, rObj.getRelatedObject());
                LPFrontEnd.servletReturnSuccess(request, response, dataSampleJSONMsg); 
                
            }                             
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            processRequest(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(zUserProfileAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            processRequest(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(zUserProfileAPI.class.getName()).log(Level.SEVERE, null, ex);
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
