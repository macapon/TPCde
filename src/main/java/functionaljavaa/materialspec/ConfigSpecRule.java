/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.materialspec;

import databases.Rdbms;
import databases.TblsCnfg;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class ConfigSpecRule {

    /**
     *
     */
    public static final String JSON_TAG_NAME_SPEC_RULE_DETAILED = "spec_rule_with_detail";

    String classVersion = "0.1";
    
    private Boolean ruleIsQuantitative=false;
    private Boolean ruleIsQualitative=false;
    
    private BigDecimal minSpec=null;
    private Boolean minSpecIsStrict=null;
    private BigDecimal maxSpec=null;
    private Boolean maxSpecIsStrict=null;
    private BigDecimal minControl=null;
    private Boolean minControlIsStrict=null;
    private BigDecimal maxControl=null;
    private Boolean maxControlIsStrict=null;
    private Boolean quantitativeHasControl=false;  
    String ruleRepresentation=null;
    private String quantitativeRuleRepresentation=null;        
    private String qualitativeRuleRepresentation=null;
    
    private String qualitativeRule="";
    private String qualitativeRuleValues="";
    private String qualitativeRuleSeparator=null;
    private String qualitativeRuleListName=null;

    /**
     *
     */
    public static final String SPEC_WORD_FOR_UPON_CONTROL="CONTROL";

    /**
     *
     */
    public static final String SPEC_WORD_FOR_OOS="OUT";

    /**
     *
     */
    public static final String SPEC_WORD_FOR_INSPEC="IN";
    
    /**
     *
     */
    public static final  String MESSAGE_CODE_QUANT_MINSPEC_MAXSPEC_SUCCESS="specLimits_quantitativeMinSpecMaxSpec_Successfully";
    
    /**
     *
     * @param rule
     * @param textSpec
     * @param separator
     * @return
     */
    public Object[] specLimitIsCorrectQualitative(String rule, String textSpec, String separator){
        String errorCode = "";
        Object[]  errorDetailVariables= new Object[0];
        
        String[] expectedRules = new String[6];
        expectedRules[0] = "EQUALTO";
        expectedRules[1] = "NOTEQUALTO";
        expectedRules[2] = "CONTAINS";
        expectedRules[3] = "NOTCONTAINS";
        expectedRules[4] = "ISONEOF";
        expectedRules[5] = "ISNOTONEOF";
                
        if ((rule==null) || (rule.length()==0)){
           errorCode = "specLimits_ruleMandatoryArgumentNull";
           errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, "");          
           return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                                
        if ((textSpec==null) || (textSpec.length()==0)){
            errorCode = "specLimits_textSpecMandatoryArgumentNull";
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, "");          
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                                
        switch (rule.toUpperCase()){
            case "EQUALTO":  errorCode = "specLimits_equalTo_Successfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);                          
            case "NOTEQUALTO": errorCode = "specLimits_notEqualTo_Successfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);
            case "CONTAINS": errorCode = "specLimits_contains_Successfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);          
            case "NOTCONTAINS": errorCode = "specLimits_notContains_Successfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);          
            case "ISONEOF": 
                if ((separator==null) || (separator.length()==0)){
                    errorCode = "specLimits_separatorMandatoryArgumentNull";
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, rule.toUpperCase());          
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, "");          
                    return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                       
                else{
                    String[] textSpecArray = textSpec.split(separator);
                    errorCode = "specLimits_isOneOf_Successfully";
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, textSpecArray.length);          
                    return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                       
            case "ISNOTONEOF": 
                if ((separator==null) || (separator.length()==0)){
                    errorCode = "specLimits_separatorMandatoryArgumentNull";
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, rule.toUpperCase());          
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, "");          
                    return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}    
                else{
                    String[] textSpecArray = textSpec.split(separator);
                    errorCode = "specLimits_isNotOneOf_Successfully";
                    errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, textSpecArray.length);          
                    return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                          
            default: 
                errorCode = "specLimits_qualitativeRuleNotRecognized";
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, rule);          
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, Arrays.toString(expectedRules));          
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);    
        }
    }
