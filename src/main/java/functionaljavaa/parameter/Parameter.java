/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author Administrator
 */
public class Parameter {

    /**
     *
     */
    public static final String BUNDLE_TAG_PARAMETER_CONFIG_CONF="parameter.config.app-config";

    /**
     *
     */
    public static final String BUNDLE_TAG_TRANSLATION_DIR_PATH="translationDirPath";
    

    /**
     *  Get the parameter value or blank otherwise.
     * @param parameterFolder - The directoy name LabPLANET (api messages/error trapping)/config (procedure business rules) (if null then config)
     * @param procName - procedureName
     * @param schemaSuffix - The procedure schema: config/data/procedure. 
     * @param parameterName - Tag name
     * @param language - Language
     * @return
     **/
    public static String getParameterBundle(String parameterFolder, String procName, String schemaSuffix, String parameterName, String language) {
        ResourceBundle prop = null;
        if (parameterFolder==null){parameterFolder="config";}
        String filePath = "parameter."+parameterFolder+"."+procName;
        if (schemaSuffix!=null){filePath=filePath+"-"+schemaSuffix;}
        if (language != null) {filePath=filePath+"_" + language;}
        
        try {
            prop = ResourceBundle.getBundle(filePath);
            if (!prop.containsKey(parameterName)) {              
                LPPlatform.saveParameterPropertyInDbErrorLog(procName, parameterFolder, parameterName);
                return "";
            } else {
                return prop.getString(parameterName);
            }
        } catch (Exception e) {
            LPPlatform.saveParameterPropertyInDbErrorLog(procName, parameterFolder, parameterName);
            return "";
        }
    }

    /**
     *  Check if a parameter is part or not of a properties file
     * @param parameterFolder - The directoy name LabPLANET (api messages/error trapping)/config (procedure business rules) (if null then config)
     * @param schemaName - procedureName
     * @param areaName - The procedure schema: config/data/procedure. 
     * @param parameterName - Tag name
     * @param language - Language
     * @return
     */
    public Boolean parameterInFile(String parameterFolder, String schemaName, String areaName, String parameterName, String language){
        return !"".equals(getParameterBundle(parameterFolder, schemaName, areaName, parameterName, language));
    }
    /**
     *
     * @param parameterName
     * @return
     */
    public static String getParameterBundleAppFile(String parameterName) {
        return getParameterBundleInAppFile("parameter.config.app", parameterName);
    }

    public static String getParameterBundleInConfigFile(String configFile, String parameterName, String language) {
        return getParameterBundleInAppFile("parameter.config." + configFile + "_" + language, parameterName);
    }

    private static String getParameterBundleInAppFile(String fileUrl, String parameterName) {
        try {
            ResourceBundle prop = ResourceBundle.getBundle(fileUrl);
            if (!prop.containsKey(parameterName)) {
                LPPlatform.saveParameterPropertyInDbErrorLog("", fileUrl, parameterName);
                return "";
            } else {
                return prop.getString(parameterName);
            }
        } catch (Exception e) {
            LPPlatform.saveParameterPropertyInDbErrorLog("", fileUrl, parameterName);
            return e.getMessage();
        }
    }
    
    /**
     *
     * @param configFile
     * @param parameterName
     * @return
     */
    public static String getParameterBundle(String configFile, String parameterName) {
        try {            
            ResourceBundle prop = ResourceBundle.getBundle("parameter.config." + configFile.replace("\"", ""));
            if (!prop.containsKey(parameterName)) {
                LPPlatform.saveParameterPropertyInDbErrorLog("", configFile, parameterName);
                return "";
            } else {
                return prop.getString(parameterName);
            }
        } catch (Exception e) {
            LPPlatform.saveParameterPropertyInDbErrorLog("", configFile, parameterName);
            return "";
        }
    }

    /**
     * Not in use
     * @param fileName
     * @param entryName
     * @param entryValue
     * @return
     * @throws java.io.IOException
     */
    public String _addTagInPropertiesFile(String fileName, String entryName, String entryValue) throws IOException{

        StringBuilder newEntryBuilder = new StringBuilder(0);

        ResourceBundle propConfig = ResourceBundle.getBundle(BUNDLE_TAG_PARAMETER_CONFIG_CONF);        
        String translationsDir = propConfig.getString(BUNDLE_TAG_TRANSLATION_DIR_PATH);
        translationsDir = translationsDir.replace("/", "\\");

        File[] transFiles = propertiesFiles(fileName);
        for (File f: transFiles)
        {
            String fileidt = translationsDir + "\\" + f.getName();
            try{    
                return " Exists the tag in " + f.getName() + " for the entry " + entryName + " and value " + entryValue;
            }catch(MissingResourceException ex)
            {
                String newLogEntry = " created tag in " + f.getName() + " for the entry " + entryName + " and value " + entryValue;

                if (fileName.equalsIgnoreCase("USERNAV")){ newEntryBuilder.append(entryName).append(":").append(entryValue);}
                else { newEntryBuilder.append(entryName).append("=").append(entryValue);}
                
                try (FileWriter fw = new FileWriter(fileidt, true)){
                    if (newEntryBuilder.length()>=0){
                        newEntryBuilder.append("\n");
                        fw.append(newEntryBuilder.toString());
                        }
                }
                return newLogEntry;
            }
        }    
        return "Nothing done";
    }

    /**
     *
     * @param fileName
     * @return
     */
    public File[] propertiesFiles(String fileName){

        ResourceBundle propConfig = ResourceBundle.getBundle(BUNDLE_TAG_PARAMETER_CONFIG_CONF);        
        String translationsDir = propConfig.getString(BUNDLE_TAG_TRANSLATION_DIR_PATH);
        translationsDir = translationsDir.replace("/", "\\");

        File dir = new File(translationsDir);
        return dir.listFiles((File dir1, String name) -> name.contains(fileName));       
    }    
     
}
