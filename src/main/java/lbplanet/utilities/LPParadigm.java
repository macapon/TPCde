/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbplanet.utilities;

import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class LPParadigm {
    private LPParadigm(){    throw new IllegalStateException("Utility class");}    
    
    public enum ParadigmErrorTrapping{ 
        SPECIAL_FUNCTION_RETURNED_EXCEPTION("SpecialFunctionReturnedException", "", ""),
        SPECIAL_FUNCTION_RETURNED_ERROR("SpecialFunctionReturnedERROR", "", ""),
        UNHANDLED_EXCEPTION_IN_CODE("UnhandledExceptionInCode", "", ""),
        ;
        private ParadigmErrorTrapping(String errCode, String defaultTextEn, String defaultTextEs){
            this.errorCode=errCode;
            this.defaultTextWhenNotInPropertiesFileEn=defaultTextEn;
            this.defaultTextWhenNotInPropertiesFileEs=defaultTextEs;
        }
        public String getErrorCode(){return this.errorCode;}
        public String getDefaultTextEn(){return this.defaultTextWhenNotInPropertiesFileEn;}
        public String getDefaultTextEs(){return this.defaultTextWhenNotInPropertiesFileEs;}
    
        private final String errorCode;
        private final String defaultTextWhenNotInPropertiesFileEn;
        private final String defaultTextWhenNotInPropertiesFileEs;
    }    
    /**
     *
     * @param fName
     * @param fValue
     * @return
     */
    public static Object[] fieldNameValueArrayChecker (String[] fName, Object[] fValue){
        Object[] diagnoses = null;
        String errorCode ="";
        Object[] errorDetailVariables= new Object[0];

        diagnoses = LPArray.checkTwoArraysSameLength(fName, fValue);
        if (!LPPlatform.LAB_TRUE.equalsIgnoreCase(diagnoses[0].toString())){
           errorCode = "DataSample_FieldArraysDifferentSize";
           errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, Arrays.toString(fName));
           errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, Arrays.toString(fValue));
           return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);
        }
        
        if (LPArray.duplicates(fName)){
           errorCode = "DataSample_FieldsDuplicated";
           errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, Arrays.toString(fName));
           return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                      
        }        
        diagnoses = LPArray.addValueToArray1D(diagnoses, LPPlatform.LAB_TRUE);
        return diagnoses;                
    }
    
}
