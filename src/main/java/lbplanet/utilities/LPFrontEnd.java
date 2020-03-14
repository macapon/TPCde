/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbplanet.utilities;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import functionaljavaa.parameter.Parameter;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class LPFrontEnd {

    public enum ResponseTags{
        DIAGNOTIC("diagnostic"), CATEGORY("category"), MESSAGE("message"), RELATED_OBJECTS("related_objects"), IS_ERROR("is_error");
        private ResponseTags(String labelName){
            this.labelName=labelName;            
        }    
        public String getLabelName(){
            return this.labelName;
        }           
        private final String labelName;
    }

    private LPFrontEnd(){    throw new IllegalStateException("Utility class");}    

    /**
     *
     * @param request
     * @return
     */
    public static String setLanguage(HttpServletRequest request){
        String language = request.getParameter(LPPlatform.REQUEST_PARAM_LANGUAGE);
        if (language == null){language = LPPlatform.REQUEST_PARAM_LANGUAGE_DEFAULT_VALUE;}
        return language;
    }
    
    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static final Boolean servletStablishDBConection(HttpServletRequest request, HttpServletResponse response){
        
        boolean isConnected = false;                               
        isConnected = Rdbms.getRdbms().startRdbms(LPTestingOutFormat.TESTING_USER, LPTestingOutFormat.TESTING_PW);      
        if (!isConnected){      
            LPFrontEnd.servletReturnResponseError(request, response, 
                    LPPlatform.API_ERRORTRAPING_PROPERTY_DATABASE_NOT_CONNECTED, null, null);                                                                
        }  
        return isConnected;
    }

    /**
     *
     * @param request
     * @param response
     * @param dbUserName
     * @param dbUserPassword
     * @return
     */
    public static final Boolean servletUserToVerify(HttpServletRequest request, HttpServletResponse response, String dbUserName, String dbUserPassword){    
        String userToVerify = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_USER_TO_CHECK);                   
        String passwordToVerify = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_PSWD_TO_CHECK);    
        if ( (!userToVerify.equalsIgnoreCase(dbUserName)) || (!passwordToVerify.equalsIgnoreCase(dbUserPassword)) ){
            servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_INVALID_USER_VERIFICATION, null, null);           
            return false;                                
        }            
        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param eSign
     * @return
     */
    public static final Boolean servletEsignToVerify(HttpServletRequest request, HttpServletResponse response, String eSign){    
        String eSignToVerify = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ESIGN_TO_CHECK);                   
        if (!eSignToVerify.equalsIgnoreCase(eSign)) {  
            servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_INVALID_ESIGN, null, null);           
            return false;                                
        }            
        return true;
    }
    /**
     *
     * @param errorStructure
     * @return
     */
    public static Object[] responseError(Object[] errorStructure){
        Object[] responseObj = new Object[0];
        responseObj = LPArray.addValueToArray1D(responseObj, HttpServletResponse.SC_UNAUTHORIZED);
        responseObj = LPArray.addValueToArray1D(responseObj, errorStructure[errorStructure.length-1].toString());        
        return responseObj;
    }

    /**
     *
     * @param lpFalseStructure
     * @return
     */
    public static JSONObject responseJSONDiagnosticLPFalse(Object[] lpFalseStructure){
        JSONObject errJsObj = new JSONObject();
        errJsObj.put(ResponseTags.DIAGNOTIC.getLabelName(), lpFalseStructure[0]);
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_es", lpFalseStructure[lpFalseStructure.length-1]);
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_en", lpFalseStructure[lpFalseStructure.length-1]);
        errJsObj.put(ResponseTags.IS_ERROR.getLabelName(), true);
        return errJsObj;
    }

    /**
     *
     * @param lpTrueStructure
     * @return
     */
    public static JSONObject responseJSONDiagnosticLPTrue(Object[] lpTrueStructure){
        JSONObject errJsObj = new JSONObject();
        errJsObj.put(ResponseTags.DIAGNOTIC.getLabelName(), lpTrueStructure[0]);
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_es", lpTrueStructure[lpTrueStructure.length-1]);
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_en", lpTrueStructure[lpTrueStructure.length-1]);
        errJsObj.put(ResponseTags.IS_ERROR.getLabelName(), false);
        return errJsObj;
    }    

    /**
     *
     * @param apiName
     * @param msgCode
     * @param msgDynamicValues
     * @return
     */
    public static JSONObject responseJSONDiagnosticLPTrue(String apiName, String msgCode, Object[] msgDynamicValues, JSONArray relatedObjects){
        String errorTextEn = Parameter.getParameterBundle(LPPlatform.CONFIG_FILES_FOLDER, LPPlatform.CONFIG_FILES_API_SUCCESSMESSAGE+apiName, null, msgCode, "en");
        String errorTextEs = Parameter.getParameterBundle(LPPlatform.CONFIG_FILES_FOLDER, LPPlatform.CONFIG_FILES_API_SUCCESSMESSAGE+apiName, null, msgCode, "es");
        if (msgCode!=null){
            for (int iVarValue=1; iVarValue<=msgDynamicValues.length; iVarValue++){
                errorTextEn = errorTextEn.replace("<*"+iVarValue+"*>", msgDynamicValues[iVarValue-1].toString());
                errorTextEs = errorTextEs.replace("<*"+iVarValue+"*>", msgDynamicValues[iVarValue-1].toString());
            }        
        }        
        JSONObject errJsObj = new JSONObject();
        errJsObj.put(ResponseTags.DIAGNOTIC.getLabelName(), LPPlatform.LAB_TRUE);
        errJsObj.put(ResponseTags.CATEGORY.getLabelName(), apiName.toUpperCase().replace("API", ""));
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_es", errorTextEs);
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_en", errorTextEn);
        errJsObj.put(ResponseTags.RELATED_OBJECTS.getLabelName(), relatedObjects);        
        errJsObj.put(ResponseTags.IS_ERROR.getLabelName(), false);
        return errJsObj;
    }    
    
    /**
     *
     * @param errorPropertyName
     * @param errorPropertyValue
     * @return
     */
    public static JSONObject responseJSONError(String errorPropertyName, Object[] errorPropertyValue){
        JSONObject errJsObj = new JSONObject();
        errJsObj.put(ResponseTags.MESSAGE.getLabelName(), errorPropertyName);
        String errorTextEn = Parameter.getParameterBundle(LPPlatform.CONFIG_FILES_FOLDER, LPPlatform.CONFIG_FILES_API_ERRORTRAPING, null, errorPropertyName, null);
        if (errorPropertyValue!=null){
            for (int iVarValue=1; iVarValue<=errorPropertyValue.length; iVarValue++){
                errorTextEn = errorTextEn.replace("<*"+iVarValue+"*>", errorPropertyValue[iVarValue-1].toString());
            }        
        }
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_en", errorTextEn);
        String errorTextEs = Parameter.getParameterBundle(LPPlatform.CONFIG_FILES_FOLDER, LPPlatform.CONFIG_FILES_API_ERRORTRAPING, null, errorPropertyName, "es");
        if (errorPropertyValue!=null){
            for (int iVarValue=1; iVarValue<=errorPropertyValue.length; iVarValue++){
                errorTextEs = errorTextEs.replace("<*"+iVarValue+"*>", errorPropertyValue[iVarValue-1].toString());
            }         
        }
        errJsObj.put(ResponseTags.MESSAGE.getLabelName()+"_es", errorTextEs);
        errJsObj.put(ResponseTags.DIAGNOTIC.getLabelName(), LPPlatform.LAB_FALSE); 
        
//        errJsObj.put("category", apiName.toUpperCase().replace("API", ""));
//        errJsObj.put("", relatedObjects);        
        errJsObj.put(ResponseTags.IS_ERROR.getLabelName(), true);
        
        return errJsObj;
    }
    /**
     *
     * @param errorStructure
     * @param language
     * @param schemaPrefix
     * @return
     */
    public static Object[] responseError(Object[] errorStructure, String language, String schemaPrefix){
        Object[] responseObj = new Object[0];
        responseObj = LPArray.addValueToArray1D(responseObj, HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
        if (errorStructure.length>0){
            responseObj = LPArray.addValueToArray1D(responseObj, errorStructure[errorStructure.length-1].toString());        
        }else{
            responseObj = LPArray.addValueToArray1D(responseObj, Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX].getMethodName());        
        }
        return responseObj;
    }
    private static final int CLIENT_CODE_STACK_INDEX;    
    static{
        int i = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()){
            i++;
            if (ste.getClassName().equals(LPPlatform.class.getName())){
                break;
            }
        }
        CLIENT_CODE_STACK_INDEX = i;
    }   
  
    
    private static void servetInvokeResponseErrorServlet(HttpServletRequest request, HttpServletResponse response){
        Rdbms.closeRdbms();      
        RequestDispatcher rd = request.getRequestDispatcher(LPPlatform.SERVLETS_RESPONSE_ERROR_SERVLET_NAME);
        try {   
            rd.forward(request,response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(LPFrontEnd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void servetInvokeResponseSuccessServlet(HttpServletRequest request, HttpServletResponse response){
        Rdbms.closeRdbms();      
        
        RequestDispatcher rd = request.getRequestDispatcher(LPPlatform.SERVLETS_RESPONSE_SUCCESS_SERVLET_NAME);
        try {           
            rd.forward(request,response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(LPFrontEnd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param request
     * @param response
     * @param errorCode
     * @param errorCodeVars
     * @param language
     */
    public static final void servletReturnResponseError(HttpServletRequest request, HttpServletResponse response, String errorCode, Object[] errorCodeVars, String language){  
        JSONObject errJSONMsg = LPFrontEnd.responseJSONError(errorCode,errorCodeVars);
        request.setAttribute(LPPlatform.SERVLETS_RESPONSE_ERROR_ATTRIBUTE_NAME, errJSONMsg.toString());
        servetInvokeResponseErrorServlet(request, response);
    }

    /**
     *
     * @param request
     * @param response
     */
    public static final void servletReturnSuccess(HttpServletRequest request, HttpServletResponse response){  
        request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME,"");
        servetInvokeResponseSuccessServlet(request, response);
    }    

    /**
     *
     * @param request
     * @param response
     * @param myStr
     */
    public static final void servletReturnSuccess(HttpServletRequest request, HttpServletResponse response, String myStr){  
        if (myStr==null){request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME,"");}
        else{request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME, myStr);}
        servetInvokeResponseSuccessServlet(request, response);
    }       

    /**
     *
     * @param request
     * @param response
     * @param jsonObj
     */
    public static final void servletReturnSuccess(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObj){  
        if (jsonObj==null){request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME,"");}
        else{request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME, jsonObj.toString());}
        servetInvokeResponseSuccessServlet(request, response);
    }   

    /**
     *
     * @param request
     * @param response
     * @param jsonArr
     */
    public static final void servletReturnSuccess(HttpServletRequest request, HttpServletResponse response, JSONArray jsonArr){  
        if (jsonArr==null){request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME,"");}
        else{request.setAttribute(LPPlatform.SERVLETS_RESPONSE_SUCCESS_ATTRIBUTE_NAME, jsonArr.toString());}
        servetInvokeResponseSuccessServlet(request, response);
    }  
    
    /**
     *
     * @param request
     * @param response
     * @param lPFalseObject
     */
    public static final void servletReturnResponseErrorLPFalseDiagnostic(HttpServletRequest request, HttpServletResponse response, Object[] lPFalseObject){       
        JSONObject errJSONMsg = LPFrontEnd.responseJSONDiagnosticLPFalse(lPFalseObject);
        request.setAttribute(LPPlatform.SERVLETS_RESPONSE_ERROR_ATTRIBUTE_NAME, errJSONMsg.toString());        
        servetInvokeResponseErrorServlet(request, response);
    }    

    /**
     *
     * @param request
     * @param response
     * @param lPTrueObject
     */
    public static final void servletReturnResponseErrorLPTrueDiagnostic(HttpServletRequest request, HttpServletResponse response, Object[] lPTrueObject){       
        JSONObject successJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(lPTrueObject);
        request.setAttribute(LPPlatform.SERVLETS_RESPONSE_ERROR_ATTRIBUTE_NAME, successJSONMsg.toString());        
        servetInvokeResponseErrorServlet(request, response);
    }      
}
