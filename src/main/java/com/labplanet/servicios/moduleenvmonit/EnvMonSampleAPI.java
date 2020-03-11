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
import com.labplanet.servicios.modulesample.SampleAPIParams;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.batch.incubator.DataBatchIncubator;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSample;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysis;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysisResult;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysisResult;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class EnvMonSampleAPI extends HttpServlet {

    /**
     *
     */
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken|schemaPrefix";
    
    public enum EnvMonSampleAPIEndpoints{
        /**
         *
         */
        LOGSAMPLE("LOGSAMPLE", "sampleTemplate|sampleTemplateVersion|programName|locationName", "", "sampleLogged_success"),
        ENTERRESULT("ENTERRESULT", "resultId|rawValueResult", "", "enterResult_success"),
        ADD_SAMPLE_MICROORGANISM("ADD_SAMPLE_MICROORGANISM", "sampleId|microorganismName", "", "MigroorganismAdded_success"),
        EM_BATCH_INCUB_ADD_SMP("EM_BATCH_INCUB_ADD_SMP", "batchName|batchTemplateId|batchTemplateVersion|sampleId", "", "batchIncubator_sampleAdded_success"),
        EM_BATCH_INCUB_MOVE_SMP("EM_BATCH_INCUB_MOVE_SMP", "batchName|batchTemplateId|batchTemplateVersion|sampleId|positionRow|positionCol", "", "batchIncubator_sampleMoved_success"),
        EM_BATCH_INCUB_REMOVE_SMP("EM_BATCH_INCUB_REMOVE_SMP", "batchName|batchTemplateId|batchTemplateVersion|sampleId", "", "batchIncubator_sampleRemoved_success"),
        GETSAMPLEINFO2("*****", "", "", ""),
        ;      
        private EnvMonSampleAPIEndpoints(String name, String mandatoryParams, String optionalParams, String successMessageCode){
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
    public static final String PARAMETER_PROGRAM_SAMPLE_PROGRAM_FIELD="programName"; 
    
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
 /*       if (con==null){
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The Transaction cannot be created, the action should be aborted");
             return;
        }
*/        
/*        try {
            con.rollback();
            con.setAutoCommit(true);    
        } catch (SQLException ex) {
            Logger.getLogger(EnvMonAPI.class.getName()).log(Level.SEVERE, null, ex);
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
            
            DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();           
            DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();           
            DataProgramSample prgSmp = new DataProgramSample();     
            DataSample smp = new DataSample(prgSmpAna);    
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(prgSmpAnaRes);               
            Object[] dataSample = null;

            EnvMonSampleAPIEndpoints endPoint = null;
            Object[] actionDiagnoses = null;
            try{
                endPoint = EnvMonSampleAPIEndpoints.valueOf(actionName.toUpperCase());
            }catch(Exception e){
                LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
                return;                   
            }
            areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, endPoint.getMandatoryParams().split("\\|"));
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response,
                        LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);
                return;
            }    
            Object[] messageDynamicData=new Object[]{};
            RelatedObjects rObj=RelatedObjects.getInstance();
            switch (endPoint){
                case LOGSAMPLE:
                    String sampleTemplate=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE);
                    String sampleTemplateVersionStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE_VERSION);                                  

                    Integer sampleTemplateVersion = Integer.parseInt(sampleTemplateVersionStr);                  
                    String fieldName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);                                        
                    String fieldValue=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);                    
                    String[] fieldNames=null;
                    Object[] fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    String programName=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME);
                    if (programName.length()==0)
                        programName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsEnvMonitData.Sample.FLD_PROGRAM_NAME.getName())].toString();
                    String locationName=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_LOCATION_NAME);                                        
                    if ((locationName==null) || (locationName.length())==0)
                        locationName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsEnvMonitData.Sample.FLD_LOCATION_NAME.getName())].toString();
                    Integer numSamplesToLog = 1;
                    String numSamplesToLogStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG);    
                    if (numSamplesToLogStr!=null){numSamplesToLog = Integer.parseInt(numSamplesToLogStr);}

                    if (numSamplesToLogStr==null){
                        dataSample = prgSmp.logProgramSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, locationName);
                    }else{
                        dataSample = prgSmp.logProgramSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, locationName);
                    }
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    messageDynamicData=new Object[]{dataSample[dataSample.length-1]};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), dataSample[dataSample.length-1]);                            
                    break;
                case ENTERRESULT:
                    Integer resultId = 0;
                    String rawValueResult = "";
                    String resultIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RESULT_ID);
                    resultId = Integer.parseInt(resultIdStr);       
                    rawValueResult = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RAW_VALUE_RESULT);
                    dataSample = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                    messageDynamicData=new Object[]{""};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), "");                            
                    break;             
                case ADD_SAMPLE_MICROORGANISM:
                    String sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    Integer sampleId = Integer.parseInt(sampleIdStr);       
                    String microorganismName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_MICROORGANISM_NAME);  
                    String[] microorganismNameArr=microorganismName.split("\\|");                     
                    for (String orgName: microorganismNameArr){
                      dataSample = DataProgramSample.addSampleMicroorganism(schemaPrefix, token, sampleId, orgName);
                    }
                    messageDynamicData=new Object[]{microorganismName.replace("\\|", ", "), sampleId};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);                                                
                    break;
                case EM_BATCH_INCUB_ADD_SMP:
                    String batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    String batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    String batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    
                    Integer positionRow=null;
                    String positionRowStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_ROW); 
                    if (positionRowStr!=null && positionRowStr.length()>0) positionRow=Integer.valueOf(positionRowStr);
                    Integer positionCol=null;
                    String positionColStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_COL); 
                    if (positionColStr!=null && positionColStr.length()>0) positionCol=Integer.valueOf(positionColStr);
                    Boolean positionOverride=false;
                    String positionOverrideStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_OVERRIDE); 
                    if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    
                    dataSample=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion)
                            , Integer.valueOf(sampleIdStr), positionRow, positionCol, positionOverride);
                    messageDynamicData=new Object[]{sampleIdStr, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleIdStr));
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case EM_BATCH_INCUB_MOVE_SMP:
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                    
                    positionRowStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_ROW); 
                    positionColStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_COL); 
                    positionOverride=false;
                    positionOverrideStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_OVERRIDE); 
                    if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    
                    dataSample=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion)
                            , Integer.valueOf(sampleIdStr), Integer.valueOf(positionRowStr), Integer.valueOf(positionColStr), positionOverride);
                    messageDynamicData=new Object[]{sampleIdStr, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleIdStr));
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case EM_BATCH_INCUB_REMOVE_SMP:
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    dataSample=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion), Integer.valueOf(sampleIdStr));
                    messageDynamicData=new Object[]{sampleIdStr, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleIdStr));
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case GETSAMPLEINFO2:
                    RequestDispatcher rd3 = request.getRequestDispatcher(SampleAPIParams.SERVLET_FRONTEND_URL);
                    rd3.forward(request,response);  
                    //messageDynamicData=new Object[]{sampleId};
                    //rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleId));                    
                    return;
                default:    
                    Rdbms.closeRdbms(); 
                    RequestDispatcher rd = request.getRequestDispatcher(SampleAPIParams.SERVLET_API_URL);
                    rd.forward(request,response);   
                    return;
            }    
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(dataSample[0].toString())){  
/*                Rdbms.rollbackWithSavePoint();
                if (!con.getAutoCommit()){
                    con.rollback();
                    con.setAutoCommit(true);}                */
                LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, dataSample);   
            }else{
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), messageDynamicData, rObj.getRelatedObject());
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
//                con.close();
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