/**
 * This method verify that the parameters provided to build one quantitative spec limit apply just one range are coherent accordingly to the different options:<br>
 * Basically when both are not null then cannot be the same value even min cannot be greater than max.
 * @param minSpec Float - The minimum value
 * @param maxSpec Float - The maximum value
 * Bundle parameters:
 *          config-specLimits_MinAndMaxSpecBothMandatory, specLimits_quantitativeMinSpecSuccessfully, specLimits_quantitativeMaxSpecSuccessfully<br>
 *          MESSAGE_CODE_QUANT_MINSPEC_MAXSPEC_SUCCESS, specLimits_quantitativeMinSpecMaxSpec_MinSpecGreaterOrEqualToMaxSpec
 * @return Object[] position 0 is a boolean to determine if the arguments are correct, when set to false then position 1 provides detail about the deficiency 
 */
    public Object[] specLimitIsCorrectQuantitative(Float minSpec, Float maxSpec){
        String errorCode = "";
        Object[]  errorDetailVariables= new Object[0];        
        
        if ((minSpec==null) && (maxSpec==null)){
            errorCode = "specLimits_MinAndMaxSpecBothMandatory"; return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                               
        if ((minSpec!=null) && (maxSpec==null)){
            errorCode = "specLimits_quantitativeMinSpecSuccessfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                    
        if ((minSpec==null) && (maxSpec!=null)){
            errorCode = "specLimits_quantitativeMaxSpecSuccessfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                           
        if (minSpec<maxSpec){
            errorCode = MESSAGE_CODE_QUANT_MINSPEC_MAXSPEC_SUCCESS; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                    
        
        errorCode = "specLimits_quantitativeMinSpecMaxSpec_MinSpecGreaterOrEqualToMaxSpec"; 
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minSpec.toString());        
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxSpec.toString());
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                                    
    }

/**
 * This method verify that the parameters provided to build one quantitative spec limit apply just one range are coherent accordingly to the different options:<br>
 * Basically when both are not null then cannot be the same value even min cannot be greater than max.
 * @param minSpec BigDecimal - The minimum value
 * @param maxSpec BigDecimal - The maximum value
 * Bundle parameters:
 *          config-specLimits_MinAndMaxSpecBothMandatory, specLimits_quantitativeMinSpecSuccessfully, specLimits_quantitativeMaxSpecSuccessfully<br>
 *          specLimits_quantitativeMinSpecMaxSpec_Successfully, specLimits_quantitativeMinSpecMaxSpec_MinSpecGreaterOrEqualToMaxSpec
 * @return Object[] position 0 is a boolean to determine if the arguments are correct, when set to false then position 1 provides detail about the deficiency 
 */
    public Object[] specLimitIsCorrectQuantitative(BigDecimal minSpec, BigDecimal maxSpec){
        String errorCode = "";
        Object[]  errorDetailVariables= new Object[0];        
                
        if ((minSpec==null) && (maxSpec==null)){
            errorCode = "specLimits_MinAndMaxSpecBothMandatory"; return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                               
        if ((minSpec!=null) && (maxSpec==null)){
            errorCode = "specLimits_quantitativeMinSpecSuccessfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                    
        if ((minSpec==null) && (maxSpec!=null)){
            errorCode = "specLimits_quantitativeMaxSpecSuccessfully"; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                           
        int comparsion = minSpec.compareTo(maxSpec);
         if (comparsion!=1){
            errorCode = MESSAGE_CODE_QUANT_MINSPEC_MAXSPEC_SUCCESS; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                    
        
        errorCode = "specLimits_quantitativeMinSpecMaxSpec_MinSpecGreaterOrEqualToMaxSpec"; 
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minSpec.toString());        
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxSpec.toString());
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                                    
    }
    
/**
 * This method verify that the parameters provided to build one quantitative spec limit apply one double level range are coherent accordingly to the different options:<br>
 * Basically when both peers, min-max, are not null then cannot be the same value even min cannot be greater than max. At the same time
 * The control range should be included or part of the spec range that should be broader.
 * @param minSpec Float - The minimum value
 * @param maxSpec Float - The maximum value
 * @param minControl1 Float - The minimum control
 * @param maxControl1 Float - The maximum control
 * Bundle parameters:
 *          config-specLimits_quantitativeMinSpecMaxSpec_Successfully, specLimits_MinControlPresent_MinSpecMandatory, specLimits_MaxControlPresent_MaxSpecMandatory<br>
 *          specLimits_minControlGreaterOrEqualToMaxControl, specLimits_minControlGreaterOrEqualToMaxSpec, specLimits_MaxControlLessThanOrEqualToMinSpec <br>
 *          specLimits_MinControlLessThanOrEqualToMinSpec, specLimits_quantitativeMinSpecMinControlMaxSpec_Successfully, specLimits_MaxControlGreaterThanOrEqualToMaxSpec <br>
 *          specLimits_quantitativeMinSpecMinControlMaxControlMaxSpec_Successfully, specLimits_MinControlAndMaxControlOutOfLogicControl
 * @return Object[] position 0 is a boolean to determine if the arguments are correct, when set to false then position 1 provides detail about the deficiency 
 */    
    public Object[] specLimitIsCorrectQuantitative(Float minSpec, Float maxSpec, Float minControl1, Float maxControl1){
        String errorCode = "";
        Object[]  errorDetailVariables= new Object[0];        
        Object[] isCorrectMinMaxSpec = this.specLimitIsCorrectQuantitative(minSpec, maxSpec);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(isCorrectMinMaxSpec[0].toString())){
            return isCorrectMinMaxSpec;}
                
        if ((minControl1==null) && (maxControl1==null)){
            errorCode = MESSAGE_CODE_QUANT_MINSPEC_MAXSPEC_SUCCESS; return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);}                                            
        if ((minControl1!=null) && (minSpec==null)){
            errorCode = "specLimits_MinControlPresent_MinSpecMandatory"; 
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minControl1.toString());        
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                           
        if ((maxControl1!=null) && (maxSpec==null)){
            errorCode = "specLimits_MaxControlPresent_MaxSpecMandatory"; 
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxControl1.toString());      
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                    
        if (((minControl1!=null) && (maxControl1!=null)) && (minControl1>=maxControl1)){
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minControl1.toString());        
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxControl1.toString());    
            errorCode = "specLimits_minControlGreaterOrEqualToMaxControl"; 
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                                    
        if (((minControl1!=null) && (maxSpec!=null)) && (minControl1>=maxSpec)){
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minControl1.toString());        
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxSpec.toString());    
            errorCode = "specLimits_minControlGreaterOrEqualToMaxSpec"; 
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                      
        if (((maxControl1!=null) && (minSpec!=null)) && (maxControl1<=minSpec)){
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxControl1.toString());        
            errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minSpec.toString());    
            errorCode = "specLimits_MaxControlLessThanOrEqualToMinSpec"; 
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);}                      
        if (minControl1!=null){                        
            if (minControl1.compareTo(minSpec)<=0){
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minControl1.toString());        
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minSpec.toString());    
                errorCode = "specLimits_MinControlLessThanOrEqualToMinSpec"; 
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                                      
            }else{
                errorCode = "specLimits_quantitativeMinSpecMinControlMaxSpec_Successfully"; 
                return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);   
            }
        }                      
        if ((maxControl1!=null)){
            if (maxControl1.compareTo(maxSpec)>=0){
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxControl1.toString());        
                errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxSpec.toString()); 
                errorCode = "specLimits_MaxControlGreaterThanOrEqualToMaxSpec"; 
                return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);                                   
            }else{
                errorCode = "specLimits_quantitativeMinSpecMinControlMaxControlMaxSpec_Successfully"; 
                return LPPlatform.trapMessage(LPPlatform.LAB_TRUE, errorCode, errorDetailVariables);        
            }    
        }
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, minControl1.toString());        
        errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, maxControl1.toString()); 
        errorCode = "specLimits_MinControlAndMaxControlOutOfLogicControl"; 
        return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);              
    }    
    
    /**
     *
     * @param schemaPrefix
     * @param limitId
     * @param language
     * @return
     */
    public Object[] specLimitsRule(String schemaPrefix, Integer limitId, String language){
      String errorCode ="";
      Object[] errorDetailVariables= new Object[0];
      String specArgumentsSeparator = "\\*";
      StringBuilder ruleBuilder = new StringBuilder(0);
      Object[][] specDef=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsCnfg.SpecLimits.TBL.getName(), 
              new String[]{TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName()}, new Object[]{limitId}, 
              new String[]{TblsCnfg.SpecLimits.FLD_RULE_TYPE.getName(), TblsCnfg.SpecLimits.FLD_RULE_VARIABLES.getName()});
      if (LPPlatform.LAB_FALSE.equalsIgnoreCase(specDef[0][0].toString())) return LPArray.array2dTo1d(specDef);
      String ruleType = specDef[0][0].toString();
      String ruleVariables = specDef[0][1].toString();
      switch (ruleType.toLowerCase()) {
        case "qualitative":
          this.ruleIsQualitative=true;
          String[] qualitSpecTestingArray = ruleVariables.split(specArgumentsSeparator);
          this.qualitativeRule = qualitSpecTestingArray[0];
          this.qualitativeRuleValues = qualitSpecTestingArray[1];
          if (qualitSpecTestingArray.length == 3) {
              this.qualitativeRuleSeparator  = qualitSpecTestingArray[2];
          }
          this.qualitativeRuleListName= null;       
          this.qualitativeRuleRepresentation=ruleType+" "+ruleVariables;
          this.ruleRepresentation=this.qualitativeRuleRepresentation;
          break;
        case "quantitative":
          this.ruleIsQuantitative=true;
          String[] quantiSpecTestingArray = ruleVariables.split(specArgumentsSeparator);
          for (Integer iField=0; iField<quantiSpecTestingArray.length;iField++){
              String curParam = quantiSpecTestingArray[iField];

              if (curParam.toUpperCase().contains("MINSPECSTRICT")){
                      curParam = curParam.replace("MINSPECSTRICT", "");       
                      this.minSpec = BigDecimal.valueOf(Double.valueOf(curParam));   this.minSpecIsStrict=true;
              }        
              if (curParam.toUpperCase().contains("MINSPEC")){
                      curParam = curParam.replace("MINSPEC", "");    
                      //Long curParamLong=Long.valueOf(-2.5); 
                      this.minSpec = BigDecimal.valueOf(Double.valueOf(curParam)); this.minSpecIsStrict=false;
              }        
              if (curParam.toUpperCase().contains("MINCONTROLSTRICT")){
                      curParam = curParam.replace("MINCONTROLSTRICT", "");
                      this.minControl = BigDecimal.valueOf(Double.valueOf(curParam)); this.minControlIsStrict=true; this.quantitativeHasControl=true;
              }        
              if (curParam.toUpperCase().contains("MINCONTROL")){
                      curParam = curParam.replace("MINCONTROL", "");          this.minControl = BigDecimal.valueOf(Double.valueOf(curParam)); this.minControlIsStrict=false; this.quantitativeHasControl=true;
              }        
              if (curParam.toUpperCase().contains("MAXCONTROLTRICT")){
                      curParam = curParam.replace("MAXCONTROLSTRICT", "");       this.maxControl = BigDecimal.valueOf(Double.valueOf(curParam));     this.maxControlIsStrict=true; this.quantitativeHasControl=true;
              }        
              if (curParam.toUpperCase().contains("MAXCONTROL")){
                      curParam = curParam.replace("MAXCONTROL", "");          this.maxControl = BigDecimal.valueOf(Double.valueOf(curParam)); this.maxControlIsStrict=false; this.quantitativeHasControl=true;
              }        
              if (curParam.toUpperCase().contains("MAXSPECSTRICT")){
                      curParam = curParam.replace("MAXSPECSTRICT", "");       this.maxSpec = BigDecimal.valueOf(Double.valueOf(curParam));    this.maxSpecIsStrict=true;
              }        
              if (curParam.toUpperCase().contains("MAXSPEC")){
                      curParam = curParam.replace("MAXSPEC", "");              this.maxSpec =BigDecimal.valueOf(Double.valueOf(curParam)); this.maxSpecIsStrict=false;
              }        
          }   
          StringBuilder ruleRepresentation = new StringBuilder(0);
          if (this.minSpec!=null){
            if (this.minSpecIsStrict)ruleRepresentation.append("<");
            ruleRepresentation.append(this.minSpec);
          }
          if (this.minControl!=null){
            ruleRepresentation.append(" ");
            if (this.minControlIsStrict)ruleRepresentation.append("<");
            ruleRepresentation.append(this.minControl);
          }

          ruleRepresentation.append(" R ");

          if (this.maxControl!=null){
            ruleRepresentation.append(" ");
            if (this.maxControlIsStrict)ruleRepresentation.append(">");
            ruleRepresentation.append(this.maxControl);
          }
          if (this.maxSpec!=null){
            ruleRepresentation.append(" ");            
            if (this.maxSpecIsStrict)ruleRepresentation.append(">");
            ruleRepresentation.append(this.maxSpec);
          }
          this.quantitativeRuleRepresentation=ruleRepresentation.toString();
          this.ruleRepresentation=this.quantitativeRuleRepresentation;
          break;
        default:
          errorCode = "DataSample_SampleAnalysisResult_SpecRuleNotImplemented";
          errorDetailVariables = LPArray.addValueToArray1D(errorDetailVariables, new Object[]{limitId.toString(), LPPlatform.buildSchemaName(LPPlatform.SCHEMA_CONFIG, schemaPrefix), ruleType});
          return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, errorCode, errorDetailVariables);        
      }
      return new Object[]{LPPlatform.LAB_TRUE, ruleBuilder.toString()};
    }

    /**
     *
     * @param schemaPrefix
     * @param sampleSpecCode
     * @param sampleSpecCodeVersion
     * @param sampleSpecVariationName
     * @param analysis
     * @param methodName
     * @param methodVersion
     * @param paramName
     * @param fieldsToRetrieve
     * @return
     */
    public static Object[][] getSpecLimitLimitIdFromSpecVariables(String schemaPrefix, String sampleSpecCode, Integer sampleSpecCodeVersion,
            String sampleSpecVariationName, String analysis, String methodName, Integer methodVersion, String paramName, String[] fieldsToRetrieve){
        return Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsCnfg.SpecLimits.TBL.getName(), 
                new String[]{TblsCnfg.SpecLimits.FLD_CODE.getName(), TblsCnfg.SpecLimits.FLD_CONFIG_VERSION.getName(), TblsCnfg.SpecLimits.FLD_VARIATION_NAME.getName(), 
                    TblsCnfg.SpecLimits.FLD_ANALYSIS.getName(), TblsCnfg.SpecLimits.FLD_METHOD_NAME.getName(), TblsCnfg.SpecLimits.FLD_METHOD_VERSION.getName(), 
                    TblsCnfg.SpecLimits.FLD_PARAMETER.getName()}, new Object[]{sampleSpecCode, sampleSpecCodeVersion, sampleSpecVariationName, 
                      analysis, methodName, methodVersion, paramName}, 
                fieldsToRetrieve);
