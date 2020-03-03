/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.testingscripts;

/**
 *
 * @author User
 */
public class LPTestingParams {
    
    public static final String UPLOAD_FILE_PARAM_FILE_PATH="filePath";
    public static final String UPLOAD_FILE_PARAM_FILE_NAME="filename";
    
    public enum TestingServletsConfig{
        NODB_SCHEMACONFIG_SPECQUAL_RULEFORMAT("/testing/config/testingConfigSpecQualitativeRuleFormat", "noDBSchema_config_SpecQualitativeRuleGeneratorChecker.txt"),
        NODB_SCHEMACONFIG_SPECQUAL_RESULTCHECK("/testing/config/ResultCheckSpecQualitative", "noDBSchema_config_specQualitative_resultCheck.txt"),
        NODB_SCHEMACONFIG_SPECQUANTI_RULEFORMAT("/testing/config/testingConfigSpecQuantitativeRuleFormat", "noDBSchema_config_SpecQuantitativeRuleGeneratorChecker.txt"),
        NODB_SCHEMACONFIG_SPECQUANTI_RESULTCHECK("/testing/config/ResultCheckSpecQuantitative", "noDBSchema_config_specQuantitative_resultCheck.txt"),
        ;
        private TestingServletsConfig(String url, String fileName){
            this.servletUrl=url;
            this.testerFileName=fileName;
        }
        public String getServletUrl(){
            return this.servletUrl;
        }        
        
        public String getTesterFileName(){
            return this.testerFileName;
        }
        private final String servletUrl;
        private final String testerFileName;
    }
}
