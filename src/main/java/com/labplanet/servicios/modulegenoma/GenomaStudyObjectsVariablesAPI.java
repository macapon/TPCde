/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulegenoma;

import com.labplanet.servicios.app.GlobalAPIsParams;
import com.labplanet.servicios.modulegenoma.GenomaProjectAPI.GenomaProjectAPIParamsList;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.audit.AuditAndUserValidation;
import functionaljavaa.moduleenvironmentalmonitoring.DataStudyObjectsVariableValues;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import static functionaljavaa.testingscripts.LPTestingOutFormat.getAttributeValue;
import java.io.IOException;
import java.io.PrintWriter;
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
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class GenomaStudyObjectsVariablesAPI extends HttpServlet {

    public static final String MANDATORY_PARAMS_MAIN_SERVLET=GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME+"|"+GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN+"|"+GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX;

    public enum GenomaStudyObjectsVariablesAPIEndPoints{ 
        ADD_VARIABLE_SET_TO_STUDY_OBJECT("ADD_VARIABLE_SET_TO_STUDY_OBJECT", "variablesSetAdded_success", 
                new LPAPIArguments[]{new LPAPIArguments(GenomaProjectAPIParamsList.STUDY_NAME.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 6),
                new LPAPIArguments(GenomaProjectAPIParamsList.VARIABLE_SET_NAME.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7),
                new LPAPIArguments(GenomaProjectAPIParamsList.OWNER_TABLE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 8),
                new LPAPIArguments(GenomaProjectAPIParamsList.OWNER_ID.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 9)}),
        STUDY_OBJECT_SET_VARIABLE_VALUE("STUDY_OBJECT_SET_VARIABLE_VALUE", "variableValueEntered_success", 
                new LPAPIArguments[]{new LPAPIArguments(GenomaProjectAPIParamsList.STUDY_NAME.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 6),
                new LPAPIArguments(GenomaProjectAPIParamsList.VARIABLE_SET_NAME.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 7),
                new LPAPIArguments(GenomaProjectAPIParamsList.OWNER_TABLE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 8),
                new LPAPIArguments(GenomaProjectAPIParamsList.OWNER_ID.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 9),
                new LPAPIArguments(GenomaProjectAPIParamsList.VARIABLE_NAME.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 10),
                new LPAPIArguments(GenomaProjectAPIParamsList.NEW_VALUE.getParamName(), LPAPIArguments.ArgumentType.STRING.toString(), true, 11)}),
        ;
        private GenomaStudyObjectsVariablesAPIEndPoints(String name, String successMessageCode, LPAPIArguments[] argums){
            this.name=name;
            this.successMessageCode=successMessageCode;
            this.arguments=argums;  
        } 
        public HashMap<HttpServletRequest, Object[]> testingSetAttributesAndBuildArgsArray(HttpServletRequest request, Object[][] contentLine, Integer lineIndex){  
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);

        String language = LPFrontEnd.setLanguage(request); 
        String[] errObject = new String[]{"Servlet Genoma StudyObjectsVariablesAPI at " + request.getServletPath()};   

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

        if (mandatoryParams!=null){
            areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, mandatoryParams);
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response, 
                       LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
               return;                   
            }     
        }        
        AuditAndUserValidation auditAndUsrValid=AuditAndUserValidation.getInstance(request, response, language);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(auditAndUsrValid.getCheckUserValidationPassesDiag()[0].toString())){
            LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, auditAndUsrValid.getCheckUserValidationPassesDiag());              
            return;          
        }           
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
            
            Object[] diagnostic = null;
            GenomaStudyObjectsVariablesAPIEndPoints endPoint = null;
    //        Object[] actionDiagnoses = null;
            try{
                endPoint = GenomaStudyObjectsVariablesAPIEndPoints.valueOf(actionName.toUpperCase());
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
            Object[] messageDynamicData=new Object[]{};
            RelatedObjects relatedObject=RelatedObjects.getInstance();
            switch (endPoint){
                case ADD_VARIABLE_SET_TO_STUDY_OBJECT:     
                    String variableSetName=request.getParameter(GenomaProjectAPIParamsList.VARIABLE_SET_NAME.getParamName());
                    String studyName=request.getParameter(GenomaProjectAPIParamsList.STUDY_NAME.getParamName());
                    String ownerTable=request.getParameter(GenomaProjectAPIParamsList.OWNER_TABLE.getParamName());
                    String ownerId=request.getParameter(GenomaProjectAPIParamsList.OWNER_ID.getParamName());
                    diagnostic =DataStudyObjectsVariableValues.addVariableSetToObject(schemaPrefix, token, studyName, variableSetName, ownerTable, ownerId);
                    messageDynamicData=LPArray.addValueToArray1D(messageDynamicData, new Object[]{variableSetName, ownerTable, ownerId});
                    relatedObject.addSimpleNode(schemaPrefix, TblsGenomaConfig.VariablesSet.TBL.getName(),  TblsGenomaConfig.VariablesSet.TBL.getName(), variableSetName);
                    relatedObject.addSimpleNode(schemaPrefix, ownerTable, ownerTable, ownerId);
                    break;                      
                case STUDY_OBJECT_SET_VARIABLE_VALUE:     
                    variableSetName=request.getParameter(GenomaProjectAPIParamsList.VARIABLE_SET_NAME.getParamName());
                    studyName=request.getParameter(GenomaProjectAPIParamsList.STUDY_NAME.getParamName());
                    ownerTable=request.getParameter(GenomaProjectAPIParamsList.OWNER_TABLE.getParamName());
                    ownerId=request.getParameter(GenomaProjectAPIParamsList.OWNER_ID.getParamName());
                    String variableName=request.getParameter(GenomaProjectAPIParamsList.VARIABLE_NAME.getParamName());
                    String newValue=request.getParameter(GenomaProjectAPIParamsList.NEW_VALUE.getParamName());
                    diagnostic =DataStudyObjectsVariableValues.objectVariableSetValue(schemaPrefix, token, studyName, ownerTable, ownerId, variableSetName, variableName, newValue);
                    messageDynamicData=LPArray.addValueToArray1D(messageDynamicData, new Object[]{newValue, variableName});
                    relatedObject.addSimpleNode(schemaPrefix, TblsGenomaData.StudyVariableValues.TBL.getName(), TblsGenomaData.StudyVariableValues.TBL.getName(), variableName);
                    relatedObject.addSimpleNode(schemaPrefix, ownerTable, ownerTable, ownerId);
                    break;  
                default:      
                    LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    return;                    
            }    
            
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagnostic[0].toString())){  
/*                Rdbms.rollbackWithSavePoint();
                if (!con.getAutoCommit()){
                    con.rollback();
                    con.setAutoCommit(true);}                */
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnosticBilingue(request, response, diagnostic[4].toString(), messageDynamicData);   
                relatedObject.killInstance();
            }else{
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), messageDynamicData, relatedObject.getRelatedObject());                
                relatedObject.killInstance();
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
            auditAndUsrValid.killInstance();
            errObject = new String[]{e.getMessage()};
            Object[] errMsg = LPFrontEnd.responseError(errObject, language, null);
            response.sendError((int) errMsg[0], (String) errMsg[1]);           
        } finally {
            
            // release database resources
            try {
                Rdbms.closeRdbms();
                auditAndUsrValid.killInstance();
            } catch (Exception ex) {Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
