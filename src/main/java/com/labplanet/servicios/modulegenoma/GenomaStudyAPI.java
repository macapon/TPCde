/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulegenoma;

import functionaljavaa.modulegenoma.GenomaDataStudyIndividuals;
import functionaljavaa.modulegenoma.GenomaDataStudyFamily;
import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.modulegenoma.GenomaDataStudy;
import functionaljavaa.modulegenoma.GenomaDataStudyIndividualSamples;
import functionaljavaa.modulegenoma.GenomaDataStudySamplesSet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
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
public class GenomaStudyAPI extends HttpServlet {

    public enum  GenomaStudyAPIParamsList{
        projectName, studyName, individualId, individualName, sampleId, samplesSetName, familyName, individualsList, samplesList, fieldsNames, fieldsValues, userName, userRole}
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken|schemaPrefix";
            
    public enum  GenomaStudyAPIEndPoints{
          STUDY_NEW("STUDY_NEW", "studyName"), STUDY_UPDATE("STUDY_UPDATE", "studyName|fieldsNames|fieldsValues"),
          STUDY_ACTIVATE("STUDY_ACTIVATE", "studyName"), STUDY_DEACTIVATE("STUDY_DEACTIVATE", "studyName"),
          STUDY_ADD_USER("STUDY_ADD_USER", "studyName|userName|userRole"), STUDY_REMOVE_USER("STUDY_REMOVE_USER", "studyName|userName|userRole"),
          STUDY_CHANGE_USER_ROLE("STUDY_CHANGE_USER_ROLE", "studyName|userName|userRole"), STUDY_USER_ACTIVATE("STUDY_USER_ACTIVATE", "studyName|userName|userRole"),
          STUDY_USER_DEACTIVATE("STUDY_USER_DEACTIVATE", "studyName|userName|userRole"),
          STUDY_ADD_INDIVIDUAL("STUDY_ADD_INDIVIDUAL", "studyName|individualName"),
          STUDY_INDIVIDUAL_ACTIVATE("STUDY_INDIVIDUAL_ACTIVATE", "studyName|individualId"), STUDY_INDIVIDUAL_DEACTIVATE("STUDY_INDIVIDUAL_DEACTIVATE", "studyName|individualId"),
          STUDY_ADD_INDIVIDUAL_SAMPLE("STUDY_ADD_INDIVIDUAL_SAMPLE", "studyName|individualId"),
          STUDY_INDIVIDUAL_SAMPLE_ACTIVATE("STUDY_INDIVIDUAL_SAMPLE_ACTIVATE", "studyName|individualId|sampleId"), STUDY_INDIVIDUAL_SAMPLE_DEACTIVATE("STUDY_INDIVIDUAL_SAMPLE_DEACTIVATE", "studyName|individualId|sampleId"),
          STUDY_ADD_FAMILY("STUDY_ADD_FAMILY", "studyName|familyName"),
          STUDY_FAMILY_ACTIVATE("STUDY_FAMILY_ACTIVATE", "studyName|familyName"), STUDY_FAMILY_DEACTIVATE("STUDY_FAMILY_DEACTIVATE", "studyName|familyName"),
          STUDY_FAMILY_ADD_INDIVIDUAL("STUDY_FAMILY_ADD_INDIVIDUAL", "studyName|familyName|individualId"), STUDY_FAMILY_REMOVE_INDIVIDUAL("STUDY_FAMILY_REMOVE_INDIVIDUAL", "studyName|familyName|individualId"),
          STUDY_ADD_SAMPLES_SET("STUDY_ADD_SAMPLE_SET", "studyName|samplesSetName"),
          STUDY_SAMPLES_SET_ACTIVATE("STUDY_SAMPLE_SET_ACTIVATE", "studyName|samplesSetName"), STUDY_SAMPLES_SET_DEACTIVATE("STUDY_SAMPLE_SET_DEACTIVATE", "studyName|samplesSetName"),
          STUDY_SAMPLES_SET_ADD_SAMPLE("STUDY_SAMPLE_SET_ADD_SAMPLE", "studyName|samplesSetName|sampleId"), STUDY_SAMPLES_SET_REMOVE_SAMPLE("STUDY_SAMPLE_SET_REMOVE_SAMPLE", "studyName|samplesSetName|sampleId"),
          ;
        private GenomaStudyAPIEndPoints(String name, String mandatoryFields){
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
    };
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
        GenomaDataStudy stud = new GenomaDataStudy();
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);

