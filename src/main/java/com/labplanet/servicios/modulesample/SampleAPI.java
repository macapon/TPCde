/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulesample;

import com.labplanet.servicios.ClassPath;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPHttp;
import com.labplanet.servicios.app.GlobalAPIsParams;
import com.labplanet.servicios.modulesample.SampleAPIParams.SampleAPIEndpoints;
import databases.Rdbms;
import databases.TblsData;
import databases.TblsDataAudit;
import databases.Token;
import functionaljavaa.audit.SampleAudit;
import functionaljavaa.changeofcustody.ChangeOfCustody;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysis;
import functionaljavaa.modulesample.DataModuleSampleAnalysis;
import functionaljavaa.modulesample.DataModuleSampleAnalysisResult;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import functionaljavaa.samplestructure.DataSampleAnalysisResult;
import functionaljavaa.samplestructure.DataSampleIncubation;
import functionaljavaa.samplestructure.DataSampleStages;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
/**
 *
 * @author Administrator
 */
public class SampleAPI extends HttpServlet {
    
    private String propertyFileName = "";    

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        propertyFileName = ClassPath.getInstance().getConfigXmlPath(); //context.getInitParameter("jsfiles");
    }    
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

        String[] mandatoryParams = new String[]{""};
        Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, SampleAPIParams.MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
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
            Logger.getLogger(SampleAPI.class.getName()).log(Level.SEVERE, null, ex);        
