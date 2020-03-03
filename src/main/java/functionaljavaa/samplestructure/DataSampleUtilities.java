/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.samplestructure;

import lbplanet.utilities.LPPlatform;
import functionaljavaa.parameter.Parameter;
import functionaljavaa.testingscripts.LPTestingOutFormat;

/**
 *
 * @author Administrator
 */
public class DataSampleUtilities {
    private DataSampleUtilities(){    throw new IllegalStateException("Utility class");}    
    
    /**
     *
     * @param schemaPrefix
     * @return
     */
    public static Object[] getSchemaSampleStatusList(String schemaPrefix){      
        return getSchemaSampleStatusList(schemaPrefix, "en");
    }

    /**
     *
     * @param schemaPrefix
     * @param language
     * @return
     */
    public static Object[] getSchemaSampleStatusList(String schemaPrefix, String language){      
        String stList = "";
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);
        if (language==null){language="en";}
       switch (language){
           case "en":
               stList = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statuses_label_en"); 
               break;
           case "es":
               stList = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statuses_label_es"); 
               break;
           default:
               stList = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), "sample_statuses"); 
               break;
       }        
        return LPTestingOutFormat.csvExtractFieldValueStringArr(stList);
    }
    
}
