/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class ClassEnvMonSample {

    /**
     * @return the messageDynamicData
     */
    public Object[] getMessageDynamicData() {
        return this.messageDynamicData;
    }

    /**
     * @return the rObj
     */
    public RelatedObjects getRelatedObj() {
        return this.relatedObj;
    }

    /**
     * @return the endpointExists
     */
    public Boolean getEndpointExists() {
        return this.endpointExists;
    }

    /**
     * @return the diagnostic
     */
    public Object[] getDiagnostic() {
        return this.diagnostic;
    }
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    
    public ClassEnvMonSample(HttpServletRequest request, Token token, String schemaPrefix, EnvMonSampleAPI.EnvMonSampleAPIEndpoints endPoint){
        Object[] dynamicDataObjects=new Object[]{};
        RelatedObjects rObj=RelatedObjects.getInstance();
        try {
            DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();
            DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();
            DataProgramSample prgSmp = new DataProgramSample();
            DataSample smp = new DataSample(prgSmpAna);               
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(prgSmpAnaRes);
            Object[] diagn = null;
            Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());
            switch (endPoint){
                case LOGSAMPLE:
/*                    String sampleTemplate=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE);
                        if (sampleTemplate==null) sampleTemplate=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE)).toString();
                    String sampleTemplateVersionStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE_VERSION);
                        if (sampleTemplateVersionStr==null) sampleTemplateVersionStr=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE_VERSION)).toString();
                    
                    Integer sampleTemplateVersion = Integer.parseInt(sampleTemplateVersionStr);
                    String fieldName=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME);
                        if (fieldName==null) fieldName=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME)).toString();
                    String fieldValue=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE);                    
                        if (fieldValue==null) fieldValue=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE)).toString();

                    String[] fieldNames=null;
                    Object[] fieldValues=null;
                    if (fieldName!=null) fieldNames = fieldName.split("\\|");
                    if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                            
                    String programName=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME);
                        if (programName==null) programName=LPNulls.replaceNull(request.getAttribute(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME)).toString();
                    if (programName.length()==0)
                        programName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsEnvMonitData.Sample.FLD_PROGRAM_NAME.getName())].toString();
                    String locationName=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_LOCATION_NAME); 
                        if (locationName==null) locationName=LPNulls.replaceNull(request.getAttribute(EnvMonitAPIParams.REQUEST_PARAM_LOCATION_NAME)).toString();
                    if ((locationName==null) || (locationName.length())==0)
                        locationName = fieldValues[LPArray.valuePosicInArray(fieldNames, TblsEnvMonitData.Sample.FLD_LOCATION_NAME.getName())].toString();
                    Integer numSamplesToLog = 1;
                    String numSamplesToLogStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG);
                        if (numSamplesToLogStr==null) numSamplesToLogStr=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG)).toString();
                    if (numSamplesToLogStr!=null){numSamplesToLog = Integer.parseInt(numSamplesToLogStr);}
*/
                    if (argValues[5]==null){
                        diagn = prgSmp.logProgramSample(schemaPrefix, token, (String) argValues[0], (Integer) argValues[1], (String[]) argValues[2].toString().split("\\|"), 
                                (Object[]) LPArray.convertStringWithDataTypeToObjectArray(argValues[3].toString().split("\\|")), (String) argValues[4], (String) argValues[5]);
                    }else{
                        diagn = prgSmp.logProgramSample(schemaPrefix, token, (String) argValues[0], (Integer) argValues[1], (String[]) argValues[2].toString().split("\\|"), 
                                (Object[]) LPArray.convertStringWithDataTypeToObjectArray(argValues[3].toString().split("\\|")), (String) argValues[4], (String) argValues[5]);
                    }
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    dynamicDataObjects=new Object[]{diagn[diagn.length-1]};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), diagn[diagn.length-1]);                            
                    break;
                case ENTERRESULT:
                    Integer resultId = 0;
                    String rawValueResult = "";
                    String resultIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RESULT_ID);
                    resultId = Integer.parseInt(resultIdStr);       
                    rawValueResult = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RAW_VALUE_RESULT);
                    diagn = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                    dynamicDataObjects=new Object[]{""};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), "");
                    break;             
                case ADD_SAMPLE_MICROORGANISM: 
/*                    String sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                        if (sampleIdStr==null) sampleIdStr=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID)).toString();
                    Integer sampleId = Integer.parseInt(sampleIdStr);
                    String microorganismName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_MICROORGANISM_NAME);
                        if (microorganismName==null) microorganismName=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_MICROORGANISM_NAME)).toString();*/
                    //String[] microorganismNameArr=argValues[1];                  
                    for (String orgName: (String[]) argValues[1].toString().split("\\|")){
                        diagn = DataProgramSample.addSampleMicroorganism(schemaPrefix, token, (Integer) argValues[0], orgName);
                        rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), diagn[diagn.length-1]);
                    }
                    //dynamicDataObjects=new Object[]{microorganismName.replace("\\|", ", "), sampleId};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), argValues[0]);                                                
                                                                    
                    break;
                case EM_BATCH_INCUB_ADD_SMP:
                    String batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    String batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    String batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    String sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    
                    Integer positionRow=null;
                    String positionRowStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_ROW); 
                    if (positionRowStr!=null && positionRowStr.length()>0) positionRow=Integer.valueOf(positionRowStr);
                    Integer positionCol=null;
                    String positionColStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_COL); 
                    if (positionColStr!=null && positionColStr.length()>0) positionCol=Integer.valueOf(positionColStr);
                    Boolean positionOverride=false;
                    String positionOverrideStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_POSITION_OVERRIDE); 
                    if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    
                    diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion)
                            , Integer.valueOf(sampleIdStr), positionRow, positionCol, positionOverride);
                    dynamicDataObjects=new Object[]{sampleIdStr, batchName};
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
                    
                    diagn=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion)
                            , Integer.valueOf(sampleIdStr), Integer.valueOf(positionRowStr), Integer.valueOf(positionColStr), positionOverride);
                    dynamicDataObjects=new Object[]{sampleIdStr, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleIdStr));
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case EM_BATCH_INCUB_REMOVE_SMP:
                    batchName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_NAME);
                    batchTemplateId = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_ID);
                    batchTemplateVersion = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_BATCH_TEMPLATE_VERSION);
                    sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion), Integer.valueOf(sampleIdStr));
                    dynamicDataObjects=new Object[]{sampleIdStr, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleIdStr));
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case GETSAMPLEINFO2:
                    this.endpointExists=false;
                    RequestDispatcher rd3 = request.getRequestDispatcher(SampleAPIParams.SERVLET_FRONTEND_URL);
                    rd3.forward(request,null);  
                    //messageDynamicData=new Object[]{sampleId};
                    //RelatedObjects.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), Integer.valueOf(sampleId));                    
                    return;
                default:
                    this.endpointExists=false;
                    Rdbms.closeRdbms(); 
                    RequestDispatcher rd = request.getRequestDispatcher(SampleAPIParams.SERVLET_API_URL);
                    rd.forward(request,null);   
            } 
        this.diagnostic=diagn;
        this.relatedObj=rObj;
        this.messageDynamicData=dynamicDataObjects;
        } catch (ServletException | IOException ex) {
            Logger.getLogger(ClassEnvMonSample.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            rObj.killInstance();
        }
    }
    
}
