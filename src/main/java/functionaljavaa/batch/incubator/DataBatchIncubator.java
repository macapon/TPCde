/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.batch.incubator;

import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitConfig;
import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitData;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.samplestructure.DataSampleIncubation;
import functionaljavaa.audit.IncubBatchAudit;
import java.math.BigDecimal;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;



/**
 *
 * @author User
 */
public class DataBatchIncubator {
    
    /**
     *
     */
    public enum BatchIncubatorType{

        /**
         *
         */
        STRUCTURED, 

        /**
         *
         */
        UNSTRUCTURED};
    enum BatchIncubatorMoments{START, END};
    enum BatchAuditEvents{BATCH_CREATED, BATCH_UPDATED, BATCH_STARTED, BATCH_ENDED, BATCH_SAMPLE_ADDED, BATCH_SAMPLE_MOVED, BATCH_SAMPLE_REMOVED, BATCH_SAMPLE_REMOVED_BY_OVERRIDE, BATCH_ASSIGN_INCUBATOR};
//    enum BatchIncubatorUpdateFieldsNotAllowed{a("f"), b("f")};//    enum BatchIncubatorUpdateFieldsNotAllowed{a("f"), b("f")};

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param bTemplateId
     * @param bTemplateVersion
     * @param fldName
     * @param fldValue
     * @return
     */