        String language = LPFrontEnd.setLanguage(request); 
        String[] errObject = new String[]{"Servlet Genoma ProjectAPI at " + request.getServletPath()};   

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

        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);    
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
                case "STUDY_NEW":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_NEW.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    String fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    String fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    String[] fieldNames=null;
                    Object[] fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    String studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.Study.FLD_NAME.getName())].toString();
                    String projectName=request.getParameter(GenomaStudyAPIParamsList.projectName.toString());
                    if (projectName.length()==0)
                        projectName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.Study.FLD_PROJECT.getName())].toString();

                    dataSample =stud.createStudy(schemaPrefix, token, studyName, projectName, fieldNames, fieldValues,  false);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_UPDATE":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_UPDATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.Study.FLD_NAME.getName())].toString();

                    dataSample =stud.studyUpdate(schemaPrefix, token, studyName, fieldNames, fieldValues);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_ACTIVATE":
                case "STUDY_DEACTIVATE":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if ("STUDY_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =stud.studyActivate(schemaPrefix, token, studyName);
                    else if ("STUDY_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =stud.studyDeActivate(schemaPrefix, token, studyName);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;
                case "STUDY_ADD_USER":
                case "STUDY_REMOVE_USER":
                case "STUDY_CHANGE_USER_ROLE":
                case "STUDY_USER_ACTIVATE":
                case "STUDY_USER_DEACTIVATE":                           
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ADD_USER.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    String userName=request.getParameter(GenomaStudyAPIParamsList.userName.toString());
                    String userRole=request.getParameter(GenomaStudyAPIParamsList.userRole.toString());
                    dataSample =stud.studyUserManagement(schemaPrefix, token, actionName, studyName, userName, userRole);
                    break;
                case "STUDY_ADD_INDIVIDUAL":
                    GenomaDataStudyIndividuals StdInd = new GenomaDataStudyIndividuals();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ADD_INDIVIDUAL.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_STUDY.getName())].toString();
                    String individualName=request.getParameter(GenomaStudyAPIParamsList.individualName.toString());
                    if (individualName.length()==0)
                        individualName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_INDIV_NAME.getName())].toString();

                    dataSample =StdInd.createStudyIndividual(schemaPrefix, token, studyName, individualName, fieldNames, fieldValues,  false);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_INDIVIDUAL_ACTIVATE":
                case "STUDY_INDIVIDUAL_DEACTIVATE":     
                    StdInd = new GenomaDataStudyIndividuals();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_INDIVIDUAL_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    String individualId=request.getParameter(GenomaStudyAPIParamsList.individualId.toString());
                    if ("STUDY_INDIVIDUAL_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdInd.studyIndividualActivate(schemaPrefix, token, studyName, Integer.valueOf(individualId));
                    else if ("STUDY_INDIVIDUAL_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdInd.studyIndividualDeActivate(schemaPrefix, token, studyName, Integer.valueOf(individualId));
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;          
                case "STUDY_ADD_INDIVIDUAL_SAMPLE":
                    GenomaDataStudyIndividualSamples StdIndSmp = new GenomaDataStudyIndividualSamples();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ADD_INDIVIDUAL_SAMPLE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_STUDY.getName())].toString();
                    individualId=request.getParameter(GenomaStudyAPIParamsList.individualId.toString());
                    if (individualId.length()==0)
                        individualId = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_INDIV_NAME.getName())].toString();

                    dataSample =StdIndSmp.createStudyIndividualSample(schemaPrefix, token, studyName, Integer.valueOf(individualId), fieldNames, fieldValues,  false);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_INDIVIDUAL_SAMPLE_ACTIVATE":
                case "STUDY_INDIVIDUAL_SAMPLE_DEACTIVATE":     
                    StdIndSmp = new GenomaDataStudyIndividualSamples();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_INDIVIDUAL_SAMPLE_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    individualId=request.getParameter(GenomaStudyAPIParamsList.individualId.toString());
                    String sampleId=request.getParameter(GenomaStudyAPIParamsList.sampleId.toString());
                    if ("STUDY_INDIVIDUAL_SAMPLE_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdIndSmp.studyIndividualSampleActivate(schemaPrefix, token, studyName, Integer.valueOf(individualId), Integer.valueOf(sampleId));
                    else if ("STUDY_INDIVIDUAL_SAMPLE_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdIndSmp.studyIndividualSampleDeActivate(schemaPrefix, token, studyName, Integer.valueOf(individualId), Integer.valueOf(sampleId));
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;                          
                case "STUDY_ADD_FAMILY":
                    GenomaDataStudyFamily StdFam = new GenomaDataStudyFamily();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ADD_FAMILY.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_STUDY.getName())].toString();
                    String familyName=request.getParameter(GenomaStudyAPIParamsList.familyName.toString());
                    if (familyName.length()==0)
                        familyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudyIndividual.FLD_INDIV_NAME.getName())].toString();
                    String[] individualsListArr =new String[0];
                    String individualsList=request.getParameter(GenomaStudyAPIParamsList.individualsList.toString());                    
                    if (individualsList!=null && individualsList.length()>0)
                        individualsListArr = individualsList.split("\\|");
                    dataSample=StdFam.createStudyFamily(schemaPrefix, token, studyName, familyName, individualsListArr, fieldNames, fieldValues, Boolean.FALSE);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_FAMILY_ACTIVATE":
                case "STUDY_FAMILY_DEACTIVATE":     
                    StdFam = new GenomaDataStudyFamily();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_FAMILY_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    familyName=request.getParameter(GenomaStudyAPIParamsList.familyName.toString());
                    if ("STUDY_FAMILY_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdFam.studyFamilyActivate(schemaPrefix, token, studyName, familyName);
                    else if ("STUDY_FAMILY_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdFam.studyFamilyDeActivate(schemaPrefix, token, studyName, familyName);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;       
                case "STUDY_FAMILY_ADD_INDIVIDUAL":
                case "STUDY_FAMILY_REMOVE_INDIVIDUAL":     
                    StdFam = new GenomaDataStudyFamily();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_FAMILY_ADD_INDIVIDUAL.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    familyName=request.getParameter(GenomaStudyAPIParamsList.familyName.toString());
                    individualId=request.getParameter(GenomaStudyAPIParamsList.individualId.toString());
                    if ("STUDY_FAMILY_ADD_INDIVIDUAL".equalsIgnoreCase(actionName))
                        dataSample =StdFam.studyFamilyAddIndividual(schemaPrefix, token, studyName, familyName, individualId);
                    else if ("STUDY_FAMILY_REMOVE_INDIVIDUAL".equalsIgnoreCase(actionName))
                        dataSample =StdFam.studyFamilyRemoveIndividual(schemaPrefix, token, studyName, familyName, individualId);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;       
                case "STUDY_ADD_SAMPLES_SET":
                    GenomaDataStudySamplesSet StdSmpSet = new GenomaDataStudySamplesSet();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_ADD_SAMPLES_SET.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    fieldName=request.getParameter(GenomaStudyAPIParamsList.fieldsNames.toString());                                        
                    fieldValue=request.getParameter(GenomaStudyAPIParamsList.fieldsValues.toString());                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    if (studyName.length()==0)
                        studyName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudySamplesSet.FLD_STUDY.getName())].toString();
                    String samplesSetName=request.getParameter(GenomaStudyAPIParamsList.samplesSetName.toString());
                    if (samplesSetName.length()==0)
                        samplesSetName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsGenomaData.StudySamplesSet.FLD_NAME.getName())].toString();
                    String[] samplesListArr =new String[0];
                    String samplesList=request.getParameter(GenomaStudyAPIParamsList.samplesList.toString());                    
                    if (samplesList!=null && samplesList.length()>0)
                        samplesListArr = samplesList.split("\\|");
                    dataSample=StdSmpSet.createStudySamplesSet(schemaPrefix, token, studyName, samplesSetName, samplesListArr, fieldNames, fieldValues, Boolean.FALSE);
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    break;
                case "STUDY_SAMPLES_SET_ACTIVATE":
                case "STUDY_SAMPLES_SET_DEACTIVATE":     
                    StdSmpSet = new GenomaDataStudySamplesSet();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_SAMPLES_SET_ACTIVATE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    samplesSetName=request.getParameter(GenomaStudyAPIParamsList.samplesSetName.toString());
                    if ("STUDY_SAMPLES_SET_ACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdSmpSet.studySamplesSetActivate(schemaPrefix, token, studyName, samplesSetName);
                    else if ("STUDY_SAMPLES_SET_DEACTIVATE".equalsIgnoreCase(actionName))
                        dataSample =StdSmpSet.studySamplesSetDeActivate(schemaPrefix, token, studyName, samplesSetName);
                    else
                        LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    break;       
                case "STUDY_SAMPLES_SET_ADD_SAMPLE":
                case "STUDY_SAMPLES_SET_REMOVE_SAMPLE":     
                    StdSmpSet = new GenomaDataStudySamplesSet();
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, GenomaStudyAPIEndPoints.STUDY_SAMPLES_SET_ADD_SAMPLE.getMandatoryFields().split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    studyName=request.getParameter(GenomaStudyAPIParamsList.studyName.toString());
                    samplesSetName=request.getParameter(GenomaStudyAPIParamsList.samplesSetName.toString());
                    sampleId=request.getParameter(GenomaStudyAPIParamsList.sampleId.toString());
                    if ("STUDY_SAMPLES_SET_ADD_SAMPLE".equalsIgnoreCase(actionName))
                        dataSample =StdSmpSet.studySamplesSetAddSample(schemaPrefix, token, studyName, samplesSetName, sampleId);
                    else if ("STUDY_SAMPLES_SET_REMOVE_SAMPLE".equalsIgnoreCase(actionName))
                        dataSample =StdSmpSet.studySamplesSetRemoveSample(schemaPrefix, token, studyName, samplesSetName, sampleId);
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
