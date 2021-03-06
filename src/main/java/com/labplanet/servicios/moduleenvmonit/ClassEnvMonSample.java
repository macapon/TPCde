/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import com.labplanet.servicios.modulesample.SampleAPIParams;
import databases.Rdbms;
import databases.TblsData;
import databases.Token;
import functionaljavaa.batch.incubator.DataBatchIncubator;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSample;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysis;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysisResult;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysisResult;
import functionaljavaa.samplestructure.DataSampleStages;
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
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());
        RelatedObjects rObj=RelatedObjects.getInstance();
        try {
            DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();
            DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();
            DataProgramSample prgSmp = new DataProgramSample();
            DataSample smp = new DataSample(prgSmpAna);               
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(prgSmpAnaRes);
            Object[] actionDiagnoses = null;  
            Integer sampleId=null;
            Integer resultId = null;
            switch (endPoint){
                case LOGSAMPLE:
                    String[] fieldNames=null;
                    Object[] fieldValues=null;
                    if (LPNulls.replaceNull(argValues[2]).toString().length()>0){
                        fieldNames=(String[]) argValues[2].toString().split("\\|");
                        fieldValues=(Object[]) LPArray.convertStringWithDataTypeToObjectArray(argValues[3].toString().split("\\|"));
                    }
                    if (argValues[5]==null){
                        actionDiagnoses = prgSmp.logProgramSample(schemaPrefix, token, (String) argValues[0], (Integer) argValues[1], 
                            fieldNames, fieldValues, (String) argValues[4], (String) argValues[5]);
                    }else{
                        actionDiagnoses = prgSmp.logProgramSample(schemaPrefix, token, (String) argValues[0], (Integer) argValues[1], 
                            fieldNames, fieldValues, (String) argValues[4], (String) argValues[5]);
                    }
                    //logProgramSamplerSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, programName, programLocation);
                    dynamicDataObjects=new Object[]{actionDiagnoses[actionDiagnoses.length-1]};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), actionDiagnoses[actionDiagnoses.length-1]);                                                
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString())){
                        sampleId=Integer.valueOf(actionDiagnoses[actionDiagnoses.length-1].toString());
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{argValues[0], schemaPrefix});                    
                    }
                    break;
                case ENTERRESULT:
                    resultId = (Integer) argValues[0];
                    String rawValueResult = argValues[1].toString();
                    actionDiagnoses = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAnalysisResult.TBL.getName(), TblsData.SampleAnalysisResult.TBL.getName(), resultId);
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString())){
                    Object[][] resultInfo=new Object[0][0];
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{resultId, rawValueResult, schemaPrefix});                    
                        resultInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAnalysisResult.TBL.getName(), 
                                new String[]{TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName()}, new Object[]{resultId}, new String[]{TblsData.SampleAnalysisResult.FLD_SAMPLE_ID.getName()});
                        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(resultInfo[0][0].toString())) sampleId=Integer.valueOf(resultInfo[0][0].toString());
                        dynamicDataObjects=new Object[]{resultInfo[0][0].toString()};
                        rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), resultInfo[0][0]);
                    }
                    break;             
                case ADD_SAMPLE_MICROORGANISM: 
                    sampleId=(Integer) argValues[0];
                    for (String orgName: (String[]) argValues[1].toString().split("\\|")){
                        actionDiagnoses = DataProgramSample.addSampleMicroorganism(schemaPrefix, token, (Integer) argValues[0], orgName);
                        rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), actionDiagnoses[actionDiagnoses.length-1]);
                    }
                    if (actionDiagnoses!=null &&  LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{argValues[0], argValues[1], schemaPrefix});                    
                    dynamicDataObjects=new Object[]{argValues[1].toString().replace("\\|", ", "), sampleId};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), argValues[0]);                                                
                    break;
                case EM_BATCH_INCUB_ADD_SMP:
                    String batchName = argValues[0].toString();
                    Integer batchTemplateId = (Integer) argValues[1];
                    Integer batchTemplateVersion = (Integer) argValues[2];
                    sampleId = (Integer) argValues[3];   
                    Integer positionRow=null;
                    if (argValues.length>=5 && argValues[4]!=null && argValues[4].toString().length()>0) positionRow=(Integer) argValues[4];
                    Integer positionCol=null;
                    if (argValues.length>=6 && argValues[5]!=null && argValues[5].toString().length()>0)positionCol= (Integer) argValues[5];
                    
                    Boolean positionOverride=false;
                    if (argValues.length>=7 && argValues[6]!=null && argValues[6].toString().length()>0) {
                        String positionOverrideStr=argValues[6].toString();
                        if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    }
                    actionDiagnoses=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, batchTemplateId, batchTemplateVersion
                            , sampleId, positionRow, positionCol, positionOverride);
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{sampleId, batchName, schemaPrefix});                                        
                    dynamicDataObjects=new Object[]{sampleId, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), batchName);
                    break;
                case EM_BATCH_INCUB_MOVE_SMP:
                    batchName = argValues[0].toString();
                    batchTemplateId = (Integer) argValues[1];
                    batchTemplateVersion = (Integer) argValues[2];
                    sampleId = (Integer) argValues[3];
                    positionRow=null;
                    if (argValues.length>=5) positionRow=(Integer) argValues[4];
                    positionCol=null;
                    if (argValues.length>=6)positionCol= (Integer) argValues[5];
                    
                    positionOverride=false;
                    if (argValues.length>=7) {
                        Object positionOverrideStr=argValues[6];
                        if (positionOverrideStr!=null && LPNulls.replaceNull(positionOverrideStr).toString().length()>0) positionOverride=Boolean.valueOf(positionOverrideStr.toString());
                    }                   
                    actionDiagnoses=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, batchTemplateId, batchTemplateVersion, sampleId, positionRow, positionCol, positionOverride);
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{sampleId, batchName, schemaPrefix});                                        
                    dynamicDataObjects=new Object[]{sampleId, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), batchName);
                    break;
                case EM_BATCH_INCUB_REMOVE_SMP:
                    batchName = argValues[0].toString();
                    batchTemplateId = (Integer) argValues[1];
                    batchTemplateVersion = (Integer) argValues[2];
                    sampleId = (Integer) argValues[3];
                    actionDiagnoses=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, batchTemplateId, batchTemplateVersion, sampleId);
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{sampleId, batchName, schemaPrefix});                                        
                    dynamicDataObjects=new Object[]{sampleId, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), batchName);
                    break;
                default:
                    this.endpointExists=false;
                    Rdbms.closeRdbms(); 
                    RequestDispatcher rd = request.getRequestDispatcher(SampleAPIParams.SERVLET_API_URL);
                    rd.forward(request,null);   
            }             
            if (actionDiagnoses!=null && LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString())){
                DataSampleStages smpStage = new DataSampleStages(schemaPrefix);
                smpStage.dataSampleActionAutoMoveToNext(schemaPrefix, token, endPoint.getName().toUpperCase(), sampleId);
            }           
            this.diagnostic=actionDiagnoses;
            this.relatedObj=rObj;
            this.messageDynamicData=dynamicDataObjects;
        } catch (ServletException | IOException ex) {
            Logger.getLogger(ClassEnvMonSample.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            rObj.killInstance();
        }
    }
    
}
