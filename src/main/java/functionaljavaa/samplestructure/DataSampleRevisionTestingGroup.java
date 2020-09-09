/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.samplestructure;

import databases.Rdbms;
import databases.TblsData;
import databases.Token;
import java.util.Arrays;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;


/**
 *
 * @author User
 */
public class DataSampleRevisionTestingGroup {
    public enum TestingGroupFileProperties{sampleTestingByGroup_ReviewByTestingGroup};

    public static Object[] addSampleRevisionByTestingGroup(String schemaPrefix, Token token, Integer sampleId, Integer testId, String specAnalysisTestingGroup){        
        Object[] isReviewByTestingGroupEnable=LPPlatform.isProcedureBusinessRuleEnable(schemaPrefix, "procedure", TestingGroupFileProperties.sampleTestingByGroup_ReviewByTestingGroup.toString());
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isReviewByTestingGroupEnable[0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "sampleTestingByGroup_ReviewByTestingGroup Not active", null);
        if (specAnalysisTestingGroup==null || specAnalysisTestingGroup.length()==0){
            Object[][] testInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAnalysis.TBL.getName(),
                new String[]{TblsData.SampleAnalysis.FLD_TEST_ID.getName()},
                new Object[]{testId}, new String[]{TblsData.SampleAnalysis.FLD_TESTING_GROUP.getName()});
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(testInfo[0][0].toString())) return testInfo;
            if (LPNulls.replaceNull(testInfo[0][0]).toString().length()==0) return testInfo;
            specAnalysisTestingGroup=testInfo[0][0].toString();
        }
        Object[] existsSampleRevisionTestingGroupRecord = Rdbms.existsRecord(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleRevisionTestingGroup.TBL.getName(), 
            new String[]{TblsData.SampleRevisionTestingGroup.FLD_SAMPLE_ID.getName(), TblsData.SampleRevisionTestingGroup.FLD_TESTING_GROUP.getName()}, 
            new Object[]{sampleId, specAnalysisTestingGroup});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(existsSampleRevisionTestingGroupRecord[0].toString())) return existsSampleRevisionTestingGroupRecord;
        return Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleRevisionTestingGroup.TBL.getName(),
            new String[]{TblsData.SampleRevisionTestingGroup.FLD_SAMPLE_ID.getName(), TblsData.SampleRevisionTestingGroup.FLD_TESTING_GROUP.getName(), TblsData.SampleRevisionTestingGroup.FLD_READY_FOR_REVISION.getName(), TblsData.SampleRevisionTestingGroup.FLD_REVIEWED.getName()}, 
            new Object[]{sampleId, specAnalysisTestingGroup, false, false});
    }
    public static Object[] isSampleRevisionByTestingGroupReviewed(String schemaPrefix, Token token, Integer sampleId){
        return isSampleRevisionByTestingGroupReviewed(schemaPrefix, token, sampleId, null);
    }
    public static Object[] isSampleRevisionByTestingGroupReviewed(String schemaPrefix, Token token, Integer sampleId, String testingGroup){
        Object[] isReviewByTestingGroupEnable=LPPlatform.isProcedureBusinessRuleEnable(schemaPrefix, "procedure", TestingGroupFileProperties.sampleTestingByGroup_ReviewByTestingGroup.toString());
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isReviewByTestingGroupEnable[0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "sampleTestingByGroup_ReviewByTestingGroup Not active", null);
        String[] fieldNames=new String[]{TblsData.SampleRevisionTestingGroup.FLD_SAMPLE_ID.getName(), TblsData.SampleRevisionTestingGroup.FLD_REVIEWED.getName()};
        Object[] fieldValues=new Object[]{sampleId, false};
        if (testingGroup!=null && testingGroup.length()>0){
            fieldNames=LPArray.addValueToArray1D(fieldNames, TblsData.SampleRevisionTestingGroup.FLD_TESTING_GROUP.getName());
            fieldValues=LPArray.addValueToArray1D(fieldValues, testingGroup);
        }
        Object[][] existsPendingRevisionRecord = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleRevisionTestingGroup.TBL.getName(),
                fieldNames, fieldValues, new String[]{TblsData.SampleRevisionTestingGroup.FLD_TESTING_GROUP.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(existsPendingRevisionRecord[0][0].toString())){
            return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "No testing group revision pending for sample <*1*> in procedure <*2*>", new Object[]{sampleId, schemaPrefix});
        }else{            
            String pendingTestingGroupStr=Arrays.toString(LPArray.getColumnFromArray2D(existsPendingRevisionRecord, 0));
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "There are pending testing group, <*1*>, for the sample <*2*> in procedure <*3*>", 
                new Object[]{pendingTestingGroupStr, sampleId, schemaPrefix});
        }
    }
    
    public static Object[] reviewSampleTestingGroup(String schemaPrefix, Token token, Integer sampleId, String testingGroup){
        Object[] isReviewByTestingGroupEnable=LPPlatform.isProcedureBusinessRuleEnable(schemaPrefix, "procedure", TestingGroupFileProperties.sampleTestingByGroup_ReviewByTestingGroup.toString());
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isReviewByTestingGroupEnable[0].toString()))
            return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "sampleTestingByGroup_ReviewByTestingGroup Not active", null);
        Object[] sampleRevisionByTestingGroupReviewed = isSampleRevisionByTestingGroupReviewed(schemaPrefix, token, sampleId, testingGroup);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(sampleRevisionByTestingGroupReviewed[0].toString())) return sampleRevisionByTestingGroupReviewed;
        
        Object[] existsPendingAnalysis = Rdbms.existsRecord(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.ViewSampleAnalysisResultWithSpecLimits.TBL.getName(),
                new String[]{TblsData.ViewSampleAnalysisResultWithSpecLimits.FLD_SAMPLE_ID.getName(), TblsData.ViewSampleAnalysisResultWithSpecLimits.FLD_TESTING_GROUP.getName(), TblsData.ViewSampleAnalysisResultWithSpecLimits.FLD_MANDATORY.getName(), TblsData.ViewSampleAnalysisResultWithSpecLimits.FLD_RAW_VALUE.getName()+" is null"}, 
                new Object[]{sampleId, testingGroup, true});
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(existsPendingAnalysis[0].toString())) 
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "There are pending results for the testing group <*1*> for the sample <*2*> in procedure <*3*>", null);
        return Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleRevisionTestingGroup.TBL.getName(),
            new String[]{TblsData.SampleRevisionTestingGroup.FLD_READY_FOR_REVISION.getName(), TblsData.SampleRevisionTestingGroup.FLD_REVIEWED.getName(), TblsData.SampleRevisionTestingGroup.FLD_REVISION_BY.getName(), TblsData.SampleRevisionTestingGroup.FLD_REVISION_ON.getName()},
            new Object[]{false, true, token.getPersonName(), LPDate.getCurrentTimeStamp()}, 
            new String[]{TblsData.SampleRevisionTestingGroup.FLD_SAMPLE_ID.getName(), TblsData.SampleRevisionTestingGroup.FLD_TESTING_GROUP.getName()},
            new Object[]{sampleId, testingGroup});
    }
}