//                new String[]{TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName(), TblsCnfg.SpecLimits.FLD_RULE_TYPE.getName(), TblsCnfg.SpecLimits.FLD_RULE_VARIABLES.getName(), TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName(), 
//                    TblsCnfg.SpecLimits.FLD_UOM.getName(), TblsCnfg.SpecLimits.FLD_UOM_CONVERSION_MODE.getName()});         
    }
  /**
   * @return the qualitativeRule
   */
  public String getQualitativeRule() {
    return qualitativeRule;
  }

  /**
   * @return the qualitativeRuleValues
   */
  public String getQualitativeRuleValues() {
    return qualitativeRuleValues;
  }

  /**
   * @return the ruleIsQuantitative
   */
  public Boolean getRuleIsQuantitative() {
    return ruleIsQuantitative;
  }

  /**
   * @return the ruleIsQualitative
   */
  public Boolean getRuleIsQualitative() {
    return ruleIsQualitative;
  }

  /**
   * @return the minSpec
   */
  public BigDecimal getMinSpec() {
    return minSpec;
  }

  /**
   * @return the minSpecIsStrict
   */
  public Boolean getMinSpecIsStrict() {
    return minSpecIsStrict;
  }

  /**
   * @return the maxSpec
   */
  public BigDecimal getMaxSpec() {
    return maxSpec;
  }

  /**
   * @return the maxSpecIsStrict
   */
  public Boolean getMaxSpecIsStrict() {
    return maxSpecIsStrict;
  }

  /**
   * @return the minControl
   */
  public BigDecimal getMinControl() {
    return minControl;
  }

  /**
   * @return the minControlIsStrict
   */
  public Boolean getMinControlIsStrict() {
    return minControlIsStrict;
  }

  /**
   * @return the maxControl
   */
  public BigDecimal getMaxControl() {
    return maxControl;
  }

  /**
   * @return the maxControlIsStrict
   */
  public Boolean getMaxControlIsStrict() {
    return maxControlIsStrict;
  }

  /**
   * @return the quantitativeHasControl
   */
  public Boolean getQuantitativeHasControl() {
    return quantitativeHasControl;
  }
  /**
   * @return the ruleRepresentation independently of being quanti or auqlitative (Use the Quant or Qual get when concrete is a need)
   */
  public String getRuleRepresentation() {
    return ruleRepresentation;
  }
  /**
   * @return the quantitativeRuleRepresentation
   */
  public String getQuantitativeRuleRepresentation() {
    return quantitativeRuleRepresentation;
  }

  /**
   * @return the qualitativeRuleRepresentation
   */
  public String getQualitativeRuleRepresentation() {
    return qualitativeRuleRepresentation;
  }
  /**
   * @return the qualitativeRuleSeparator
   */
  public String getQualitativeRuleSeparator() {
    return qualitativeRuleSeparator;
  }

  /**
   * @return the qualitativeRuleListName
   */
  public String getQualitativeRuleListName() {
    return qualitativeRuleListName;
  }
    
} 
