/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.modulegenoma;

import functionaljavaa.parameter.Parameter;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class GenomaBusinessRules {
    
    public static Boolean activateOnCreation(String schemaPrefix, String schemaSuffix, String tableName){
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, schemaSuffix);
        
        String propertyEntryName = tableName+"_activeOnCreation";        
        String propertyEntryValue = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), propertyEntryName);        
        if (propertyEntryValue.length()==0) return false;
        if ( ("YES".equalsIgnoreCase(propertyEntryValue)) || ("SI".equalsIgnoreCase(propertyEntryValue)) )
            return true;                  
        
        return false;
    }

    public static Object[] specialFieldsInUpdateArray(String schemaPrefix, String schemaSuffix, String tableName, String[] fieldsToCheck){
        String schemaDataName = LPPlatform.buildSchemaName(schemaPrefix, schemaSuffix);
        
        String propertyEntryName = tableName+"_specialFieldsLockedForProjectUpdateEndPoint";        
        String propertyEntryValue = Parameter.getParameterBundle(schemaDataName.replace("\"", ""), propertyEntryName);        
        if (propertyEntryValue.length()==0) return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "", null);
        String[] propertyEntryValueArr=propertyEntryValue.split("\\|");
        String specialFieldsPresent="";
        for (String curFldToCheck: fieldsToCheck){
            if ( LPArray.valueInArray(propertyEntryValueArr, curFldToCheck) ) {
                if (specialFieldsPresent.length()>0) specialFieldsPresent=specialFieldsPresent+", ";
                specialFieldsPresent=specialFieldsPresent+curFldToCheck;
            }                  
        }
        if (specialFieldsPresent.length()>0) return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "Special fields (<*1*>) are present and they are not allowed by the generic update action.", new Object[]{specialFieldsPresent});
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "", null);
    }
    
}
