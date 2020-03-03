/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPPlatform;
import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.batch.incubator.DataBatchIncubator;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramCorrectiveAction;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSample;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysis;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysisResult;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysisResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPDate;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class EnvMonAPI extends HttpServlet {    

    /**
     *
     */
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken|schemaPrefix";

    /**
     *
     */
    public static final String MANDATORY_PARAMS_CORRECTIVE_ACTION_COMPLETE="programName|programCorrectiveActionId";
    
    /**
     *
     */
    public static final String PARAMETER_PROGRAM_SAMPLE_TEMPLATE="sampleTemplate";

    /**
     *
     */
    public static final String PARAMETER_PROGRAM_SAMPLE_TEMPLATE_VERSION="sampleTemplateVersion";       

    /**
     *
     */
    public static final String PARAMETER_NUM_SAMPLES_TO_LOG="numSamplesToLog";

    /**
     *
     */
    public static final String PARAMETER_PROGRAM_FIELD_NAME="fieldName";

    /**
     *
     */
    public static final String PARAMETER_PROGRAM_FIELD_VALUE="fieldValue";    

    /**
     *
     */
    public static final String PARAMETER_PROGRAM_SAMPLE_PROGRAM_NAME="programName"; 

    /**
     *
     */
    public static final String PARAMETER_PROGRAM_SAMPLE_CORRECITVE_ACTION_ID="programCorrectiveActionId"; 
    
    /**
     *
     */
    public static final String TABLE_SAMPLE_PROGRAM_FIELD="program"; 
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);
        

        String language = LPFrontEnd.setLanguage(request); 
        String[] errObject = new String[]{"Servlet programAPI at " + request.getServletPath()};   

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

        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);    
        String schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG);    
        //Rdbms.setTransactionId(schemaConfigName);
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
            
            DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();           
            DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();           
            DataProgramSample prgSmp = new DataProgramSample();     
            DataSample smp = new DataSample(prgSmpAna);    
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(prgSmpAnaRes);               
            Object[] actionDiagnoses = null;
            
            switch (actionName.toUpperCase()){
                case "CORRECTIVE_ACTION_COMPLETE":
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_CORRECTIVE_ACTION_COMPLETE.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                     
                    String programName=request.getParameter(PARAMETER_PROGRAM_SAMPLE_PROGRAM_NAME);
                    Integer correctiveActionId = Integer.valueOf(request.getParameter(PARAMETER_PROGRAM_SAMPLE_CORRECITVE_ACTION_ID));                                  
                    
                    actionDiagnoses = DataProgramCorrectiveAction.markAsCompleted(schemaPrefix, token, correctiveActionId);
                    break;
                case EnvMonitAPIParams.API_ENDPOINT_EM_BATCH_INCUB_CREATE:                    
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, EnvMonitAPIParams.MANDATORY_PARAMS_BATCH_INCUB_CREATE.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                                                              
                    String batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    String batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    String batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    String fieldName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);                                        
                    String fieldValue=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);                    
                    String[] fieldNames=new String[0];
                    Object[] fieldValues=new Object[0];
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    actionDiagnoses= DataBatchIncubator.createBatch(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion), fieldNames, fieldValues);
                    break;                    
                case EnvMonitAPIParams.API_ENDPOINT_EM_BATCH_ASSIGN_INCUB:
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, EnvMonitAPIParams.MANDATORY_PARAMS_BATCH_ASSIGN_INCUB.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                                                              
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    String incubationName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_INCUBATOR_NAME);
                    actionDiagnoses=DataBatchIncubator.batchAssignIncubator(schemaPrefix, token, batchName, incubationName);
                    break;
                case EnvMonitAPIParams.API_ENDPOINT_EM_BATCH_UPDATE_INFO:
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, EnvMonitAPIParams.MANDATORY_PARAMS_BATCH_UPDATE_INFO.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                                                              
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    fieldName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FIELD_NAME);
                    String[] fieldsName = fieldName.split("\\|");
                    fieldValue = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FIELD_VALUE);
                    Object[] fieldsValue= LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));
                    actionDiagnoses=DataBatchIncubator.batchUpdateInfo(schemaPrefix, token, batchName, fieldsName, fieldsValue);
                    break;
                case EnvMonitAPIParams.API_ENDPOINT_EM_BATCH_INCUB_START:
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, EnvMonitAPIParams.MANDATORY_PARAMS_BATCH_INCUB_START.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                                                              
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);                    
                    String incubName=null;
                    actionDiagnoses=DataBatchIncubator.batchStarted(schemaPrefix, token, batchName, incubName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion));
                    break;                    
                case EnvMonitAPIParams.API_ENDPOINT_EM_BATCH_INCUB_END:
                    areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, EnvMonitAPIParams.MANDATORY_PARAMS_BATCH_INCUB_END.split("\\|"));
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                        LPFrontEnd.servletReturnResponseError(request, response, 
                                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                        return;                  
                    }                                                              
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);                    
                    incubName=null;
                    actionDiagnoses=DataBatchIncubator.batchEnded(schemaPrefix, token, batchName, incubName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion));
                    break;
                case EnvMonitAPIParams.API_ENDPOINT_EM_LOGSAMPLE_SCHEDULER:
                    String dateStartStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_DATE_START);      
                    LocalDateTime dateStart=null;
                    if (dateStartStr!=null) dateStart=LPDate.dateStringFormatToLocalDateTime(dateStartStr);
                    String dateEndStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_DATE_END);      
                    LocalDateTime dateEnd=null;
                    if (dateEndStr!=null) dateEnd=LPDate.dateStringFormatToLocalDateTime(dateEndStr);
                    String programNameStr = request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME);      
                    programName=null;
                    if (programNameStr!=null) programName=programNameStr;
                    actionDiagnoses=prgSmp.logProgramSampleScheduled(schemaPrefix, token, programName, dateStart, dateEnd);
                    break;
                default:    
                    LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                    return;    
            }    
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(actionDiagnoses[0].toString())){  
//                Rdbms.rollbackWithSavePoint();
//                if (!con.getAutoCommit()){
//                    con.rollback();
//                    con.setAutoCommit(true);}                
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, actionDiagnoses);   
            }else{
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(actionDiagnoses);
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
        }      }

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
