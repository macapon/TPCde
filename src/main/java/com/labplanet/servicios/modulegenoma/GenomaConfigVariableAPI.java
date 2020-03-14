/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulegenoma;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.modulegenoma.GenomaConfigVariables;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class GenomaConfigVariableAPI extends HttpServlet {

    public enum  GenomaVariableAPIParamsList{
        variableSetName, variableName, fieldsValues, userName, userRole}
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken|schemaPrefix";
            
    public enum  GenomaVariableAPIEndPoints{
//          PROJECT_NEW("PROJECT_NEW", "projectName"), PROJECT_UPDATE("PROJECT_UPDATE", "projectName|fieldsNames|fieldsValues"),
//          PROJECT_ACTIVATE("PROJECT_ACTIVATE", "projectName"), PROJECT_DEACTIVATE("PROJECT_DEACTIVATE", "projectName"),
          VARIABLE_SET_ADD_VARIABLE("VARIABLE_SET_ADD_VARIABLE", "variableSetName|variableName"), VARIABLE_SET_REMOVE_VARIABLE("VARIABLE_SET_REMOVE_VARIABLE", "variableSetName|variableName"),
//          PROJECT_CHANGE_USER_ROLE("PROJECT_CHANGE_USER_ROLE", "projectName|userName|userRole"), PROJECT_USER_ACTIVATE("PROJECT_USER_ACTIVATE", "projectName|userName|userRole"),
//          PROJECT_USER_DEACTIVATE("PROJECT_USER_DEACTIVATE", "projectName|userName|userRole"),
          ;
        private GenomaVariableAPIEndPoints(String name, String mandatoryFields){
            this.endPointName=name;
            this.endPointMandatoryFields=mandatoryFields;
        }
        public String getName(){
            return this.endPointName;
        }
        public String getMandatoryFields(){
            return this.endPointMandatoryFields;
        }
      String endPointName="";
      String endPointMandatoryFields="";
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);

        String language = LPFrontEnd.setLanguage(request); 
        String[] errObject = new String[]{"Servlet Genoma VariableAPI at " + request.getServletPath()};   

        String[] mandatoryParams = new String[]{""};
        Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            LPFrontEnd.servletReturnResponseError(request, response, 
                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
            return;          
        }             
        String schemaPrefix = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX);            
        String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
        String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);                   
        
        Token token = new Token(finalToken);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(token.getUserName())){
                LPFrontEnd.servletReturnResponseError(request, response, 
                        LPPlatform.API_ERRORTRAPING_INVALID_TOKEN, null, language);              
                return;                             
        }
        mandatoryParams = null;                        

        Object[] procActionRequiresUserConfirmation = LPPlatform.procActionRequiresUserConfirmation(schemaPrefix, actionName);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresUserConfirmation[0].toString())){     
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_USER_TO_CHECK);    
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_PSWD_TO_CHECK);    
        }
        Object[] procActionRequiresEsignConfirmation = LPPlatform.procActionRequiresEsignConfirmation(schemaPrefix, actionName);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(procActionRequiresEsignConfirmation[0].toString())){                                                      
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_ESIGN_TO_CHECK);    
        }        
        if (mandatoryParams!=null){
            areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, mandatoryParams);
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
*/        
/*        try {
            con.rollback();
            con.setAutoCommit(true);    
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
*/                    
/*        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(sampleAPI.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        String schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG);    
        Rdbms.setTransactionId(schemaConfigName);
        //ResponseEntity<String121> responsew;        
        try (PrintWriter out = response.getWriter()) {

            Object[] actionEnabled = LPPlatform.procActionEnabled(schemaPrefix, token, actionName);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionEnabled[0].toString())){
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionEnabled);
                return ;               
            }            
            actionEnabled = LPPlatform.procUserRoleActionEnabled(schemaPrefix, token.getUserRole(), actionName);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionEnabled[0].toString())){       
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionEnabled);
                return ;                           
            }            
            
            Object[] dataSample = null;
            
            switch (actionName.toUpperCase()){
/*                case "PROJECT_NEW":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaProjectAPIEndPoints.PROJECT_NEW.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    String fieldName=request.getParameter(GenomaProjectAPIParamsList.fieldsNames.toString());                                        
                    String fieldValue=request.getParameter(GenomaProjectAPIParamsList.fieldsValues.toString());                    
                    String[] fieldNames=null;
                    Object[] fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    String projectName=request.getParameter(GenomaProjectAPIParamsList.projectName.toString());
                    if (projectName.length()==0)
                        projectName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.Project.FLD_NAME.getName())].toString();

                    dataSample =cnfVar.createProject(schemaPrefix, token, projectName, fieldNames, fieldValues,  false);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "PROJECT_UPDATE":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaProjectAPIEndPoints.PROJECT_UPDATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaProjectAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaProjectAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    projectName=request.getParameter(GenomaProjectAPIParamsList.projectName.toString());
                    if (projectName.length()==0)
                        projectName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.Project.FLD_NAME.getName())].toString();

                    dataSample =cnfVar.projectUpdate(schemaPrefix, token, projectName, fieldNames, fieldValues);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "PROJECT_ACTIVATE":
                case "PROJECT_DEACTIVATE":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaProjectAPIEndPoints.PROJECT_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    projectName=request.getParameter(GenomaProjectAPIParamsList.projectName.toString());
                    if ("PROJECT_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =cnfVar.projectActivate(schemaPrefix, token, projectName);
                    else if ("PROJECT_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =cnfVar.projectDeActivate(schemaPrefix, token, projectName);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;
*/                
                case "VARIABLE_SET_ADD_VARIABLE":
                case "VARIABLE_SET_REMOVE_VARIABLE":     
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaVariableAPIEndPoints.VARIABLE_SET_ADD_VARIABLE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    String variableSetName=request.getParameter(GenomaVariableAPIParamsList.variableSetName.toString());
                    String variableName=request.getParameter(GenomaVariableAPIParamsList.variableName.toString());
                    if ("VARIABLE_SET_ADD_VARIABLE".equalsIgnoreCase(actionName))
                        dataSample =GenomaConfigVariables.variableSetAddVariable(schemaPrefix, token, variableSetName, variableName);
                    else if ("VARIABLE_SET_REMOVE_VARIABLE".equalsIgnoreCase(actionName))
                        dataSample =GenomaConfigVariables.variableSetRemoveVariable(schemaPrefix, token, variableSetName, variableName);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;       
                default:      
                    LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    return;                    
            }    
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(dataSample[0].toString())){  
/*                Rdbms.rollbackWithSavePoint();
                if (!con.getAutoCommit()){
                    con.rollback();
                    con.setAutoCommit(true);}                */
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, dataSample);   
            }else{
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(dataSample);
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
            response.setStatus(401);
            Rdbms.closeRdbms();                   
            errObject = new String[]{e.getMessage()};
            Object[] errMsg = LPFrontEnd.responseError(errObject, language, null);
            response.sendError((int) errMsg[0], (String) errMsg[1]);           
        } finally {
            // release database resources
            try {
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

}
