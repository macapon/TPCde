/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import com.labplanet.servicios.app.AuthenticationAPIParams.AuthenticationAPIEndpoints;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPSession;
import databases.Rdbms;
import databases.TblsApp;
import databases.Token;
import databases.TblsApp.Users;
import functionaljavaa.parameter.Parameter;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import functionaljavaa.user.UserAndRolesViews;
import static functionaljavaa.user.UserAndRolesViews.BUNDLE_PARAMETER_CREDENTIALS_USER_IS_CASESENSITIVE;
import static functionaljavaa.user.UserAndRolesViews.setUserDefaultTabsOnLogin;
import static functionaljavaa.user.UserAndRolesViews.setUserNewEsign;
import static functionaljavaa.user.UserAndRolesViews.setUserNewPassword;
import functionaljavaa.user.UserProfile;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import lbplanet.utilities.LPAPIArguments;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author Administrator
 */
public class AuthenticationAPI extends HttpServlet {    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
        
        try (PrintWriter out = response.getWriter()) {            
            
            if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}
            ResourceBundle prop = ResourceBundle.getBundle(Parameter.BUNDLE_TAG_PARAMETER_CONFIG_CONF);
            
            String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);                                    
            AuthenticationAPIEndpoints endPoint=null;
            try{
                endPoint = AuthenticationAPIEndpoints.valueOf(actionName.toUpperCase());
            }catch(Exception e){
                LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                return;                   
            }
            Object[] areMandatoryParamsInResponse = LPHttp.areEndPointMandatoryParamsInApiRequest(request, endPoint.getArguments());
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response,
                        LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);
                return;
            }                            
            Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());
            switch (endPoint){
                case AUTHENTICATE:                                             
                    String dbUserName = argValues[0].toString();
                    String dbUserPassword = argValues[1].toString();                 
                    String userIsCaseSensitive = prop.getString(BUNDLE_PARAMETER_CREDENTIALS_USER_IS_CASESENSITIVE);
                    if (!Boolean.valueOf(userIsCaseSensitive)) dbUserName=dbUserName.toLowerCase();
                    
                    String personName = UserAndRolesViews.getPersonByUser(dbUserName);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(personName)){               
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_PROPERTY_PERSON_NOT_FOUND, null, language);              
                        return;                                                          
                    }                    
                    Object[] validUserPassword = UserAndRolesViews.isValidUserPassword(dbUserName, dbUserPassword);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(validUserPassword[0].toString())){
                        validUserPassword = UserAndRolesViews.isValidUserPassword(dbUserName, dbUserPassword);
                        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(validUserPassword[0].toString())){     
                            LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_PROPERTY_INVALID_USER_PSSWD, null, language);              
                            return;                               
                        }
                    }                                                          
                    Token token = new Token("");                    
                    String myToken = token.createToken(dbUserName, dbUserPassword, personName, "Admin", "", "", "");                    

                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_USER_INFO_ID, personName);
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_MY_TOKEN, myToken);
                    LPFrontEnd.servletReturnSuccess(request, response, jsonObj);
                    return;
                case GETUSERROLE:                                                 
                    String firstToken = argValues[0].toString();                    
                    token = new Token(firstToken);
                    
                    UserProfile usProf = new UserProfile();
                    Object[] allUserProcedurePrefix = usProf.getAllUserProcedurePrefix(token.getUserName());
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(allUserProcedurePrefix[0].toString())){
                        LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, allUserProcedurePrefix);
                        return;                                             
                    }                                        
                    Object[] allUserProcedureRoles = usProf.getProcedureUserProfileFieldValues(allUserProcedurePrefix, token.getPersonName());
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(allUserProcedureRoles[0].toString())){            
                        LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, allUserProcedureRoles);
                        return;                                                            
                    }                    
                    JSONArray jArray= new JSONArray();
                    jArray.addAll(Arrays.asList(allUserProcedureRoles));        
                    response.getWriter().write(jArray.toJSONString()); 
                    Rdbms.closeRdbms();    
                    return;                                
                case FINALTOKEN:   
                  Rdbms.stablishDBConection();
                  if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}   
                    firstToken = argValues[0].toString();
                    String userRole = argValues[1].toString();

                    token = new Token(firstToken);
                    String[] fieldsName = new String[]{TblsApp.AppSession.FLD_PERSON.getName(), TblsApp.AppSession.FLD_ROLE_NAME.getName()};
                    Object[] fieldsValue = new Object[]{token.getPersonName(), userRole};
                    Object[] newAppSession = LPSession.newAppSession(fieldsName, fieldsValue, request.getRemoteAddr());                    
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(newAppSession[0].toString())){   
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_PROPERTY_SESSION_ID_NOT_GENERATED, null, language);              
                        return;                                                         
                    }                    
                    Integer sessionId = Integer.parseInt((String) newAppSession[newAppSession.length-1]);
                    String sessionIdStr = sessionId.toString();
                    if (sessionId==null){
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_PROPERTY_SESSION_ID_NULL_NOT_ALLOWED, null, language);          
                        return;                                                   
                    }
                   Date nowLocalDate =LPDate.getTimeStampLocalDate();
                   Object[][] userInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.SCHEMA_APP, Users.TBL.getName(), 
                            new String[]{Users.FLD_USER_NAME.getName()}, new Object[]{token.getUserName()}, 
                            new String[]{Users.FLD_ESIGN.getName(), TblsApp.Users.FLD_TABS_ON_LOGIN.getName()});
                   
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(userInfo[0][0].toString())){  
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_PROPERTY_ESIGN_INFO_NOT_AVAILABLE, null, language);       
                        return;                                                                                
                    }                               
                    String myFinalToken = token.createToken(token.getUserName(), token.getUsrPw(), token.getPersonName(), 
                            userRole, sessionIdStr, nowLocalDate.toString(), userInfo[0][0].toString());
                    Rdbms.closeRdbms();                    
                    jsonObj = new JSONObject();
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_FINAL_TOKEN, myFinalToken);
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_APP_SESSION_ID, sessionIdStr);
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_APP_SESSION_DATE, nowLocalDate.toString());
                    if (userInfo[0][0].toString().length()==0)
                        jsonObj.put("warning", "no esign phrase");
                    String tabsStr=userInfo[0][1].toString();
                    String[] tabs=tabsStr.split("\\|");
                    JSONArray jArr=new JSONArray();
                    for (String curTab: tabs){
                        String[] tabAttrArr=curTab.split("\\*");
                        JSONObject jObj = new JSONObject();
                        for (String curTabAttr: tabAttrArr){ 
                            String[] curAttr=curTabAttr.split("\\:");
                            if (curAttr.length>=2)
                                jObj.put(curAttr[0], curAttr[1]);
                        }
                        jArr.add(jObj);
                    }                    
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_APP_USER_TABS_ON_LOGIN, jArr);
                    LPFrontEnd.servletReturnSuccess(request, response, jsonObj);
                    return;                                   
                case TOKEN_VALIDATE_ESIGN_PHRASE:     
                    myToken = argValues[0].toString();
                    String esignPhraseToCheck = argValues[1].toString();

                    token = new Token(myToken);
                    if (token.geteSign().length()==0){               
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_TOKEN_ESIGN_VALUE_NULL, new Object[]{esignPhraseToCheck}, language);
                        return;                             
                    }
                    if(esignPhraseToCheck.equals(token.geteSign())){   
                        LPFrontEnd.servletReturnSuccess(request, response);
                        return;                                             
                    }else{               
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_ESIGN_TO_CHECK_INVALID, new Object[]{esignPhraseToCheck}, language);
                        return;                             
                    }                    
                case TOKEN_VALIDATE_USER_CREDENTIALS:     
                    myToken = argValues[0].toString();
                    String userToCheck = argValues[1].toString();                      
                    String passwordToCheck = argValues[2].toString();
                    
                    token = new Token(myToken);
                    if ( (userToCheck.equals(token.getUserName())) && (passwordToCheck.equals(token.getUsrPw())) ){
                        LPFrontEnd.servletReturnSuccess(request, response);
                    }else{                        
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_USER_PSSWD_TO_CHECK_INVALID, new Object[]{userToCheck}, language);              
                    }    
                    break;
                case USER_CHANGE_PSWD:     
                    String finalToken = argValues[0].toString();
                    String newPassword = argValues[1].toString();
                    userToCheck = argValues[2].toString();
                    passwordToCheck = argValues[3].toString();
                    token = new Token(finalToken);
                    Object[] newPwDiagn=setUserNewPassword(token.getUserName(), newPassword);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(newPwDiagn[0].toString()))
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_USER_NEW_PSSWD_NOT_SET, new Object[]{token.getUserName()}, language);              
                    String appStartedDate=null;
                    if (token.getAppSessionStartedDate()!=null) appStartedDate=token.getAppSessionStartedDate().toString();
                    Token newToken= new Token("");
                    String myNewToken=newToken.createToken(token.getUserName(), 
                            newPassword, 
                            token.getPersonName(), 
                            token.getUserRole(), 
                            token.getAppSessionId(), 
                            appStartedDate, 
                            token.geteSign());
                    Rdbms.closeRdbms();  
                    RelatedObjects rObj=RelatedObjects.getInstance();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsApp.Users.TBL.getName(), TblsApp.Users.TBL.getName(), token.getUserName());
                    jsonObj = new JSONObject();
                    jsonObj = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), new Object[0], rObj.getRelatedObject());                
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_FINAL_TOKEN, myNewToken);
                    rObj.killInstance();
                    LPFrontEnd.servletReturnSuccess(request, response, jsonObj);
                    return;      
                case USER_CHANGE_PSWD_SEND_MAIL:     
