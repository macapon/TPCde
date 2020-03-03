/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.samplestructure;

import databases.Token;

/**
 *
 * @author Administrator
 */
public interface DataSampleAnalysisStrategy {

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param sampleId
     * @param sampleFieldName
     * @param sampleFieldValue
     * @param eventName
     * @param preAuditId
     * @return
     */
    public abstract Object[] autoSampleAnalysisAdd(String schemaPrefix, Token token, Integer sampleId, String[] sampleFieldName, Object[] sampleFieldValue, String eventName, Integer preAuditId);

    /**
     *
     * @param schemaPrefix
     * @param template
     * @param templateVersion
     * @param dataSample
     * @param preAuditId
     * @return
     */
    public abstract String specialFieldCheckSampleAnalysisAnalyst(String schemaPrefix, String template, Integer templateVersion, DataSample dataSample, Integer preAuditId);
  
}
