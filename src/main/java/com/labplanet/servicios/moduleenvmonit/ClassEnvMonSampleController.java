/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Token;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPNulls;
import org.json.simple.JSONArray;

/**
 *
 * @author User
 */
public class ClassEnvMonSampleController {
    
    private StringBuilder rowArgsRows=new StringBuilder();
    private Object[] functionDiagn=new Object[0];
    private JSONArray functionRelatedObjects=new JSONArray();
    private Boolean functionFound=false;
    
    public ClassEnvMonSampleController(HttpServletRequest request, Token token, String schemaPrefix, String actionName, Object[][] testingContent, Integer iLines, Integer table1NumArgs) {
        
        Object[] argsForLogFiles=new Object[0];
        EnvMonSampleAPI.EnvMonSampleAPIEndpoints endPoint = null;
        try{
            endPoint = EnvMonSampleAPI.EnvMonSampleAPIEndpoints.valueOf(actionName.toString().toUpperCase());
                    HashMap<HttpServletRequest, Object[]> hmQuery = endPoint.testingSetAttributesAndBuildArgsArray(request, testingContent, iLines);
                    HttpServletRequest query= hmQuery.keySet().iterator().next();   
                    argsForLogFiles = hmQuery.get(query);
/*            switch (endPoint){
                case LOGSAMPLE:
                    HashMap<HttpServletRequest, Object[]> hmQuery = endPoint.testingSetAttributesAndBuildArgsArray(request, testingContent, iLines);
                    HttpServletRequest query= hmQuery.keySet().iterator().next();   
                    argsForLogFiles = hmQuery.get(query);
                                        
//                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE, LPTestingOutFormat.getAttributeValue(testingContent[iLines][6], testingContent) );
//                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE_VERSION, LPTestingOutFormat.getAttributeValue(testingContent[iLines][7], testingContent) );
//                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME, LPTestingOutFormat.getAttributeValue(testingContent[iLines][8], testingContent) );
//                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE, LPTestingOutFormat.getAttributeValue(testingContent[iLines][9], testingContent) );                    
//                    request.setAttribute(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME, LPTestingOutFormat.getAttributeValue(testingContent[iLines][10], testingContent));
//                    request.setAttribute(EnvMonitAPIParams.REQUEST_PARAM_LOCATION_NAME, LPTestingOutFormat.getAttributeValue(testingContent[iLines][11], testingContent));   
//                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG, LPTestingOutFormat.getAttributeValue(testingContent[iLines][12], testingContent));   
//                    argsForLogFiles=new Object[]{GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE+":"+LPNulls.replaceNull(testingContent[iLines][6]).toString(), GlobalAPIsParams.REQUEST_PARAM_SAMPLE_TEMPLATE_VERSION+":"+LPNulls.replaceNull(testingContent[iLines][7]).toString(), 
//                        GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_NAME+":"+LPNulls.replaceNull(testingContent[iLines][8]).toString(), GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_VALUE+":"+LPNulls.replaceNull(testingContent[iLines][9]).toString(), 
//                        EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME+":"+LPNulls.replaceNull(testingContent[iLines][10]).toString(), EnvMonitAPIParams.REQUEST_PARAM_LOCATION_NAME+":"+LPNulls.replaceNull(testingContent[iLines][11]).toString(),
//                        GlobalAPIsParams.REQUEST_PARAM_NUM_SAMPLES_TO_LOG+":"+LPNulls.replaceNull(testingContent[iLines][12]).toString()    
//                    };
//                    
                    break;
                case ADD_SAMPLE_MICROORGANISM:
                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID, LPTestingOutFormat.getAttributeValue(testingContent[iLines][6], testingContent) );
                    request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_MICROORGANISM_NAME, LPTestingOutFormat.getAttributeValue(testingContent[iLines][7], testingContent) );
                    argsForLogFiles=new Object[]{GlobalAPIsParams.REQUEST_PARAM_SAMPLE_ID+":"+LPNulls.replaceNull(testingContent[iLines][6]).toString(), GlobalAPIsParams.REQUEST_PARAM_MICROORGANISM_NAME+":"+LPNulls.replaceNull(testingContent[iLines][7]).toString(), 
                    };
                    break;
            }  
*/        
            for (int inumArg=argsForLogFiles.length+4;inumArg<table1NumArgs;inumArg++){
                argsForLogFiles=LPArray.addValueToArray1D(argsForLogFiles, "");
            }
            this.functionFound=true;
            this.rowArgsRows=this.rowArgsRows.append(LPTestingOutFormat.rowAddFields(argsForLogFiles));
            ClassEnvMonSample clss=new ClassEnvMonSample(request, token, schemaPrefix.toString(), endPoint);
            this.functionDiagn=clss.getDiagnostic();
            this.functionRelatedObjects=clss.getRelatedObj().getRelatedObject();  
            
        }catch(Exception e){
            //endPoint = SampleAPIParams.SampleAPIEndpoints.valueOf(actionName.toString().toUpperCase());
            //ClassEnvMonSample clss=new ClassSample(request, token, schemaPrefix.toString(), endPoint);
            //functionEvaluation=clss.getDiagnostic();
            //functionRelatedObjects=clss.getRelatedObj().getRelatedObject();              
        }
    }

    /**
     * @return the rowArgsRows
     */
    public StringBuilder getRowArgsRows() {
        return rowArgsRows;
    }

    /**
     * @return the functionDiagn
     */
    public Object getFunctionDiagn() {
        return functionDiagn;
    }

    /**
     * @return the functionRelatedObjects
     */
    public JSONArray getFunctionRelatedObjects() {
        return functionRelatedObjects;
    }

    /**
     * @return the functionFound
     */
    public Boolean getFunctionFound() {
        return functionFound;
    }
}

                

