/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulesample;

import com.labplanet.servicios.app.GlobalAPIsParams;
import com.labplanet.servicios.modulesample.SampleAPIParams.SampleAPIEndpoints;
import databases.Rdbms;
import databases.TblsData;
import databases.TblsDataAudit;
import databases.Token;
import functionaljavaa.audit.SampleAudit;
import functionaljavaa.changeofcustody.ChangeOfCustody;
import functionaljavaa.modulesample.DataModuleSampleAnalysis;
import functionaljavaa.modulesample.DataModuleSampleAnalysisResult;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysis;
import functionaljavaa.samplestructure.DataSampleAnalysisResult;
import functionaljavaa.samplestructure.DataSampleIncubation;
import functionaljavaa.samplestructure.DataSampleStages;
import java.math.BigDecimal;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class ClassSample {
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
    public Boolean getFunctionFound() {
        return functionFound;
    }    
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    private Boolean functionFound=false;
    
    public ClassSample(HttpServletRequest request, Token token, String schemaPrefix, SampleAPIEndpoints endPoint){
        Object[] dynamicDataObjects=new Object[]{};
        RelatedObjects rObj=RelatedObjects.getInstance();
        String schemaDataName="";
        String language="";
        DataModuleSampleAnalysis smpAna = new DataModuleSampleAnalysis();
        DataModuleSampleAnalysisResult moduleSmpAnaRes = new DataModuleSampleAnalysisResult();
        DataSample smp = new DataSample(smpAna);
        DataSampleAnalysisResult smpAnaRes = new DataSampleAnalysisResult(moduleSmpAnaRes);
        
        Integer incubationStage=null;
        Integer sampleId = null;
        Object[] diagn = null;
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());        
        this.functionFound=true;
        switch (endPoint){
            case LOGSAMPLE:
                String sampleTemplate= argValues[0].toString();
                Integer sampleTemplateVersion = (Integer) argValues[1];
                String fieldName=argValues[2].toString();
                String fieldValue=argValues[3].toString();
                String[] fieldNames=null;
                Object[] fieldValues=null;
                if (fieldName!=null) fieldNames = fieldName.split("\\|");
                if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));
                
                Integer numSamplesToLog=(Integer) argValues[4];
                
                if (numSamplesToLog==null){
                    diagn = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues);
                }else{
                    diagn = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, numSamplesToLog);
                }
                dynamicDataObjects=new Object[]{diagn[diagn.length-1]};
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), diagn[diagn.length-1]);
                messageDynamicData=new Object[]{diagn[diagn.length-1]};
                break;
            case RECEIVESAMPLE:
                sampleId = (Integer) argValues[0];
                diagn = smp.sampleReception(schemaPrefix, token, sampleId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SETSAMPLINGDATE:
                sampleId = (Integer) argValues[0];
                diagn = smp.setSamplingDate(schemaPrefix, token, sampleId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case CHANGESAMPLINGDATE:
                sampleId = (Integer) argValues[0];
                Date newDate=(Date) argValues[1];
                diagn = smp.changeSamplingDate(schemaPrefix, token, sampleId, newDate);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLINGCOMMENTADD:
                sampleId = (Integer) argValues[0];
                String comment=null;
                comment = argValues[1].toString();
                diagn = smp.sampleReceptionCommentAdd(schemaPrefix, token, sampleId, comment);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLINGCOMMENTREMOVE:
                sampleId = (Integer) argValues[0];
                comment = argValues[1].toString();
                diagn = smp.sampleReceptionCommentRemove(schemaPrefix, token, sampleId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case INCUBATIONSTART:
                incubationStage=1;
            case INCUBATION2START:
                if (incubationStage==null) incubationStage=2;
                sampleId = (Integer) argValues[0];
                String incubName=argValues[1].toString();
                BigDecimal tempReading=null;
                diagn = DataSampleIncubation.setSampleStartIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case INCUBATIONEND:
                incubationStage=1;
            case INCUBATION2END:
                if (incubationStage==null) incubationStage=2;
                sampleId = (Integer) argValues[0];
                incubName= argValues[1].toString();
                tempReading=null;
                diagn = DataSampleIncubation.setSampleEndIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLEANALYSISADD:
                sampleId = (Integer) argValues[0];
                String[] fieldNameArr = null;
                Object[] fieldValueArr = null;
                fieldName = argValues[1].toString();
                fieldNameArr =fieldName.split("\\|");
                fieldValue = argValues[2].toString();
                fieldValueArr = fieldValue.split("\\|");
                fieldValueArr = LPArray.convertStringWithDataTypeToObjectArray((String[]) fieldValueArr);
                diagn = DataSampleAnalysis.sampleAnalysisAddtoSample(schemaPrefix, token, sampleId, fieldNameArr, fieldValueArr, null);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case ENTERRESULT:
                Integer resultId = (Integer) argValues[0];
                String rawValueResult = argValues[1].toString();
                diagn = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case REVIEWRESULT:
                Integer objectId = (Integer) argValues[0];
                String objectLevel = argValues[1].toString();
                sampleId = null; Integer testId = null; resultId = null;
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                //diagn=smp.sampleReview(schemaPrefix, token.getPersonName(), token.getUserRole(), sampleId, Integer.parseInt(token.getAppSessionId()));
                diagn = smpAnaRes.sampleResultReview(schemaPrefix, token, sampleId, testId, resultId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case CANCELRESULT:
                objectId = (Integer) argValues[0];
                objectLevel = argValues[1].toString();
                sampleId = null; testId = null; resultId = null;
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                diagn = smpAnaRes.sampleAnalysisResultCancel(schemaPrefix, token, sampleId, testId, resultId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case UNREVIEWRESULT:   // No break then will take the same logic than the next one
            case UNCANCELRESULT:
                objectId = (Integer) argValues[0];
                objectLevel = argValues[1].toString();
                sampleId = null; testId = null; resultId = null;
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_SAMPLE)){sampleId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_TEST)){testId = objectId;}
                if (objectLevel.equalsIgnoreCase(GlobalAPIsParams.REQUEST_PARAM_OBJECT_LEVEL_RESULT)){resultId = objectId;}
                diagn = smpAnaRes.sampleAnalysisResultUnCancel(schemaPrefix, token, sampleId, testId, resultId, smp);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case TESTASSIGNMENT:
                testId = (Integer) argValues[0];
                String newAnalyst = argValues[1].toString();
                diagn = DataSampleAnalysis.sampleAnalysisAssignAnalyst(schemaPrefix, token, testId, newAnalyst, smp);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case GETSAMPLEINFO:
                sampleId = (Integer) argValues[0];
                String sampleFieldToRetrieve = argValues[1].toString();
                
                String[] sampleFieldToRetrieveArr =sampleFieldToRetrieve.split("\\|");
                schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);
                
                String[] sortFieldsNameArr = null;
                String sortFieldsName = argValues[2].toString();
                if (! ((sortFieldsName==null) || (sortFieldsName.contains("undefined"))) ) {
                    sortFieldsNameArr = sortFieldsName.split("\\|");
                }else{   sortFieldsNameArr=null;}
                
                String diagnStr = Rdbms.getRecordFieldsByFilterJSON(schemaDataName, TblsData.Sample.TBL.getName(),
                        new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, sampleFieldToRetrieveArr, sortFieldsNameArr);
                if (diagnStr.contains(LPPlatform.LAB_FALSE)){
                    Object[] errMsg = LPFrontEnd.responseError(diagnStr.split("\\|"), language, schemaPrefix);
                    //response.sendError((int) errMsg[0], (String) errMsg[1]);
                }else{
                    LPFrontEnd.servletReturnSuccess(request, null, diagnStr);
                }
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                return;
            case COC_STARTCHANGE:
                objectId = (Integer) argValues[0];
                String custodianCandidate = argValues[1].toString();
                ChangeOfCustody coc = new ChangeOfCustody();
                Integer appSessionId=null;
                if (token.getAppSessionId()!=null){appSessionId=Integer.valueOf(token.getAppSessionId());}
                diagn = coc.cocStartChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), objectId, custodianCandidate, token);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case COC_CONFIRMCHANGE:
                sampleId = (Integer) argValues[0];
                String confirmChangeComment = argValues[1].toString();
                coc =  new ChangeOfCustody();
                diagn = coc.cocConfirmedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, confirmChangeComment);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case COC_ABORTCHANGE:
                sampleId = (Integer) argValues[0];
                String cancelChangeComment = argValues[1].toString();
                coc =  new ChangeOfCustody();
                diagn = coc.cocAbortedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, cancelChangeComment);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case LOGALIQUOT:
                sampleId = (Integer) argValues[0];
                fieldName=argValues[1].toString();
                fieldValue=argValues[2].toString();
                fieldNames=null;
                fieldValues=null;
                if (fieldName!=null) fieldNames = fieldName.split("\\|");
                if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));
                diagn = smp.logSampleAliquot(schemaPrefix, token, sampleId,
                        // sampleTemplate, sampleTemplateVersion,
                        fieldNames, fieldValues);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case LOGSUBALIQUOT:
                Integer aliquotId = (Integer) argValues[0];
                fieldName=argValues[1].toString();
                fieldValue=argValues[2].toString();
                fieldNames=null;
                fieldValues=null;
                if (fieldName!=null) fieldNames =  fieldName.split("\\|");
                if (fieldValue!=null) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));
                diagn = smp.logSampleSubAliquot(schemaPrefix, token, aliquotId,
                        // sampleTemplate, sampleTemplateVersion,
                        fieldNames, fieldValues);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLESTAGE_MOVETOPREVIOUS:
            case SAMPLESTAGE_MOVETONEXT:
                DataSampleStages smpStage=new DataSampleStages(schemaPrefix);
                if (!smpStage.isSampleStagesEnable()){
                    LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, null,
                            LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "STAGES_FUNCTIONALITY_NOT_ENABLE", new Object[]{"Samples", schemaPrefix}));
                    return;
                }
                sampleId = (Integer) argValues[0];
                String sampleStage = argValues[1].toString();
                String sampleStageNext = argValues[2].toString();
                if ((sampleStage==null) || (sampleStage=="undefined") || (sampleStage.length()==0)){
                    Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(),
                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                            new String[]{TblsData.Sample.FLD_CURRENT_STAGE.getName()});
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())){
                        LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, null, LPArray.array2dTo1d(sampleInfo));
                        return;
                    }
                    sampleStage=sampleInfo[0][0].toString();
                }
                if (SampleAPIEndpoints.SAMPLESTAGE_MOVETONEXT.getName().equalsIgnoreCase(endPoint.getName()))
                    diagn=smpStage.moveToNextStage(schemaPrefix, sampleId, sampleStage, sampleStageNext);
                if (SampleAPIEndpoints.SAMPLESTAGE_MOVETOPREVIOUS.getName().equalsIgnoreCase(endPoint.getName()))
                    diagn=smpStage.moveToPreviousStage(schemaPrefix, sampleId, sampleStage, sampleStageNext);
                String[] sampleFieldName=new String[]{TblsData.Sample.FLD_CURRENT_STAGE.getName(), TblsData.Sample.FLD_PREVIOUS_STAGE.getName()};
                Object[] sampleFieldValue=new Object[]{diagn[diagn.length-1], sampleStage};
                if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagn[0].toString())){
                    smpStage.dataSampleStagesTimingCapture(schemaPrefix, sampleId, sampleStage, DataSampleStages.SampleStageTimingCapturePhases.END.name());                                                         
                    smpStage.dataSampleStagesTimingCapture(schemaPrefix, sampleId, diagn[diagn.length-1].toString(), DataSampleStages.SampleStageTimingCapturePhases.START.toString());
                    diagn=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(),
                            sampleFieldName, 
                            sampleFieldValue,
                            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId});
                    String[] fieldsForAudit = LPArray.joinTwo1DArraysInOneOf1DString(sampleFieldName, sampleFieldValue, token.getUserName());
                    SampleAudit smpAudit = new SampleAudit();
                    smpAudit.sampleAuditAdd(schemaPrefix, endPoint.getName(), TblsData.Sample.TBL.getName(), sampleId, sampleId, null, null, fieldsForAudit, token, null);
                }
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLEAUDIT_SET_AUDIT_ID_REVIEWED:
                Integer auditId = (Integer) argValues[0];
                diagn=SampleAudit.sampleAuditSetAuditRecordAsReviewed(schemaPrefix, Integer.valueOf(auditId), token.getPersonName());
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsDataAudit.Sample.TBL.getName(), TblsDataAudit.Sample.TBL.getName(), auditId);
                messageDynamicData=new Object[]{auditId};
                break;
        }
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(diagn[0].toString())){
            DataSampleStages smpStage = new DataSampleStages(schemaPrefix);
            smpStage.dataSampleActionAutoMoveToNext(schemaPrefix, token, endPoint.getName().toUpperCase(), sampleId);
        }
        this.diagnostic=diagn;
        this.relatedObj=rObj;
        this.messageDynamicData=dynamicDataObjects;
        rObj.killInstance();
    }
    
}
