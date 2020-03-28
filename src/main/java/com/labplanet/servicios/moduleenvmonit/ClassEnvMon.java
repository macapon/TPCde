/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import com.labplanet.servicios.moduleenvmonit.EnvMonAPI.EnvMonAPIEndpoints;
import databases.Token;
import functionaljavaa.batch.incubator.DataBatchIncubator;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramCorrectiveAction;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSample;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysis;
import functionaljavaa.moduleenvironmentalmonitoring.DataProgramSampleAnalysisResult;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class ClassEnvMon {
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    private Boolean functionFound=false;

    public ClassEnvMon(HttpServletRequest request, Token token, String schemaPrefix, EnvMonAPIEndpoints endPoint){
        Object[] dynamicDataObjects=new Object[]{};
        RelatedObjects rObj=RelatedObjects.getInstance();
        
        DataProgramSampleAnalysis prgSmpAna = new DataProgramSampleAnalysis();           
        DataProgramSampleAnalysisResult prgSmpAnaRes = new DataProgramSampleAnalysisResult();           
        DataProgramSample prgSmp = new DataProgramSample();     
        String batchName = "";
        String incubationName = "";
        
        String language="";
        Object[] actionDiagnoses = null;
        Integer incubationStage=null;
        Integer sampleId = null;
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());        
        this.functionFound=true;
            switch (endPoint){
                case CORRECTIVE_ACTION_COMPLETE:
                    String programName=argValues[0].toString();
                    Integer correctiveActionId = (Integer) argValues[1];                    
                    actionDiagnoses = DataProgramCorrectiveAction.markAsCompleted(schemaPrefix, token, correctiveActionId);
                    break;
                case EM_BATCH_INCUB_CREATE:    
                    batchName = argValues[0].toString();
                    Integer batchTemplateId = (Integer) argValues[1];
                    Integer batchTemplateVersion = (Integer) argValues[2];
                    String fieldName=argValues[3].toString();
                    String fieldValue=argValues[4].toString();
                    String[] fieldNames=new String[0];
                    Object[] fieldValues=new Object[0];
                    if (fieldName!=null && fieldName.length()>0) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null && fieldValue.length()>0) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    actionDiagnoses= DataBatchIncubator.createBatch(schemaPrefix, token, batchName, batchTemplateId, batchTemplateVersion, fieldNames, fieldValues);
                    batchName=actionDiagnoses[actionDiagnoses.length-1].toString();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator_batch", batchName);                
                    messageDynamicData=new Object[]{batchName};
                    break;                    
                case EM_BATCH_ASSIGN_INCUB: 
                    batchName = argValues[0].toString();
                    incubationName = argValues[1].toString();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator", incubationName);                
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator_batch", batchName);                
                    messageDynamicData=new Object[]{incubationName, batchName};
                    actionDiagnoses=DataBatchIncubator.batchAssignIncubator(schemaPrefix, token, batchName, incubationName);
                    break;
                case EM_BATCH_UPDATE_INFO: 
                    batchName = argValues[0].toString();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator_batch", batchName);                
                    fieldName = argValues[1].toString();
                    String[] fieldsName = fieldName.split("\\|");
                    fieldValue = argValues[2].toString();
                    Object[] fieldsValue= LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));
                    actionDiagnoses=DataBatchIncubator.batchUpdateInfo(schemaPrefix, token, batchName, fieldsName, fieldsValue);
                    messageDynamicData=new Object[]{incubationName, batchName};
                    break;
                case EM_BATCH_INCUB_START:
                    batchName = argValues[0].toString();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator_batch", batchName);                
                    batchTemplateId = (Integer) argValues[1];
                    batchTemplateVersion = (Integer) argValues[2];
                    String incubName=null;
                    actionDiagnoses=DataBatchIncubator.batchStarted(schemaPrefix, token, batchName, incubName, batchTemplateId, batchTemplateVersion);
                    messageDynamicData=new Object[]{incubationName, batchName};
                    break;                    
                case EM_BATCH_INCUB_END:
                    batchName = argValues[0].toString();
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsEnvMonitData.IncubBatch.TBL.getName(), "incubator_batch", batchName);                
                    batchTemplateId = (Integer) argValues[1];
                    batchTemplateVersion = (Integer) argValues[2];
                    incubName=null;
                    actionDiagnoses=DataBatchIncubator.batchEnded(schemaPrefix, token, batchName, incubName, batchTemplateId, batchTemplateVersion);
                    messageDynamicData=new Object[]{incubationName, batchName};
                    break;
                case EM_LOGSAMPLE_SCHEDULER:
                    LocalDateTime dateStart=(LocalDateTime) argValues[0];
//                    if (dateStartStr!=null) dateStart=LPDate.dateStringFormatToLocalDateTime(dateStartStr);
                    LocalDateTime dateEnd=(LocalDateTime) argValues[1];
//                    if (dateEndStr!=null) dateEnd=LPDate.dateStringFormatToLocalDateTime(dateEndStr);
                    programName = argValues[2].toString();
  //                  programName=null;
  //                  if (programNameStr!=null) programName=programNameStr;
                    actionDiagnoses=prgSmp.logProgramSampleScheduled(schemaPrefix, token, programName, dateStart, dateEnd);
                    messageDynamicData=new Object[]{};
                    break;
            }    
        this.diagnostic=actionDiagnoses;
        this.relatedObj=rObj;
        this.messageDynamicData=dynamicDataObjects;
        rObj.killInstance();
    }
    
    /**
     * @return the messageDynamicData
     */
    public Object[] getMessageDynamicData() {
        return messageDynamicData;
    }

    /**
     * @return the relatedObj
     */
    public RelatedObjects getRelatedObj() {
        return relatedObj;
    }

    /**
     * @return the endpointExists
     */
    public Boolean getEndpointExists() {
        return endpointExists;
    }

    /**
     * @return the diagnostic
     */
    public Object[] getDiagnostic() {
        return diagnostic;
    }

    /**
     * @return the functionFound
     */
    public Boolean getFunctionFound() {
        return functionFound;
    }
    
}