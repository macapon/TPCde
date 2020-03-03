/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.modulegenoma;

import com.labplanet.servicios.modulegenoma.TblsGenomaConfig;
import static functionaljavaa.modulegenoma.GenomaDataStudyFamily.isStudyFamilyOpenToChanges;
import static functionaljavaa.modulegenoma.GenomaUtilities.addObjectToUnstructuredField;
import static functionaljavaa.modulegenoma.GenomaUtilities.removeObjectToUnstructuredField;
import databases.Token;
import functionaljavaa.modulegenoma.GenomaDataAudit;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class GenomaConfigVariables {
    
public static Object[] variableSetAddVariable(String schemaPrefix, Token token, String variableSetName, String variableName) {
    
    Object[] updateFamilyIndividuals=addObjectToUnstructuredField(schemaPrefix, LPPlatform.SCHEMA_CONFIG, TblsGenomaConfig.VariablesSet.TBL.getName(), 
            new String[]{TblsGenomaConfig.VariablesSet.FLD_NAME.getName()}, new Object[]{variableSetName}, 
            TblsGenomaConfig.VariablesSet.FLD_VARIABLES_LIST.getName(), variableName.toString(), variableName.toString());  
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        return updateFamilyIndividuals;
    }
/*    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.STUDY_FAMILY_ADDED_INDIVIDUAL.toString(), TblsGenomaConfig.VariablesSet.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(new String[]{TblsGenomaConfig.VariablesSet.FLD_VARIABLES_LIST.getName()}, new Object[]{updateFamilyIndividuals[updateFamilyIndividuals.length-1]}, ":"), null);
    }*/
    return updateFamilyIndividuals;
}

public static Object[] variableSetRemoveVariable(String schemaPrefix, Token token, String variableSetName, String variableName) {
    
    Object[] updateFamilyIndividuals=removeObjectToUnstructuredField(schemaPrefix, LPPlatform.SCHEMA_CONFIG, TblsGenomaConfig.VariablesSet.TBL.getName(), 
            new String[]{TblsGenomaConfig.VariablesSet.FLD_NAME.getName()}, new Object[]{variableSetName}, 
            TblsGenomaConfig.VariablesSet.FLD_VARIABLES_LIST.getName(), TblsGenomaConfig.Variables.TBL.getName(), variableName.toString(), variableName.toString());  
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        return updateFamilyIndividuals;
    }
    
/*    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(updateFamilyIndividuals[0].toString())) {
        GenomaDataAudit.studyAuditAdd(schemaPrefix, token, GenomaDataAudit.StudyAuditEvents.STUDY_FAMILY_REMOVED_INDIVIDUAL.toString(), TblsGenomaConfig.VariablesSet.TBL.getName(), familyName, 
            studyName, null, LPArray.joinTwo1DArraysInOneOf1DString(new String[]{TblsGenomaConfig.VariablesSet.FLD_VARIABLES_LIST.getName()}, new Object[]{updateFamilyIndividuals[updateFamilyIndividuals.length-1]}, ":"), null);
    }*/
    return updateFamilyIndividuals;
}
    
    
}
