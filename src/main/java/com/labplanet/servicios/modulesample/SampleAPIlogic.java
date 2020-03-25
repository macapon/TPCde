/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulesample;

import com.labplanet.servicios.app.GlobalAPIsParams;
import static com.labplanet.servicios.modulegenoma.GenomaStudyAPI.GenomaStudyAPIParamsList.sampleId;
import databases.TblsData;
import databases.Token;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleAnalysis;
import functionaljavaa.samplestructure.DataSampleStages;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class SampleAPIlogic {
    public static JSONObject performAction  (HttpServletRequest request, Token token, String schemaPrefix, DataSample smp, String actionName, SampleAPIParams.SampleAPIEndpoints endPoint, RelatedObjects rObj)  {
        
    
        //case TESTASSIGNMENT:
        String objectIdStr = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_TEST_ID);
        int testId = Integer.parseInt(objectIdStr);     
                    String newAnalyst = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_NEW_ANALYST);
        Object[] dataSample = DataSampleAnalysis.sampleAnalysisAssignAnalyst(schemaPrefix, token, testId, newAnalyst, smp);
                   rObj.addSimpleNode(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), TblsData.Sample.TBL.getName(), sampleId);                            
        Object[] messageDynamicData = new Object[]{sampleId}; 
                    //break;                               
        JSONObject dataSampleJSONMsg=new JSONObject();

        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(dataSample[0].toString())){  
/*                Rdbms.rollbackWithSavePoint();
            if (!con.getAutoCommit()){
                con.rollback();
                con.setAutoCommit(true);}                */
        }else{
            DataSampleStages smpStage=new DataSampleStages(schemaPrefix);
            if (smpStage.isSampleStagesEnable() && (sampleId!=null))
                //smpStage.dataSampleActionAutoMoveToNext(schemaPrefix, token, actionName, sampleId);
            
            dataSampleJSONMsg = LPFrontEnd.responseJSONDiagnosticLPTrue("this.getClass().getSimpleName()", 
                    endPoint.getSuccessMessageCode(), messageDynamicData, rObj.getRelatedObject());
        }
        rObj.killInstance();
        return dataSampleJSONMsg;
    }   
}
