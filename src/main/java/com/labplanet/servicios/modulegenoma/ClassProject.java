/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulegenoma;

import com.labplanet.servicios.modulegenoma.GenomaProjectAPI.GenomaProjectAPIEndPoints;
import databases.Token;
import functionaljavaa.modulegenoma.GenomaDataProject;
import functionaljavaa.modulegenoma.GenomaDataStudy;
import functionaljavaa.responserelatedobjects.RelatedObjects;
import javax.servlet.http.HttpServletRequest;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class ClassProject {
    private Object[] messageDynamicData=new Object[]{};
    private RelatedObjects relatedObj=RelatedObjects.getInstance();
    private Boolean endpointExists=true;
    private Object[] diagnostic=new Object[0];
    private Boolean functionFound=false;

    public ClassProject(HttpServletRequest request, Token token, String schemaPrefix, GenomaProjectAPIEndPoints endPoint){
        RelatedObjects rObj=RelatedObjects.getInstance();

        GenomaDataProject prj = new GenomaDataProject();
        GenomaDataStudy prjStudy = new GenomaDataStudy();
        String projectName = "";
        
        Object[] actionDiagnoses = null;
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());        
        this.functionFound=true;
            switch (endPoint){
                case PROJECT_NEW:
                case PROJECT_UPDATE:
                    projectName = argValues[0].toString();
                    String fieldName=argValues[1].toString();
                    String fieldValue=argValues[2].toString();
                    String[] fieldNames=new String[0];
                    Object[] fieldValues=new Object[0];
                    if (fieldName!=null && fieldName.length()>0) fieldNames = fieldName.split("\\|");                                            
                    if (fieldValue!=null && fieldValue.length()>0) fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    if ("PROJECT_NEW".equalsIgnoreCase(endPoint.getName()))
                        actionDiagnoses= prj.createProject(schemaPrefix, token, projectName, fieldNames, fieldValues,  false);
                    if ("PROJECT_UPDATE".equalsIgnoreCase(endPoint.getName()))
                        actionDiagnoses= prj.projectUpdate(schemaPrefix, token, projectName, fieldNames, fieldValues);
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsGenomaData.Project.TBL.getName(), TblsGenomaData.Project.TBL.getName(), projectName);                
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{projectName, schemaPrefix});                    
                    this.messageDynamicData=new Object[]{projectName, schemaPrefix};
                    break;
                case PROJECT_ACTIVATE:
                case PROJECT_DEACTIVATE:
                    projectName = argValues[0].toString();
                    if ("PROJECT_ACTIVATE".equalsIgnoreCase(endPoint.getName()))
                        actionDiagnoses =prj.projectActivate(schemaPrefix, token, projectName);
                    else if ("PROJECT_DEACTIVATE".equalsIgnoreCase(endPoint.getName()))
                        actionDiagnoses =prj.projectDeActivate(schemaPrefix, token, projectName);                    
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsGenomaData.Project.TBL.getName(), TblsGenomaData.Project.TBL.getName(), projectName);                
                    break;
                case PROJECT_ADD_USER:
                case PROJECT_REMOVE_USER:
                case PROJECT_CHANGE_USER_ROLE:
                case PROJECT_USER_ACTIVATE:
                case PROJECT_USER_DEACTIVATE:
                    projectName = argValues[0].toString();
                    String userName=argValues[1].toString();
                    String userRole=argValues[2].toString();
                    actionDiagnoses =prj.projectUserManagement(schemaPrefix, token, endPoint.getName(), projectName, userName, userRole);
                    this.messageDynamicData=new Object[]{projectName, userName, userRole, schemaPrefix};
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsGenomaData.Project.TBL.getName(), TblsGenomaData.Project.TBL.getName(), projectName);                                    
                    break;
                case STUDY_NEW:
                    projectName = argValues[0].toString();
                    String studyName = argValues[1].toString();
                    fieldName=argValues[2].toString();
                    fieldValue=argValues[3].toString();
                    fieldNames=new String[0];
                    fieldValues=new Object[0];
                    if (fieldName!=null && fieldName.length()>0) fieldNames = fieldName.split("\\|");    
                    
                    if (fieldValue!=null && fieldValue.length()>0) 
                        fieldValues=TblsGenomaData.Study.convertStringWithDataTypeToObjectArray(fieldNames, fieldValue.split("\\|"));
//                        fieldValues = LPArray.convertStringWithDataTypeToObjectArray(fieldValue.split("\\|"));                                                                                
                    actionDiagnoses= prjStudy.createStudy(schemaPrefix, token, studyName, projectName, fieldNames, fieldValues,  false);
                    rObj.addSimpleNode(LPPlatform.SCHEMA_APP, TblsGenomaData.Study.TBL.getName(), TblsGenomaData.Study.TBL.getName(), studyName);                
                    if (LPPlatform.LAB_TRUE.equalsIgnoreCase(actionDiagnoses[0].toString()))
                        actionDiagnoses=LPPlatform.trapMessage(LPPlatform.LAB_TRUE, endPoint.getSuccessMessageCode(), new Object[]{studyName, schemaPrefix});                    
                    this.messageDynamicData=new Object[]{projectName, studyName, schemaPrefix};
                    break;
                default:
                    break;
            }    
        this.diagnostic=actionDiagnoses;
        this.relatedObj=rObj;
        //this.messageDynamicData=dynamicDataObjects;
        rObj.killInstance();
    }
    
    /**
     * @return the messageDynamicData
     */
    public Object[] getMessageDynamicData() {
        return messageDynamicData;
    }

    /**
     * @return the relatedObj
     */
    public RelatedObjects getRelatedObj() {
        return relatedObj;
    }

    /**
     * @return the endpointExists
     */
    public Boolean getEndpointExists() {
        return endpointExists;
    }

    /**
     * @return the diagnostic
     */
    public Object[] getDiagnostic() {
        return diagnostic;
    }

    /**
     * @return the functionFound
     */
    public Boolean getFunctionFound() {
        return functionFound;
    }
    
}
