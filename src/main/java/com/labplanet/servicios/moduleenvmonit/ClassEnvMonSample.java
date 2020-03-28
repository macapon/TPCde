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
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());
        RelatedObjects rObj=RelatedObjects.getInstance();
        try {
            DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();
            DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();
            DataProgramSample prgSmp = new DataProgramSample();
            DataSample smp = new DataSample(prgSmpAna);               
            DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(prgSmpAnaRes);
            Object[] diagn = null;            
            switch (endPoint){
                case LOGSAMPLE:
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
                    Integer resultId = (Integer) argValues[0];
                    String rawValueResult = argValues[1].toString();
                    diagn = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                    dynamicDataObjects=new Object[]{""};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), "");
                    break;             
                case ADD_SAMPLE_MICROORGANISM: 
                    for (String orgName: (String[]) argValues[1].toString().split("\\|")){
                        diagn = DataProgramSample.addSampleMicroorganism(schemaPrefix, token, (Integer) argValues[0], orgName);
                        rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), TblsEnvMonitData.SampleMicroorganism.TBL.getName(), diagn[diagn.length-1]);
                    }
                    //dynamicDataObjects=new Object[]{microorganismName.replace("\\|", ", "), sampleId};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), argValues[0]);                                                
                    break;
                case EM_BATCH_INCUB_ADD_SMP:
                    String batchName = argValues[0].toString();
                    Integer batchTemplateId = (Integer) argValues[1];
                    Integer batchTemplateVersion = (Integer) argValues[2];
                    Integer sampleId = (Integer) argValues[3];   
                    Integer positionRow=null;
                    if (argValues.length>=5 && argValues[4]!=null && argValues[4].toString().length()>0) positionRow=(Integer) argValues[4];
                    Integer positionCol=null;
                    if (argValues.length>=6 && argValues[5]!=null && argValues[5].toString().length()>0)positionCol= (Integer) argValues[5];
                    
                    Boolean positionOverride=false;
                    if (argValues.length>=7 && argValues[6]!=null && argValues[6].toString().length()>0) {
                        String positionOverrideStr=argValues[6].toString();
                        if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    }
                    diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, batchTemplateId, batchTemplateVersion
                            , sampleId, positionRow, positionCol, positionOverride);
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
                        String positionOverrideStr=argValues[6].toString();
                        if (positionOverrideStr!=null && positionOverrideStr.length()>0) positionOverride=Boolean.valueOf(positionOverrideStr);
                    }
                   
                    diagn=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion)
                            , Integer.valueOf(sampleId), positionRow, positionCol, positionOverride);
                    dynamicDataObjects=new Object[]{sampleId, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
                case EM_BATCH_INCUB_REMOVE_SMP:
                    batchName = argValues[0].toString();
                    batchTemplateId = (Integer) argValues[1];
                    batchTemplateVersion = (Integer) argValues[2];
                    sampleId = (Integer) argValues[3];
                    diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, Integer.valueOf(batchTemplateId), Integer.valueOf(batchTemplateVersion), sampleId);
                    dynamicDataObjects=new Object[]{sampleId, batchName};
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), TblsEnvMonitData.Sample.TBL.getName(), sampleId);
                    rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), TblsEnvMonitData.IncubBatch.TBL.getName(), Integer.valueOf(batchName));
                    break;
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
