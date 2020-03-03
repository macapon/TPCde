/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.moduleenvironmentalmonitoring;

import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitData;
import databases.Rdbms;
import lbplanet.utilities.LPArray;
import java.util.Arrays;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author Administrator
 */
public class DataProgramProductionLot{
    /**
     *
     * @param schemaPrefix
   * @param lotName
     * @param fieldName
     * @param fieldValue
     * @param personName
     * @param userRole
     * @param appSessionId
     * @return
     */
    public static Object[] newProgramProductionLot(String schemaPrefix, String lotName, String[] fieldName, Object[] fieldValue, String personName, String userRole, Integer appSessionId) {
        Object[] newProjSample = new Object[0];
        String[] tblFlds=new String[0];
        for (TblsEnvMonitData.ProductionLot obj: TblsEnvMonitData.ProductionLot.values()){
          tblFlds=LPArray.addValueToArray1D(tblFlds, obj.getName());
        }        
        if (fieldName==null)fieldName=new String[0];
        for (String curFld: fieldName){
          if (curFld.length()>0 && LPArray.valuePosicInArray(tblFlds, curFld)==-1)return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, 
                  "productionLot_fieldNotFound", new Object[]{curFld, lotName, Arrays.toString(fieldName), Arrays.toString(fieldValue), schemaPrefix});
        }
        fieldName=LPArray.addValueToArray1D(fieldName, TblsEnvMonitData.ProductionLot.FLD_LOT_NAME.getName());
        fieldValue=LPArray.addValueToArray1D(fieldValue, lotName);
        Integer posicInArr=LPArray.valuePosicInArray(fieldName, TblsEnvMonitData.ProductionLot.FLD_CREATED_BY.getName());
        if (posicInArr==-1){
          fieldName=LPArray.addValueToArray1D(fieldName, TblsEnvMonitData.ProductionLot.FLD_CREATED_BY.getName());
          fieldValue=LPArray.addValueToArray1D(fieldValue, personName);
        }else{fieldValue[posicInArr]=personName;}
        posicInArr=LPArray.valuePosicInArray(fieldName, TblsEnvMonitData.ProductionLot.FLD_CREATED_ON.getName());
        if (posicInArr==-1){
          fieldName=LPArray.addValueToArray1D(fieldName, TblsEnvMonitData.ProductionLot.FLD_CREATED_ON.getName());
          fieldValue=LPArray.addValueToArray1D(fieldValue, LPDate.getCurrentTimeStamp());
        }else{fieldValue[posicInArr]=LPDate.getCurrentTimeStamp();}
        newProjSample=Rdbms.insertRecordInTable(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.ProductionLot.TBL.getName(), fieldName, fieldValue);
        return newProjSample;
    }

    /**
     *
     * @param schemaPrefix
     * @param lotName
     * @param personName
     * @param userRole
     * @param appSessionId
     * @return
     */
    public static Object[] activateProgramProductionLot(String schemaPrefix, String lotName, String personName, String userRole, Integer appSessionId) {
      String[] fieldName=new String[]{TblsEnvMonitData.ProductionLot.FLD_ACTIVE.getName()};
      Object[] fieldValue=new Object[]{true};
      return updateProgramProductionLot(schemaPrefix, lotName, fieldName, fieldValue, personName, userRole, appSessionId);
    }    

    /**
     *
     * @param schemaPrefix
     * @param lotName
     * @param personName
     * @param userRole
     * @param appSessionId
     * @return
     */
    public static Object[] deactivateProgramProductionLot(String schemaPrefix, String lotName, String personName, String userRole, Integer appSessionId) {
      String[] fieldName=new String[]{TblsEnvMonitData.ProductionLot.FLD_ACTIVE.getName()};
      Object[] fieldValue=new Object[]{false};
      return updateProgramProductionLot(schemaPrefix, lotName, fieldName, fieldValue, personName, userRole, appSessionId);
    }        
    private static Object[] updateProgramProductionLot(String schemaPrefix, String lotName, String[] fieldName, Object[] fieldValue, String personName, String userRole, Integer appSessionId) {
      Object[] updProjSample = new Object[0];
      updProjSample=Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.ProductionLot.TBL.getName(), 
              fieldName, fieldValue, new String[]{TblsEnvMonitData.ProductionLot.FLD_LOT_NAME.getName()}, new Object[]{lotName});
      return updProjSample;            
    }
}
