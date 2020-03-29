/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import databases.Rdbms;
import databases.TblsData;
import databases.Token;
import functionaljavaa.instruments.incubator.ConfigIncubator;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class ClassEnvMonQueries {
    
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    private Boolean functionFound=false;

    public ClassEnvMonQueries(HttpServletRequest request, Token token, String schemaPrefix, EnvMonAPI.EnvMonQueriesAPIEndpoints endPoint){
        try{
            Rdbms.stablishDBConection();
            Object[] dynamicDataObjects=new Object[]{};
            RelatedObjects rObj=RelatedObjects.getInstance();
            Object[] actionDiagnoses = null;
            Integer sampleId = null;
            Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());    
            String instrName="";
            BigDecimal temperature=null;
            this.functionFound=true;
            switch (endPoint){
                    case GET_SAMPLE_RESULTS:
                        sampleId=(Integer) argValues[0];
                        Integer testId=null;
                        if (argValues.length>1 && argValues[1]!=null && argValues[1].toString().length()>0) testId=(Integer) argValues[1];
                        Integer resultId=null;
                        if (argValues.length>2 && argValues[2]!=null && argValues[2].toString().length()>0) testId=(Integer) argValues[2];
                        String[] whereFieldNames=new String[]{TblsData.SampleAnalysisResult.FLD_SAMPLE_ID.getName()};
                        Object[] whereFieldValues=new Object[]{sampleId};
                        if (testId!=null){
                            whereFieldNames=LPArray.addValueToArray1D(whereFieldNames, TblsData.SampleAnalysisResult.FLD_TEST_ID.getName());
                            whereFieldValues=LPArray.addValueToArray1D(whereFieldValues, testId);
                        }
                        if (resultId!=null){
                            whereFieldNames=LPArray.addValueToArray1D(whereFieldNames, TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName());
                            whereFieldValues=LPArray.addValueToArray1D(whereFieldValues, resultId);
                        }
                        Object[][] resultInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAnalysisResult.TBL.getName(), 
                                whereFieldNames, whereFieldValues, 
                                new String[]{TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName()}, new String[]{TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName()});
                        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(resultInfo[0][0].toString())) actionDiagnoses=resultInfo[0];
                        else{
                            for (Object[] curResult: resultInfo){
                                rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsData.SampleAnalysisResult.TBL.getName(), TblsData.SampleAnalysisResult.TBL.getName(), curResult[0]); 
                            }
                            actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{sampleId});
                        }
                        messageDynamicData=new Object[]{instrName};
                        break;
            }
            if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))                
            this.diagnostic=actionDiagnoses;
            this.relatedObj=rObj;
            this.messageDynamicData=dynamicDataObjects;
            rObj.killInstance();
        }finally{
            Rdbms.closeRdbms(); 
        }
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