/*        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(SampleAPI.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);    
        String schemaConfigName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG);    
        Rdbms.setTransactionId(schemaConfigName);
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
            
            DataModuleSampleAnalysis smpAna = new DataModuleSampleAnalysis();   
            DataModuleSampleAnalysisResult moduleSmpAnaRes = new DataModuleSampleAnalysisResult();   
            DataSample smp = new DataSample(smpAna);    
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(moduleSmpAnaRes);   
            Object[] dataSample = null;
            Integer incubationStage=null;
            Integer sampleId = null;

            SampleAPIEndpoints endPoint = null;
            Object[] actionDiagnoses = null;
            try{
                endPoint = SampleAPIEndpoints.valueOf(actionName.toUpperCase());
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
            RelatedObjects rObj=RelatedObjects.getInstance();
            Object[] messageDynamicData=new Object[]{};
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

                    Integer numSamplesToLog = 1;
                    String numSamplesToLogStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG);    
                    if (numSamplesToLogStr!=null){numSamplesToLog = Integer.parseInt(numSamplesToLogStr);}

                    if (numSamplesToLogStr==null){
                        dataSample = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues);
                    }else{
                        dataSample = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, numSamplesToLog);
                    }
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), dataSample[dataSample.length-1]);                            
                    messageDynamicData=new Object[]{dataSample[dataSample.length-1]};
                    break;
                case RECEIVESAMPLE:   
                    String sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);      
                    dataSample = smp.sampleReception(schemaPrefix, token, sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;
                case SETSAMPLINGDATE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                                     
                    sampleId = Integer.parseInt(sampleIdStr);      
                    dataSample = smp.setSamplingDate(schemaPrefix, token, sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;
                case CHANGESAMPLINGDATE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                                     
                    sampleId = Integer.parseInt(sampleIdStr);      
                    Date newDate=Date.valueOf(request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NEW_DATE));
                    dataSample = smp.changeSamplingDate(schemaPrefix, token, sampleId, newDate);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;       
                case SAMPLINGCOMMENTADD:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);      
                    String comment=null;                    
                    comment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_COMMENT); 
                    dataSample = smp.sampleReceptionCommentAdd(schemaPrefix, token, sampleId, comment);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};                    
                    break;       
                case SAMPLINGCOMMENTREMOVE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);      
                    comment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_COMMENT); 
                    dataSample = smp.sampleReceptionCommentRemove(schemaPrefix, token, sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;       
                case INCUBATIONSTART:
                  incubationStage=1;
                case INCUBATION2START:  
                    if (incubationStage==null) incubationStage=2;
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);                     
                    String incubName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_INCUBATOR_NAME);
                    BigDecimal tempReading=null;                    
                    dataSample = DataSampleIncubation.setSampleStartIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;       
                case INCUBATIONEND:
                    incubationStage=1;
                case INCUBATION2END:
                    if (incubationStage==null) incubationStage=2;
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);      
                    incubName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_INCUBATOR_NAME);
                    tempReading=null;
                    dataSample = DataSampleIncubation.setSampleEndIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};                    
                    break;       
                case SAMPLEANALYSISADD:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);       
                    String[] fieldNameArr = null;
                    Object[] fieldValueArr = null;
                    fieldName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);
                    fieldNameArr =fieldName.split("\\|");                                    
                    fieldValue = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);
                    fieldValueArr = fieldValue.split("\\|");                        
                    fieldValueArr = LPArray.convertStringWithDataTypeToObjectArray((String[]) fieldValueArr);
                    dataSample = DataSampleAnalysis.sampleAnalysisAddtoSample(schemaPrefix, token, sampleId, fieldNameArr, fieldValueArr, null); 
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};                    
                    break;              
                case ENTERRESULT:
                    Integer resultId = 0;
                    String rawValueResult = "";
                    String resultIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RESULT_ID);
                    resultId = Integer.parseInt(resultIdStr);       
                    rawValueResult = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RAW_VALUE_RESULT);
                    dataSample = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;              
                case REVIEWRESULT:
                    Integer objectId = 0;
                    String objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_ID);
                    objectId = Integer.parseInt(objectIdStr);     
                    String objectLevel = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL);
                    sampleId = null; Integer testId = null; resultId = null;
                    if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                    if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                    if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                    //dataSample=smp.sampleReview(schemaPrefix, token.getPersonName(), token.getUserRole(), sampleId, Integer.parseInt(token.getAppSessionId()));
                    dataSample = smpAnaRes.sampleResultReview(schemaPrefix, token, sampleId, testId, resultId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;                       
                case CANCELRESULT:
                    objectId = 0;
                    objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_ID);
                    objectId = Integer.parseInt(objectIdStr);     
                    objectLevel = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL);
                        sampleId = null; testId = null; resultId = null;
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                        dataSample = smpAnaRes.sampleAnalysisResultCancel(schemaPrefix, token, sampleId, testId, resultId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;   
                case UNREVIEWRESULT:   // No break then will take the same logic than the next one  
                case UNCANCELRESULT:
                    objectId = 0;
                    objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_ID);
                    objectId = Integer.parseInt(objectIdStr);     
                    objectLevel = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL);
                        sampleId = null; testId = null; resultId = null;
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                        if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                        dataSample = smpAnaRes.sampleAnalysisResultUnCancel(schemaPrefix, token, sampleId, testId, resultId, smp);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;       
                case TESTASSIGNMENT: 
                    objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_TEST_ID);
                    testId = Integer.parseInt(objectIdStr);     
                    String newAnalyst = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NEW_ANALYST);
                    dataSample = DataSampleAnalysis.sampleAnalysisAssignAnalyst(schemaPrefix, token, testId, newAnalyst, smp);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId}; 
                    break;                       
                case GETSAMPLEINFO:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.parseInt(sampleIdStr);                                               
                    String sampleFieldToRetrieve = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_TO_RETRIEVE);                                                                                     

                    String[] sampleFieldToRetrieveArr =sampleFieldToRetrieve.split("\\|");                           
                    schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);              

                    String[] sortFieldsNameArr = null;
                    String sortFieldsName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SORT_FIELDS_NAME); 
                    if (! ((sortFieldsName==null) || (sortFieldsName.contains("undefined"))) ) {
                        sortFieldsNameArr = sortFieldsName.split("\\|");                                    
                    }else{   sortFieldsNameArr=null;}  
                    
                    String dataSampleStr = Rdbms.getRecordFieldsByFilterJSON(schemaDataName, TblsData.Sample.TBL.getName(), 
                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, sampleFieldToRetrieveArr, sortFieldsNameArr);
                   if (dataSampleStr.contains(LPPlatform.LAB_FALSE)){                                 
                        Object[] errMsg = LPFrontEnd.responseError(dataSampleStr.split("\\|"), language, schemaPrefix);
                        response.sendError((int) errMsg[0], (String) errMsg[1]);        
                    }else{
                        LPFrontEnd.servletReturnSuccess(request, response, dataSampleStr);
                    }                  
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    return;        
                case COC_STARTCHANGE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    objectId = Integer.valueOf(sampleIdStr);
                    String custodianCandidate = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CUSTODIAN_CANDIDATE);                             
                    ChangeOfCustody coc = new ChangeOfCustody();
                    Integer appSessionId=null;
                    if (token.getAppSessionId()!=null){appSessionId=Integer.valueOf(token.getAppSessionId());}
                    dataSample = coc.cocStartChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), objectId, custodianCandidate, token);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;
                case COC_CONFIRMCHANGE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.valueOf(sampleIdStr);
                    String confirmChangeComment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CONFIRM_CHANGE_COMMENT);                             
                    coc =  new ChangeOfCustody();
                    dataSample = coc.cocConfirmedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, confirmChangeComment);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;
                case COC_ABORTCHANGE:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);                             
                    sampleId = Integer.valueOf(sampleIdStr);
                    String cancelChangeComment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CANCEL_CHANGE_COMMENT);                             
                    coc =  new ChangeOfCustody();
                    dataSample = coc.cocAbortedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, cancelChangeComment);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;                    
                case LOGALIQUOT:
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);              
                    sampleId = Integer.valueOf(sampleIdStr);                    
                    fieldName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);                                        
                    fieldValue=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    dataSample = smp.logSampleAliquot(schemaPrefix, token, sampleId, 
                                // sampleTemplate, sampleTemplateVersion, 
                                fieldNames, fieldValues);                                                                
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;                     
                case LOGSUBALIQUOT:
                    String aliquotIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ALIQUOT_ID);              
                    Integer aliquotId = Integer.valueOf(aliquotIdStr);
                    fieldName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);                                        
                    fieldValue=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);                    
                    fieldNames=null;
                    fieldValues=null;
                    if (fieldName!=null) fieldNames =  fieldName.split("\\|");                                            
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    dataSample = smp.logSampleSubAliquot(schemaPrefix, token, aliquotId, 
                                // sampleTemplate, sampleTemplateVersion, 
                                fieldNames, fieldValues);                                                                
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                    break;     
                case SAMPLESTAGE_MOVETOPREVIOUS:
                case SAMPLESTAGE_MOVETONEXT:
                  DataSampleStages smpStage=new DataSampleStages(schemaPrefix);
                  if (!smpStage.isSampleStagesEnable()){
                      LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, 
                              LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "STAGES_FUNCTIONALITY_NOT_ENABLE", new Object[]{"Samples", schemaPrefix}));
                     return;                   
                  }
                  sampleId = 0;
                  sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                  sampleId = Integer.parseInt(sampleIdStr);       
                  String sampleStage = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_STAGE);
                  String sampleStageNext = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_STAGE_NEXT);
                  if ((sampleStage==null) || (sampleStage=="undefined") || (sampleStage.length()==0)){
                    Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(),
                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                            new String[]{TblsData.Sample.FLD_CURRENT_STAGE.getName()});
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())){
                      LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, LPArray.array2dTo1d(sampleInfo));
                      return;
                    }
                    sampleStage=sampleInfo[0][0].toString();
                  }
                  if (SampleAPIEndpoints.SAMPLESTAGE_MOVETONEXT.getName().equalsIgnoreCase(actionName))
                    dataSample=smpStage.moveToNextStage(schemaPrefix, sampleId, sampleStage, sampleStageNext);
                  if (SampleAPIEndpoints.SAMPLESTAGE_MOVETOPREVIOUS.getName().equalsIgnoreCase(actionName))
                    dataSample=smpStage.moveToPreviousStage(schemaPrefix, sampleId, sampleStage, sampleStageNext);       
                  String[] sampleFieldName=new String[]{TblsData.Sample.FLD_CURRENT_STAGE.getName(), TblsData.Sample.FLD_PREVIOUS_STAGE.getName()};
                  Object[] sampleFieldValue=new Object[]{dataSample[dataSample.length-1], sampleStage};
                  if (LPPlatform.LAB_TRUE.equalsIgnoreCase(dataSample[0].toString())){
                    smpStage.DataSampleStagesTimingCapture(schemaPrefix, sampleId, sampleStage, 
                            DataSampleStages.SampleStageTimingCapturePhases.END.name());                                                         
                    smpStage.DataSampleStagesTimingCapture(schemaPrefix, sampleId, dataSample[dataSample.length-1].toString(), DataSampleStages.SampleStageTimingCapturePhases.START.toString());
                    dataSample=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                            sampleFieldName, 
                            sampleFieldValue,
                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
                              String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getUserName());
                    SampleAudit smpAudit = new SampleAudit();
                    smpAudit.sampleAuditAdd(schemaPrefix, actionName, TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
                  }
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
                    messageDynamicData=new Object[]{sampleId};
                  break;                    
                case SAMPLEAUDIT_SET_AUDIT_ID_REVIEWED:
                    String auditIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_AUDIT_ID);
                    dataSample=SampleAudit.sampleAuditSetAuditRecordAsReviewed(schemaPrefix, Integer.valueOf(auditIdStr), token.getPersonName());
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsDataAudit.Sample.TBL.getName(), TblsDataAudit.Sample.TBL.getName(), auditIdStr);                            
                    messageDynamicData=new Object[]{auditIdStr};
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
                DataSampleStages smpStage=new DataSampleStages(schemaPrefix);
                if (smpStage.isSampleStagesEnable() && (sampleId!=null))
                    smpStage.DataSampleActionAutoMoveToNext(schemaPrefix, token, actionName, sampleId);
                    
                JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(this.getClass().getSimpleName(), endPoint.getSuccessMessageCode(), messageDynamicData, rObj.getRelatedObject());
                rObj.killInstance();
                LPFrontEnd.servletReturnSuccess(request, response, dataSampleJSONMsg); 
                
                //JSONObject dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue(dataSample);
                //LPFrontEnd.servletReturnSuccess(request, response, dataSampleJSONMsg);
            }            
        }catch(Exception e){   
            LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_EXCEPTION_RAISED, new Object[]{e.getMessage(), this.getServletName()}, language);                   
 /*           try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(SampleAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
*/            
        } finally {
            // release database resources
            try {
//                con.close();
                Rdbms.closeRdbms();   
            } catch (Exception ignore) {
            }
        }                                       
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlLPFrontEnd on the + sign on the left to edit the code.">
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