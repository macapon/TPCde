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
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPNulls;
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
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    
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
        switch (endPoint){
            case LOGSAMPLE:
                String sampleTemplate=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE);
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
                
                Integer numSamplesToLog = 1;
                String numSamplesToLogStr=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG);
                    if (numSamplesToLogStr==null) numSamplesToLogStr=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG)).toString();
                if (numSamplesToLogStr!=null){numSamplesToLog = Integer.parseInt(numSamplesToLogStr);}
                
                if (numSamplesToLogStr==null){
                    diagn = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues);
                }else{
                    diagn = smp.logSample(schemaPrefix, token, sampleTemplate, sampleTemplateVersion, fieldNames, fieldValues, numSamplesToLog);
                }
                dynamicDataObjects=new Object[]{diagn[diagn.length-1]};
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), diagn[diagn.length-1]);
                messageDynamicData=new Object[]{diagn[diagn.length-1]};
                break;
            case RECEIVESAMPLE:
                String sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.parseInt(sampleIdStr);
                diagn = smp.sampleReception(schemaPrefix, token, sampleId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SETSAMPLINGDATE:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                    if (sampleIdStr==null) sampleIdStr=LPNulls.replaceNull(request.getAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID)).toString();                
                sampleId = Integer.parseInt(sampleIdStr);
                diagn = smp.setSamplingDate(schemaPrefix, token, sampleId);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case CHANGESAMPLINGDATE:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.parseInt(sampleIdStr);
                Date newDate=Date.valueOf(request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NEW_DATE));
                diagn = smp.changeSamplingDate(schemaPrefix, token, sampleId, newDate);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLINGCOMMENTADD:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.parseInt(sampleIdStr);
                String comment=null;
                comment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_COMMENT);
                diagn = smp.sampleReceptionCommentAdd(schemaPrefix, token, sampleId, comment);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case SAMPLINGCOMMENTREMOVE:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.parseInt(sampleIdStr);
                comment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_COMMENT);
                diagn = smp.sampleReceptionCommentRemove(schemaPrefix, token, sampleId);
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
                diagn = DataSampleIncubation.setSampleStartIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
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
                diagn = DataSampleIncubation.setSampleEndIncubationDateTime(schemaPrefix, token, sampleId, incubationStage, incubName, tempReading);
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
                diagn = DataSampleAnalysis.sampleAnalysisAddtoSample(schemaPrefix, token, sampleId, fieldNameArr, fieldValueArr, null);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case ENTERRESULT:
                Integer resultId = 0;
                String rawValueResult = "";
                String resultIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RESULT_ID);
                resultId = Integer.parseInt(resultIdStr);
                rawValueResult = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_RAW_VALUE_RESULT);
                diagn = smpAnaRes.sampleAnalysisResultEntry(schemaPrefix, token, resultId, rawValueResult, smp);
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
                //diagn=smp.sampleReview(schemaPrefix, token.getPersonName(), token.getUserRole(), sampleId, Integer.parseInt(token.getAppSessionId()));
                diagn = smpAnaRes.sampleResultReview(schemaPrefix, token, sampleId, testId, resultId);
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
                diagn = smpAnaRes.sampleAnalysisResultCancel(schemaPrefix, token, sampleId, testId, resultId);
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
                diagn = smpAnaRes.sampleAnalysisResultUnCancel(schemaPrefix, token, sampleId, testId, resultId, smp);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case TESTASSIGNMENT:
                objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_TEST_ID);
                testId = Integer.parseInt(objectIdStr);
                String newAnalyst = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NEW_ANALYST);
                diagn = DataSampleAnalysis.sampleAnalysisAssignAnalyst(schemaPrefix, token, testId, newAnalyst, smp);
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
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                objectId = Integer.valueOf(sampleIdStr);
                String custodianCandidate = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CUSTODIAN_CANDIDATE);
                ChangeOfCustody coc = new ChangeOfCustody();
                Integer appSessionId=null;
                if (token.getAppSessionId()!=null){appSessionId=Integer.valueOf(token.getAppSessionId());}
                diagn = coc.cocStartChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), objectId, custodianCandidate, token);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case COC_CONFIRMCHANGE:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.valueOf(sampleIdStr);
                String confirmChangeComment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CONFIRM_CHANGE_COMMENT);
                coc =  new ChangeOfCustody();
                diagn = coc.cocConfirmedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, confirmChangeComment);
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);
                messageDynamicData=new Object[]{sampleId};
                break;
            case COC_ABORTCHANGE:
                sampleIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID);
                sampleId = Integer.valueOf(sampleIdStr);
                String cancelChangeComment = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_CANCEL_CHANGE_COMMENT);
                coc =  new ChangeOfCustody();
                diagn = coc.cocAbortedChange(schemaPrefix, TblsData.Sample.TBL.getName(), TblsData.Sample.FLD_SAMPLE_ID.getName(), sampleId, token, cancelChangeComment);
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
                diagn = smp.logSampleAliquot(schemaPrefix, token, sampleId,
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
                String auditIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_AUDIT_ID);
                diagn=SampleAudit.sampleAuditSetAuditRecordAsReviewed(schemaPrefix, Integer.valueOf(auditIdStr), token.getPersonName());
                rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsDataAudit.Sample.TBL.getName(), TblsDataAudit.Sample.TBL.getName(), auditIdStr);
                messageDynamicData=new Object[]{auditIdStr};
                break;
        }
        this.diagnostic=diagn;
        this.relatedObj=rObj;
        this.messageDynamicData=dynamicDataObjects;
        rObj.killInstance();
    }
    
}
