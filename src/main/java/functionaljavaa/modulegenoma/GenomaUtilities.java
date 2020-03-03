/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.modulegenoma;

import com.labplanet.servicios.modulegenoma.TblsGenomaData;
import databases.Rdbms;
import java.util.Arrays;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class GenomaUtilities {
    
    public static Object[] addObjectToUnstructuredField(String schemaPrefix, String schemaType, String tableName, String[] tableKeyFieldName, Object[] tableKeyFieldValue, String unstructuredFieldName, String newObjectId, String newObjectInfoToStore){
        String separator = "*";
        String[] sampleInfoFieldsToRetrieve = new String[]{unstructuredFieldName};
        Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, schemaType), tableName, 
                tableKeyFieldName, tableKeyFieldValue, sampleInfoFieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())) {
            return LPArray.array2dTo1d(sampleInfo);
        }
        String familyIndividuals = LPNulls.replaceNull(sampleInfo[0][0]).toString();
        if (familyIndividuals.length() > 0) {
            familyIndividuals = familyIndividuals + "|";
        }
        familyIndividuals = familyIndividuals + newObjectId.toString();
        String[] updFieldName = new String[]{unstructuredFieldName};
        Object[] updFieldValue = new Object[]{familyIndividuals};
        Object[] updateFamilyIndividuals = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, schemaType), tableName, 
                updFieldName, updFieldValue, tableKeyFieldName, tableKeyFieldValue);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(updateFamilyIndividuals[0].toString()))
            updateFamilyIndividuals=LPArray.addValueToArray1D(updateFamilyIndividuals, familyIndividuals);
        return updateFamilyIndividuals;        
    }

    public static Object[] removeObjectToUnstructuredField(String schemaPrefix, String schemaType, String tableName, String[] tableKeyFieldName, Object[] tableKeyFieldValue, 
            String unstructuredFieldName, String objectTableName, String newObjectId, String newObjectInfoToStore){
        String separator = "*";
        String[] sampleInfoFieldsToRetrieve = new String[]{unstructuredFieldName};
        Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, schemaType), tableName, 
                tableKeyFieldName, tableKeyFieldValue, sampleInfoFieldsToRetrieve);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())) {
            return LPArray.array2dTo1d(sampleInfo);
        }
        String familyIndividuals = LPNulls.replaceNull(sampleInfo[0][0]).toString();
        Integer samplePosic = familyIndividuals.indexOf(newObjectId.toString());
        if (samplePosic == -1) {
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, objectTableName+" <*1*> not found in "+tableName+" <*2*> for procedure <*3*>.", new Object[]{newObjectId, Arrays.toString(tableKeyFieldValue), schemaPrefix});
        }
        String samplePosicInfo = familyIndividuals.substring(samplePosic, samplePosic + newObjectInfoToStore.toString().length());
        String[] samplePosicInfoArr = samplePosicInfo.split("\\*");
        if (samplePosicInfoArr.length != 1) {
            return LPPlatform.trapMessage(LPPlatform.LAB_FALSE, " removeObjectToUnstructuredField cannot parse the info for the "+tableName+" <*1*> when there are more than 1 pieces of info. Family individual info is <*2*> for procedure <*3*>.", new Object[]{samplePosicInfo, familyIndividuals, schemaPrefix});
        }

        if (samplePosic == 0) {
            if (familyIndividuals.length() == samplePosicInfo.length()) {
                familyIndividuals = familyIndividuals.substring(samplePosic + samplePosicInfo.length());
            } else {
                familyIndividuals = familyIndividuals.substring(samplePosic + samplePosicInfo.length() + 1);
            }
        } else {
            familyIndividuals = familyIndividuals.substring(0, samplePosic - 1) + familyIndividuals.substring(samplePosic + samplePosicInfo.length());
        }
        String[] updFieldName = new String[]{unstructuredFieldName};
        Object[] updFieldValue = new Object[]{familyIndividuals};
        Object[] updateFamilyIndividuals = Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, schemaType), tableName, 
                updFieldName, updFieldValue, tableKeyFieldName, tableKeyFieldValue);
        if (LPPlatform.LAB_TRUE.equalsIgnoreCase(updateFamilyIndividuals[0].toString()))
            updateFamilyIndividuals=LPArray.addValueToArray1D(updateFamilyIndividuals, familyIndividuals);
        return updateFamilyIndividuals;
    }
    
}
