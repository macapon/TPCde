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
public interface DataSampleAnalysisResultStrategy {

    /**
     *
     */
    String sampleActionWhenUponControlModeEnablingStatuses="ENABLE";

    /**
     *
     */
    String sampleActionWhenUponOOSModeEnablingStatuses="ENABLE";

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param resultId
     * @param sampleFieldName
     * @param sampleFieldValue
     * @param sarFieldName
     * @param sarFieldValue
     * @return
     */
    public abstract Object[] sarControlAction(String schemaPrefix, Token token, Integer resultId, String[] sampleFieldName, Object[] sampleFieldValue, String[] sarFieldName, Object[] sarFieldValue);

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param resultId
     * @param sampleFieldName
     * @param sampleFieldValue
     * @param sarFieldName
     * @param sarFieldValue
     * @return
     */
    public abstract Object[] sarOOSAction(String schemaPrefix, Token token, Integer resultId, String[] sampleFieldName, Object[] sampleFieldValue, String[] sarFieldName, Object[] sarFieldValue);
}
