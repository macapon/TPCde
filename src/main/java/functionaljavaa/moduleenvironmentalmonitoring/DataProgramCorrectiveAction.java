/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.moduleenvironmentalmonitoring;

import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitProcedure;
import databases.Rdbms;
import databases.TblsData;
import databases.Token;
import functionaljavaa.materialspec.ConfigSpecRule;
import functionaljavaa.parameter.Parameter;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author Administrator
 */
public class DataProgramCorrectiveAction {

    /**
     *
     * @param schemaPrefix
     * @param token
     * @param resultId
     * @param sampleFieldNames
     * @param sampleFieldValues
     * @param sarFieldNames
     * @param sarFieldValues
     * @return
     */
    public static Object[] createNew(String schemaPrefix, Token token, Integer resultId, String[] sampleFieldNames, Object[] sampleFieldValues, String[] sarFieldNames, Object[] sarFieldValues){    
    String statusFirst=Parameter.getParameterBundle(schemaPrefix+"-"+LPPlatform.SCHEMA_DATA, "programCorrectiveAction_statusFirst");
    String[] sampleFldsToGet= new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_PROGRAM_NAME.getName(), 
      TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_LOCATION_NAME.getName(), TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_AREA.getName()};
    String[] sampleAnalysisResultToGet= new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_RESULT_ID.getName(),
      TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_TEST_ID.getName(), TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_SPEC_EVAL.getName(),
      TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_SPEC_EVAL_DETAIL.getName(), TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_LIMIT_ID.getName(),
      TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_ANALYSIS.getName(), TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_METHOD_NAME.getName(),
      TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_METHOD_VERSION.getName(), TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_PARAM_NAME.getName()};
    String[] missingFields=new String[0];
    String[] myFldName=new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_PROGRAM_NAME.getName()};    
    Object[] myFldValue=new Object[]{""};        
    for (TblsEnvMonitProcedure.ProgramCorrectiveAction obj: TblsEnvMonitProcedure.ProgramCorrectiveAction.values()){
      if (!"TBL".equalsIgnoreCase(obj.name())){
        Integer posicInArray=LPArray.valuePosicInArray(sarFieldNames, obj.getName());
        if (posicInArray==-1){
          posicInArray=LPArray.valuePosicInArray(sampleFieldNames, obj.getName());
          if (posicInArray>-1){
            myFldName=LPArray.addValueToArray1D(myFldName, obj.getName());
            myFldValue=LPArray.addValueToArray1D(myFldValue, sampleFieldValues[posicInArray]);            
          }
        }else{
          myFldName=LPArray.addValueToArray1D(myFldName, obj.getName());
          myFldValue=LPArray.addValueToArray1D(myFldValue, sarFieldValues[posicInArray]);
        }
      } 
    }  
    Integer sampleId=-999;
    String programName="";
    Integer posicInArray=LPArray.valuePosicInArray(sampleFieldNames, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_PROGRAM_NAME.getName());
    if (posicInArray==-1){
      posicInArray=LPArray.valuePosicInArray(sampleFieldNames, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_SAMPLE_ID.getName());
      if (posicInArray==-1) return new Object[]{LPPlatform.LAB_FALSE};
      sampleId=Integer.valueOf(LPNulls.replaceNull(sampleFieldValues[posicInArray].toString()));
      Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
              new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, 
              new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_PROGRAM_NAME.getName()});
      if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())){return LPArray.array2dTo1d(sampleInfo);}
      programName=sampleInfo[0][0].toString();
    }else{programName=sampleFieldValues[posicInArray].toString();}
    
    myFldValue[0]=programName;
    posicInArray=LPArray.valuePosicInArray(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_RESULT_ID.getName());
    if (posicInArray==-1){
      myFldName=LPArray.addValueToArray1D(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_RESULT_ID.getName());
      myFldValue=LPArray.addValueToArray1D(myFldValue, resultId);
    }
    Object[][] sampleInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
            new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{sampleId}, sampleFldsToGet);
    for (int iFld=0;iFld<sampleFldsToGet.length;iFld++){
      String currFld=sampleFldsToGet[iFld];
      posicInArray=LPArray.valuePosicInArray(myFldName, currFld);
      if (posicInArray==-1){
        myFldName=LPArray.addValueToArray1D(myFldName, currFld);
        myFldValue=LPArray.addValueToArray1D(myFldValue, sampleInfo[0][iFld]);      
      }else{myFldValue[posicInArray]=sampleInfo[0][iFld];}      
    }
    Object[][] resultInfo=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsData.SampleAnalysisResult.TBL.getName(), 
            new String[]{TblsData.SampleAnalysisResult.FLD_RESULT_ID.getName()}, new Object[]{resultId}, sampleAnalysisResultToGet);
    for (int iFld=0;iFld<sampleAnalysisResultToGet.length;iFld++){
      String currFld=sampleAnalysisResultToGet[iFld];
      posicInArray=LPArray.valuePosicInArray(myFldName, currFld);
      if (posicInArray==-1){
        myFldName=LPArray.addValueToArray1D(myFldName, currFld);
        myFldValue=LPArray.addValueToArray1D(myFldValue, resultInfo[0][iFld]);      
      }else{myFldValue[posicInArray]=resultInfo[0][iFld];}      
    }
    
    posicInArray=LPArray.valuePosicInArray(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_LIMIT_ID.getName());
    if (posicInArray>-1){
      Integer limitId =Integer.valueOf(myFldValue[posicInArray].toString()); 
      ConfigSpecRule specRule = new ConfigSpecRule();
      specRule.specLimitsRule(schemaPrefix, limitId, "");
      myFldName=LPArray.addValueToArray1D(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_SPEC_RULE_WITH_DETAIL.getName());
      myFldValue=LPArray.addValueToArray1D(myFldValue, specRule.getRuleRepresentation());      
    }
    posicInArray=LPArray.valuePosicInArray(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_STATUS.getName());
    if (posicInArray==-1){
      myFldName=LPArray.addValueToArray1D(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_STATUS.getName());
      myFldValue=LPArray.addValueToArray1D(myFldValue, statusFirst);      
    }else{myFldValue[posicInArray]=statusFirst;}
    posicInArray=LPArray.valuePosicInArray(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_CREATED_BY.getName());
    if (posicInArray==-1){
      myFldName=LPArray.addValueToArray1D(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_CREATED_BY.getName());
      myFldValue=LPArray.addValueToArray1D(myFldValue, token.getPersonName());      
    }else{myFldValue[posicInArray]=token.getPersonName();}
    posicInArray=LPArray.valuePosicInArray(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_CREATED_ON.getName());
    if (posicInArray==-1){
      myFldName=LPArray.addValueToArray1D(myFldName, TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_CREATED_ON.getName());
      myFldValue=LPArray.addValueToArray1D(myFldValue, LPDate.getCurrentTimeStamp());      
    }else{myFldValue[posicInArray]=LPDate.getCurrentTimeStamp();}
    return Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE), TblsEnvMonitProcedure.ProgramCorrectiveAction.TBL.getName(), 
            myFldName, myFldValue);
  }
  
    /**
     *
     * @param schemaPrefix
     * @param token
     * @param correctiveActionId
     * @return
     */
    public static Object[] markAsCompleted(String schemaPrefix, Token token, Integer correctiveActionId){    
    String statusClosed=Parameter.getParameterBundle(schemaPrefix+"-"+LPPlatform.SCHEMA_DATA, "programCorrectiveAction_statusClosed");
    
    return Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_PROCEDURE), TblsEnvMonitProcedure.ProgramCorrectiveAction.TBL.getName(), 
            new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_STATUS.getName()}, 
            new Object[]{statusClosed}, 
            new String[]{TblsEnvMonitProcedure.ProgramCorrectiveAction.FLD_ID.getName()}, new Object[]{correctiveActionId});
  }  
}