    public static Object[] createBatch(String schemaPrefix, Token token, String bName, Integer bTemplateId, Integer bTemplateVersion, String[] fldName, Object[] fldValue){
        Object[][] templateInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_CONFIG_ID.getName(), TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_VERSION.getName()}, 
                new Object[]{bTemplateId, bTemplateVersion}, new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(templateInfo[0][0].toString()))
            return LPArray.array2dTo1d(templateInfo);
        if (!Boolean.valueOf(templateInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Template <*1*> and version <*2*> is not active", new Object[]{bTemplateId, bTemplateVersion});

        String batchType=templateInfo[0][1].toString();
        Object[] batchTypeCheckerDiagn= createBatchTypeChecker(schemaPrefix, batchType, bName, bTemplateId, bTemplateVersion, fldName, fldValue);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeCheckerDiagn[0].toString())) return batchTypeCheckerDiagn;
        
        if (batchType.equalsIgnoreCase(BatchIncubatorType.UNSTRUCTURED.toString())){ 
            return DataBatchIncubatorUnstructured.createBatchUnstructured(schemaPrefix, token, bName, bTemplateId, bTemplateVersion, fldName, fldValue);
        }else if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) return DataBatchIncubatorStructured.createBatchStructured(schemaPrefix, token, bName, bTemplateId, bTemplateVersion, fldName, fldValue);
        else
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized", new Object[]{batchType});         
    }
    
    private static Object[] createBatchTypeChecker(String schemaPrefix, String batchType, String bName, Integer bTemplateId, Integer bTemplateVersion, String[] fldName, Object[] fldValue){
        Object[] batchTypeExist=batchTypeExists(batchType);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeExist[0].toString())) return batchTypeExist;
        
        return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "createBatch Not implemented yet", null);        
    }
    
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param bTemplateId
     * @param bTemplateVersion
     * @param sampleId
     * @return
     */
    public static Object[] batchAddSample(String schemaPrefix, Token token, String bName, Integer bTemplateId, Integer bTemplateVersion, Integer sampleId){
        return batchAddSample(schemaPrefix, token, bName, bTemplateId, bTemplateVersion, sampleId, null, null, null);
    }

    public static Object[] batchAddSample(String schemaPrefix, Token token, String bName, Integer bTemplateId, Integer bTemplateVersion, Integer sampleId, Integer row, Integer col, Boolean override){
        Object[][] templateInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_CONFIG_ID.getName(), TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_VERSION.getName()}, 
                new Object[]{bTemplateId, bTemplateVersion}, new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(templateInfo[0][0].toString()))
            return LPArray.array2dTo1d(templateInfo);
        if (!Boolean.valueOf(templateInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Template <*1*> and version <*2*> is not active", new Object[]{bTemplateId, bTemplateVersion});

        String batchType=templateInfo[0][1].toString();
        Object[] batchTypeExist=batchTypeExists(batchType);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeExist[0].toString())) return batchTypeExist;
        String[] sampleInfoFieldsToRetrieve=new String[]{TblsEnvMonitData.Sample.FLD_INCUBATION_PASSED.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_PASSED.getName(),                    
                    TblsEnvMonitData.Sample.FLD_INCUBATION_BATCH.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_BATCH.getName()};
        Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                new String[]{TblsEnvMonitData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                sampleInfoFieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString()))
            return LPArray.array2dTo1d(sampleInfo);
        Integer pendingIncubationStage=samplePendingBatchStage(sampleId, sampleInfoFieldsToRetrieve, sampleInfo[0]);
        if (pendingIncubationStage==-1) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "There is no pending incubation for sample <*1*> in procedure <*2*>", new Object[]{sampleId, schemaPrefix});
        Object[] smpIsBatchable=sampleIncubStageIsBatchable(schemaPrefix, sampleId, pendingIncubationStage, sampleInfoFieldsToRetrieve, sampleInfo[0]);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(smpIsBatchable[0].toString())) return smpIsBatchable;        
        
        if (batchType.equalsIgnoreCase(BatchIncubatorType.UNSTRUCTURED.toString())){ 
            return DataBatchIncubatorUnstructured.batchAddSampleUnstructured(schemaPrefix, token, bName, sampleId, pendingIncubationStage);
        }else if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) return DataBatchIncubatorStructured.batchAddSampleStructured(schemaPrefix, token, bName, sampleId, pendingIncubationStage, row, col, override);
        else
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized", new Object[]{batchType}); 
    }

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param bTemplateId
     * @param bTemplateVersion
     * @param sampleId
     * @return
     */
    public static Object[] batchMoveSample(String schemaPrefix, Token token, String bName, Integer bTemplateId, Integer bTemplateVersion, Integer sampleId, Integer newRow, Integer newCol, Boolean override){
        Object[][] templateInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_CONFIG_ID.getName(), TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_VERSION.getName()}, 
                new Object[]{bTemplateId, bTemplateVersion}, new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(templateInfo[0][0].toString()))
            return LPArray.array2dTo1d(templateInfo);
        if (!Boolean.valueOf(templateInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Template <*1*> and version <*2*> is not active", new Object[]{bTemplateId, bTemplateVersion});
        String[] sampleInfoFieldsToRetrieve=new String[]{TblsEnvMonitData.Sample.FLD_INCUBATION_PASSED.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_PASSED.getName(),                    
                    TblsEnvMonitData.Sample.FLD_INCUBATION_BATCH.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_BATCH.getName()};
        Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                new String[]{TblsEnvMonitData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                sampleInfoFieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString()))
            return LPArray.array2dTo1d(sampleInfo);
        Integer pendingIncubationStage=samplePendingBatchStage(sampleId, sampleInfoFieldsToRetrieve, sampleInfo[0]);
        if (pendingIncubationStage==-1) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "There is no pending incubation for sample <*1*> in procedure <*2*>", new Object[]{sampleId, schemaPrefix});

        String batchType=templateInfo[0][1].toString();
        Object[] batchTypeExist=batchTypeExists(batchType);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeExist[0].toString())) return batchTypeExist;
        if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) return DataBatchIncubatorStructured.batchMoveSampleStructured(schemaPrefix, token, bName, sampleId, pendingIncubationStage, newRow, newCol, override);
                else
                    return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized for Batch Movement", new Object[]{batchType});         
    }
    
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param bTemplateId
     * @param bTemplateVersion
     * @param sampleId
     * @return
     */
    public static Object[] batchRemoveSample(String schemaPrefix, Token token, String bName, Integer bTemplateId, Integer bTemplateVersion, Integer sampleId){
        Object[][] templateInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_CONFIG_ID.getName(), TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_VERSION.getName()}, 
                new Object[]{bTemplateId, bTemplateVersion}, new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(templateInfo[0][0].toString()))
            return LPArray.array2dTo1d(templateInfo);
        if (!Boolean.valueOf(templateInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Template <*1*> and version <*2*> is not active", new Object[]{bTemplateId, bTemplateVersion});

        String batchType=templateInfo[0][1].toString();
        Object[] batchTypeExist=batchTypeExists(batchType);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeExist[0].toString())) return batchTypeExist;

        String[] sampleInfoFieldsToRetrieve=new String[]{TblsEnvMonitData.Sample.FLD_INCUBATION_PASSED.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_PASSED.getName(),                    
                    TblsEnvMonitData.Sample.FLD_INCUBATION_BATCH.getName(), TblsEnvMonitData.Sample.FLD_INCUBATION2_BATCH.getName()};
        Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                new String[]{TblsEnvMonitData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
                sampleInfoFieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString()))
            return LPArray.array2dTo1d(sampleInfo);        
        Integer pendingIncubationStage=samplePendingBatchStage(sampleId, sampleInfoFieldsToRetrieve, sampleInfo[0]);
        if (pendingIncubationStage==-1) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "There is no pending incubation for sample <*1*> in procedure <*2*>", new Object[]{sampleId, schemaPrefix});

        if (batchType.equalsIgnoreCase(BatchIncubatorType.UNSTRUCTURED.toString())) 
            return DataBatchIncubatorUnstructured.batchRemoveSampleUnstructured(schemaPrefix, token, bName, sampleId);
        else if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) return DataBatchIncubatorStructured.batchRemoveSampleStructured(schemaPrefix, token, bName, sampleId, pendingIncubationStage);
        else
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized", new Object[]{batchType}); 
        
    }
    
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param incubName
     * @param bTemplateId
     * @param bTemplateVersion
     * @return
     */
    public static Object[] batchStarted(String schemaPrefix, Token token, String bName, String incubName, Integer bTemplateId, Integer bTemplateVersion){
        return batchMomentMarked(schemaPrefix, token, bName, incubName, bTemplateId, bTemplateVersion, BatchIncubatorMoments.START.toString());
    }
    
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param bName
     * @param incubName
     * @param bTemplateId
     * @param bTemplateVersion
     * @return
     */
    public static Object[] batchEnded(String schemaPrefix, Token token, String bName, String incubName, Integer bTemplateId, Integer bTemplateVersion){
        return batchMomentMarked(schemaPrefix, token, bName, incubName, bTemplateId, bTemplateVersion, BatchIncubatorMoments.END.toString());
    }
    
    private static Object[] batchMomentMarked(String schemaPrefix, Token token, String bName, String incubName, Integer bTemplateId, Integer bTemplateVersion, String moment){
        Object[][] templateInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_CONFIG_ID.getName(), TblsEnvMonitConfig.IncubBatch.FLD_INCUB_BATCH_VERSION.getName()}, 
                new Object[]{bTemplateId, bTemplateVersion}, new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(templateInfo[0][0].toString()))
            return LPArray.array2dTo1d(templateInfo);
        if (!Boolean.valueOf(templateInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Template <*1*> and version <*2*> is not active", new Object[]{bTemplateId, bTemplateVersion});
        if (incubName==null){
            Object[][] batchInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), 
                    new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{bName}, 
                    new String[]{TblsEnvMonitData.IncubBatch.FLD_INCUBATION_INCUBATOR.getName()});            
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchInfo[0][0].toString()))
                return LPArray.array2dTo1d(batchInfo);
            incubName=batchInfo[0][0].toString();
        }
        String batchType=templateInfo[0][1].toString();
        Object[] batchTypeExist=batchTypeExists(batchType);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchTypeExist[0].toString())) return batchTypeExist;
        Object[] batchSampleIncubationMomentMarkedDiagn= new Object[0];

        if (BatchIncubatorMoments.START.toString().equalsIgnoreCase(moment)){
            if (batchType.equalsIgnoreCase(BatchIncubatorType.UNSTRUCTURED.toString())) 
                batchSampleIncubationMomentMarkedDiagn=DataBatchIncubatorUnstructured.batchSampleIncubStartedUnstructured(schemaPrefix, token, bName, incubName);
            else if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) 
                batchSampleIncubationMomentMarkedDiagn=DataBatchIncubatorStructured.batchSampleIncubStartedStructured();
            else
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized", new Object[]{batchType}); 
        }else if (BatchIncubatorMoments.END.toString().equalsIgnoreCase(moment)){
            if (batchType.equalsIgnoreCase(BatchIncubatorType.UNSTRUCTURED.toString())) 
                batchSampleIncubationMomentMarkedDiagn=DataBatchIncubatorUnstructured.batchSampleIncubEndedUnstructured(schemaPrefix, token, bName, incubName);
            else if (batchType.equalsIgnoreCase(BatchIncubatorType.STRUCTURED.toString())) 
                batchSampleIncubationMomentMarkedDiagn=DataBatchIncubatorStructured.batchSampleIncubEndedStructured();
            else
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "batchType <*1*> Not recognized", new Object[]{batchType}); 
        } else return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The moment <*1*> is not declared in BatchIncubatorMoments", new Object[]{moment});
        

        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchSampleIncubationMomentMarkedDiagn[0].toString())) return batchSampleIncubationMomentMarkedDiagn;
        String[] requiredFields = new String[0];
        Object[] requiredFieldsValue= new Object[0];
        String batchAuditEvent="";
        if (BatchIncubatorMoments.START.toString().equalsIgnoreCase(moment)){
            requiredFields = new String[]{TblsEnvMonitData.IncubBatch.FLD_INCUBATION_INCUBATOR.getName(), TblsEnvMonitData.IncubBatch.FLD_INCUBATION_START.getName()};
            requiredFieldsValue= new Object[]{incubName, LPDate.getCurrentTimeStamp()}; 
            batchAuditEvent=BatchAuditEvents.BATCH_STARTED.toString();
        }else if (BatchIncubatorMoments.END.toString().equalsIgnoreCase(moment)){
            requiredFields = new String[]{TblsEnvMonitData.IncubBatch.FLD_INCUBATION_END.getName(), TblsEnvMonitData.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitData.IncubBatch.FLD_COMPLETED.getName()};
            requiredFieldsValue= new Object[]{LPDate.getCurrentTimeStamp(), false, true};                
            batchAuditEvent=BatchAuditEvents.BATCH_ENDED.toString();
        } else return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The moment <*1*> is not declared in BatchIncubatorMoments", new Object[]{moment});
        
        Object[] updateDiagnostic=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), 
                requiredFields, requiredFieldsValue, 
                new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{bName});
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateDiagnostic[0].toString()))
            IncubBatchAudit.IncubBatchAuditAdd(schemaPrefix, token, batchAuditEvent, TblsEnvMonitData.IncubBatch.TBL.getName(), bName, bName,
                LPArray.joinTwo1DArraysInOneOf1DString(requiredFields, requiredFieldsValue, ": "), null);
        return updateDiagnostic;
    }
    
    private static Object[] batchTypeExists(String batchType){
        Boolean typeExists = false;
        BatchIncubatorType arr[] = BatchIncubatorType.values();
        for (BatchIncubatorType curType: arr){
            if (batchType.equalsIgnoreCase(curType.toString())){
                typeExists=true;
                break;
            }
        }
        if (typeExists)return new Object[]{LPPlatform.LAB_TRUE};
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "Batch type <*1*> not recognized to create incubation batches", new Object[]{batchType});        
    }
    
    private static Integer samplePendingBatchStage(Integer sampleId, String[] fieldsName, Object[] fieldsValue){
        Integer posic = LPArray.valuePosicInArray(fieldsName, TblsEnvMonitData.Sample.FLD_INCUBATION_PASSED.getName());
        if (posic==-1) return posic;
        if ((fieldsValue[posic]==null) || (!Boolean.valueOf(fieldsValue[posic].toString())) ) return 1;

        posic = LPArray.valuePosicInArray(fieldsName, TblsEnvMonitData.Sample.FLD_INCUBATION2_PASSED.getName());
        if (posic==-1) return posic;
        if ((fieldsValue[posic]==null) || (!Boolean.valueOf(fieldsValue[posic].toString())) ) return 2;
        return -1;
    }
    
    private static Object[] sampleIncubStageIsBatchable(String schemaPrefix, Integer sampleId, Integer incubStage, String[] fieldsName, Object[] fieldsValue){
        String batchFldName="";
        if (incubStage==1)
            batchFldName=TblsEnvMonitData.Sample.FLD_INCUBATION_BATCH.getName();         
        else if (incubStage==2)
            batchFldName=TblsEnvMonitData.Sample.FLD_INCUBATION2_BATCH.getName();
        else
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, " Incubation stage <*1*> is not 1 or 2 therefore not recognized for procedure <*2*>.",
                        new Object[]{incubStage, schemaPrefix});

        Integer posic = LPArray.valuePosicInArray(fieldsName, batchFldName);
        if (posic==-1) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, " Field <*1*> not found in table <*2*> for procedure <*3*>",
                new Object[]{batchFldName, TblsEnvMonitData.Sample.TBL.getName(), schemaPrefix});
        if ( (fieldsValue[posic]==null) || (fieldsValue[posic].toString().length()==0) ) return new Object[]{LPPlatform.LAB_TRUE};
        else return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, " The pending incubation stage is <*1*> and the sample <*2*>  is already batched in batch <*3*> for procedure <*3*>",
                new Object[]{incubStage, sampleId, fieldsValue[posic], schemaPrefix});
    }
    
        
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param batchName
     * @param incubName
     * @return
     */
    public static Object[] batchAssignIncubator(String schemaPrefix, Token token, String batchName, String incubName){
        Object[][] batchInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{batchName}, 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchInfo[0][0].toString()))
            return LPArray.array2dTo1d(batchInfo);
        if (!Boolean.valueOf(batchInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The Batch <*1*> is not active", new Object[]{batchName});
        String[] updFieldName=new String[]{TblsEnvMonitData.IncubBatch.FLD_INCUBATION_INCUBATOR.getName()};
        Object[] updFieldValue=new Object[]{incubName};
        Object[] updateDiagn=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), 
                updFieldName, updFieldValue, 
                new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{batchName});        
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateDiagn[0].toString()))
            IncubBatchAudit.IncubBatchAuditAdd(schemaPrefix, token, BatchAuditEvents.BATCH_ASSIGN_INCUBATOR.toString(), TblsEnvMonitData.IncubBatch.TBL.getName(), batchName, incubName,  
                        LPArray.joinTwo1DArraysInOneOf1DString(updFieldName, updFieldValue, ":"), null);
        return updateDiagn;
    }
    
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param batchName
     * @param fieldsName
     * @param fieldsValue
     * @return
     */
    public static Object[] batchUpdateInfo(String schemaPrefix, Token token, String batchName, String[] fieldsName, Object[] fieldsValue){
        Object[][] batchInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitConfig.IncubBatch.TBL.getName(), 
                new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{batchName}, 
                new String[]{TblsEnvMonitConfig.IncubBatch.FLD_ACTIVE.getName(), TblsEnvMonitConfig.IncubBatch.FLD_TYPE.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(batchInfo[0][0].toString()))
            return LPArray.array2dTo1d(batchInfo);
        if (!Boolean.valueOf(batchInfo[0][0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The Batch <*1*> is not active", new Object[]{batchName});        
        Object[] updateDiagnostic=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.IncubBatch.TBL.getName(), 
                fieldsName, fieldsValue, 
                new String[]{TblsEnvMonitData.IncubBatch.FLD_NAME.getName()}, new Object[]{batchName});
        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateDiagnostic[0].toString()))
            IncubBatchAudit.IncubBatchAuditAdd(schemaPrefix, token, BatchAuditEvents.BATCH_UPDATED.toString(), TblsEnvMonitData.IncubBatch.TBL.getName(), batchName, batchName,
                LPArray.joinTwo1DArraysInOneOf1DString(fieldsName, fieldsValue, ": "), null);
        return updateDiagnostic;
    }
}
