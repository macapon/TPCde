/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.moduleenvironmentalmonitoring;

import databases.Token;
import functionaljavaa.parameter.Parameter;
import functionaljavaa.samplestructure.DataSampleAnalysisResultStrategy;
import lbplanet.utilities.LPArray;

/**
 *
 * @author Administrator
 */
public class DataProgramSampleAnalysisResult implements DataSampleAnalysisResultStrategy{

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
    @Override
  public Object[] sarControlAction(String schemaPrefix, Token token, Integer resultId, String[] sampleFieldName, Object[] sampleFieldValue, String[] sarFieldName, Object[] sarFieldValue) {
      String sampleActionWhenUponControlMode = Parameter.getParameterBundle("config", schemaPrefix, "procedure", "sampleActionWhenUponControlMode", null);
      if (LPArray.valuePosicInArray(SAMPLEACTIONWHENUPONCONTROLMODEENABLINGSTATUSES.split("\\|"), sampleActionWhenUponControlMode)==-1)
          return new Object[0];
      return DataProgramCorrectiveAction.createNew(schemaPrefix, token, resultId, sampleFieldName, sampleFieldValue,sarFieldName, sarFieldValue);
  }

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
    @Override
  public Object[] sarOOSAction(String schemaPrefix, Token token, Integer resultId, String[] sampleFieldName, Object[] sampleFieldValue, String[] sarFieldName, Object[] sarFieldValue) {
      String sampleActionWhenOOSMode = Parameter.getParameterBundle("config", schemaPrefix, "procedure", "sampleActionWhenOOSMode", null);
      if (LPArray.valuePosicInArray(SAMPLEACTIONWHENUPONOOSMODEENABLINGSTATUSES.split("\\|"), sampleActionWhenOOSMode)==-1)
          return new Object[0];
      return DataProgramCorrectiveAction.createNew(schemaPrefix, token, resultId, sampleFieldName, sampleFieldValue,sarFieldName, sarFieldValue);
  }
}