//                    lbplanet.utilities.LPMailing.sendMailViaTLS("prueba", "esto es una prueba", new String[]{"info.fran.gomez@gmail.com"}, 
//                        null, null, new String[]{"d:/FE Refactoring LP.xlsx", "D:/LP-Documentacion/hexagon-white-blue-light.jpg"});
lbplanet.utilities.LPMailing.sendMailViaSSL("prueba SSL", "SSL esto es una prueba", new String[]{"info.fran.gomez@gmail.com"}, 
        null, null, new String[]{"d:/FE Refactoring LP.xlsx"});
                    
                    return;
                case USER_CHANGE_PSWD_BY_MAIL:     
                    finalToken = argValues[0].toString();
                    newPassword = argValues[1].toString();
                    userToCheck = argValues[2].toString();
                    passwordToCheck = argValues[3].toString();
                    token = new Token(finalToken);
                    newPwDiagn=setUserNewPassword(token.getUserName(), newPassword);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(newPwDiagn[0].toString()))
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_USER_NEW_PSSWD_NOT_SET, new Object[]{token.getUserName()}, language);              
                    appStartedDate=null;
                    if (token.getAppSessionStartedDate()!=null) appStartedDate=token.getAppSessionStartedDate().toString();
                    newToken= new Token("");
                    myNewToken=newToken.createToken(token.getUserName(), 
                            newPassword, 
                            token.getPersonName(), 
                            token.getUserRole(), 
                            token.getAppSessionId(), 
                            appStartedDate, 
                            token.geteSign());
                    Rdbms.closeRdbms();  
                    rObj=RelatedObjects.getInstance();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsApp.Users.TBL.getName(), TblsApp.Users.TBL.getName(), token.getUserName());
                    jsonObj = new JSONObject();
                    jsonObj = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), new Object[0], rObj.getRelatedObject());                
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_FINAL_TOKEN, myNewToken);
                    rObj.killInstance();
                    LPFrontEnd.servletReturnSuccess(request, response, jsonObj);
                    return;      
                case USER_CHANGE_ESIGN:     
                    finalToken = argValues[0].toString();
                    String newEsign = argValues[1].toString();
                    userToCheck = argValues[2].toString();
                    passwordToCheck = argValues[3].toString();
                    token = new Token(finalToken);
                    Object[] newEsignDiagn=setUserNewEsign(token.getUserName(), newEsign);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(newEsignDiagn[0].toString()))
                        LPFrontEnd.servletReturnResponseError(request, response, AuthenticationAPIParams.ERROR_API_ERRORTRAPING_PROPERTY_USER_NEW_PSSWD_NOT_SET, new Object[]{token.getUserName()}, language);              
                    appStartedDate=null;
                    if (token.getAppSessionStartedDate()!=null) appStartedDate=token.getAppSessionStartedDate().toString();
                    newToken= new Token("");
                    myNewToken=newToken.createToken(token.getUserName(), 
                            token.getUsrPw(), 
                            token.getPersonName(), 
                            token.getUserRole(), 
                            token.getAppSessionId(), 
                            appStartedDate, 
                            newEsign);
                    Rdbms.closeRdbms();                    
                    jsonObj = new JSONObject();
                    rObj=RelatedObjects.getInstance();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsApp.Users.TBL.getName(), TblsApp.Users.TBL.getName(), token.getUserName());
                    jsonObj = new JSONObject();
                    jsonObj = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), new Object[0], rObj.getRelatedObject());                
                    jsonObj.put(AuthenticationAPIParams.RESPONSE_JSON_TAG_FINAL_TOKEN, myNewToken);
                    rObj.killInstance();
                    LPFrontEnd.servletReturnSuccess(request, response, jsonObj);
                    return;        
                case SET_DEFAULT_TABS_ON_LOGIN: 
                    finalToken = argValues[0].toString();
                    String tabsString = argValues[1].toString();
                    token = new Token(finalToken);
                    Object[] diagn=setUserDefaultTabsOnLogin(token, tabsString);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(diagn[0].toString()))
                        LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, diagn);   
                    else{
                        rObj=RelatedObjects.getInstance();
                        jsonObj = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), new Object[0], rObj.getRelatedObject());                                        
                        rObj.killInstance();
                        LPFrontEnd.servletReturnSuccess(request, response, jsonObj);                        
                    }
                    return;
                default:      
                    LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;
            }
        }catch(@SuppressWarnings("FieldNameHidesFieldInSuperclass") Exception e){            
            String exceptionMessage = e.getMessage();     
            LPFrontEnd.servletReturnResponseError(request, response, exceptionMessage, null, null);                    
        } finally {
            // release database resources
            try {
                Rdbms.closeRdbms();   
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
        try{
        processRequest(request, response);
        }catch(ServletException|IOException e){
            LPFrontEnd.servletReturnResponseError(request, response, e.getMessage(), new Object[]{}, null);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        try{
        processRequest(request, response);
        }catch(ServletException|IOException e){
            LPFrontEnd.servletReturnResponseError(request, response, e.getMessage(), new Object[]{}, null);
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