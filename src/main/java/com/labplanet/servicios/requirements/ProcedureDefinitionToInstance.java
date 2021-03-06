/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.requirements;

import com.labplanet.servicios.proceduredefinition.ProcedureDefinitionAPI;
import com.labplanet.servicios.proceduredefinition.ProcedureDefinitionAPI.ProcedureDefinitionAPIEndpoints;
import databases.DbObjects;
import lbplanet.utilities.LPFrontEnd;
import databases.Rdbms;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPAPIArguments;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class ProcedureDefinitionToInstance extends HttpServlet {

    private static final Boolean  CREATE_SCHEMAS_AND_PROC_TBLS=false;
    private static final Boolean  PROC_DEPLOY_PROCEDURE_INFO=false;
    private static final Boolean  PROC_DEPLOY_PROCEDURE_USER_ROLES=false;
    private static final Boolean  PROC_DEPLOY_PROCEDURE_SOP_META_DATA=false;
    private static final Boolean  PROC_DEPLOY_ASSIGN_PROCEDURE_SOPS_TO_USERS=false;
    private static final Boolean  PROC_DEPLOY_PROCEDURE_EVENTS=false;
    private static final Boolean  PROC_DEPLOY_BUSINESS_RULES_PROPERTIES_FILES=false;
    private static final Boolean  PROC_DEPLOY_MODULE_TABLES_AND_FIELDS=true;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response=LPTestingOutFormat.responsePreparation(response);
        String fileContent = LPTestingOutFormat.getHtmlStyleHeader(this.getClass().getSimpleName(), "No File");
        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}   
        //String procName = "em-demo-a"; 
        //Integer procVersion = 1; 
        //String schemaPrefix = "em-demo-a";

        ProcedureDefinitionAPI.ProcedureDefinitionAPIEndpoints endPoint = ProcedureDefinitionAPIEndpoints.DEPLOY_REQUIREMENTS;
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());                
        String procName = argValues[0].toString(); //request.getParameter("procedureName"); //"process-us";         
        Integer procVersion= (Integer) argValues[1];
        String schemaPrefix=argValues[2].toString(); //request.getParameter("schemaPrefix"); //"process-us";
        String moduleName=argValues[3].toString();
        String[][] businessVariablesHeader = new String[][]{{"Business Rule", "Value"}                 
                            , {"Process Name", procName}, {"Process Version", procVersion.toString()}, {"Instance", schemaPrefix}
                            , {"CREATE_SCHEMAS_AND_PROC_TBLS", CREATE_SCHEMAS_AND_PROC_TBLS.toString()}
                            , {"PROC_DEPLOY_PROCEDURE_INFO", PROC_DEPLOY_PROCEDURE_INFO.toString()}
                            , {"PROC_DEPLOY_PROCEDURE_USER_ROLES", PROC_DEPLOY_PROCEDURE_USER_ROLES.toString()}
                            , {"PROC_DEPLOY_PROCEDURE_SOP_META_DATA", PROC_DEPLOY_PROCEDURE_SOP_META_DATA.toString()}    
                            , {"PROC_DEPLOY_ASSIGN_PROCEDURE_SOPS_TO_USERS", PROC_DEPLOY_ASSIGN_PROCEDURE_SOPS_TO_USERS.toString()}                
//                            , {"PROC_DISPLAY_PROC_INSTANCE_SOPS", PROC_DISPLAY_PROC_INSTANCE_SOPS.toString()}                
//                            , {"PROC_DEPLOYMENT_ENTIRE_PROCEDURE", PROC_DEPLOYMENT_ENTIRE_PROCEDURE.toString()}
//                            , {"PROC_DEPLOYMENT_CREATE_MISSING_PROC_EVENT_SOPS", PROC_DEPLOYMENT_CREATE_MISSING_PROC_EVENT_SOPS.toString()}
                
//                            , {"PROC_DEPLOYMENT_ASSIGN_USER_SOPS", PROC_DEPLOYMENT_ASSIGN_USER_SOPS.toString()}
                    };
        fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(businessVariablesHeader); 
        try (PrintWriter out = response.getWriter()) {
            
            if (CREATE_SCHEMAS_AND_PROC_TBLS){
                JSONObject createDBProcedureInfo = DbObjects.createModuleSchemas(schemaPrefix);
                String[][] createDBProcedureInfoTbl = new String[][]{{"Log for PROC_DEPLOY_PROCEDURE_INFO"},{createDBProcedureInfo.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureInfoTbl);
            }   

            
            if (PROC_DEPLOY_PROCEDURE_INFO){
                JSONObject createDBProcedureInfo = functionaljavaa.requirement.ProcedureDefinitionToInstance.createDBProcedureInfo(procName, procVersion, schemaPrefix);
                String[][] createDBProcedureInfoTbl = new String[][]{{"Log for PROC_DEPLOY_PROCEDURE_INFO"},{createDBProcedureInfo.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureInfoTbl);
            }   
            if (PROC_DEPLOY_PROCEDURE_USER_ROLES){
                JSONObject createDBProcedureUserRoles = functionaljavaa.requirement.ProcedureDefinitionToInstance.createDBPersonProfiles(procName, procVersion, schemaPrefix);
                String[][] createDBProcedureUserRolesTbl = new String[][]{{"Log for PROC_DEPLOY_PROCEDURE_USER_ROLES"},{createDBProcedureUserRoles.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureUserRolesTbl);
            }            
            if (PROC_DEPLOY_PROCEDURE_SOP_META_DATA){
                JSONObject createDBProcedureUserRoles = functionaljavaa.requirement.ProcedureDefinitionToInstance.createDBSopMetaDataAndUserSop(procName, procVersion, schemaPrefix);
                String[][] createDBProcedureUserRolesTbl = new String[][]{{"Log for PROC_DEPLOY_PROCEDURE_SOP_META_DATA"},{createDBProcedureUserRoles.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureUserRolesTbl);
            }       
            if (PROC_DEPLOY_ASSIGN_PROCEDURE_SOPS_TO_USERS){
                JSONObject createDBProcedureUserRoles = functionaljavaa.requirement.ProcedureDefinitionToInstance.addProcedureSOPtoUsers(procName, procVersion, schemaPrefix);
                String[][] createDBProcedureUserRolesTbl = new String[][]{{"Log for PROC_DEPLOY_ASSIGN_PROCEDURE_SOPS_TO_USERS"},{createDBProcedureUserRoles.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureUserRolesTbl);
            }  
            if (PROC_DEPLOY_PROCEDURE_EVENTS){
                JSONObject createDBProcedureEvents = functionaljavaa.requirement.ProcedureDefinitionToInstance.createDBProcedureEvents(procName, procVersion, schemaPrefix);
                String[][] createDBProcedureEventsDiagnostic = new String[][]{{"Log for PROC_DEPLOY_PROCEDURE_EVENTS"},{createDBProcedureEvents.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBProcedureEventsDiagnostic);                
            }
            if (PROC_DEPLOY_BUSINESS_RULES_PROPERTIES_FILES){
                
            }
            if (PROC_DEPLOY_MODULE_TABLES_AND_FIELDS){
                JSONObject createDBModuleTablesAndFields = functionaljavaa.requirement.ProcedureDefinitionToInstance.createDBModuleTablesAndFields(procName, procVersion, schemaPrefix, moduleName);
                String[][] createDBModuleTablesAndFieldsDiagnostic = new String[][]{{"Log for PROC_DEPLOY_MODULE_TABLES_AND_FIELDS"},{createDBModuleTablesAndFields.toJSONString()}};  
                fileContent = fileContent + LPTestingOutFormat.convertArrayInHtmlTable(createDBModuleTablesAndFieldsDiagnostic);                
            }
            
            fileContent=fileContent+LPTestingOutFormat.bodyEnd()+LPTestingOutFormat.htmlEnd();
            out.println(fileContent);            
        }
        Rdbms.closeRdbms();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
        try{
        processRequest(request, response);
        }catch(ServletException|IOException e){
            LPFrontEnd.servletReturnResponseError(request, response, e.getMessage(), new Object[]{}, null);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        try{
        processRequest(request, response);
        }catch(ServletException|IOException e){
            LPFrontEnd.servletReturnResponseError(request, response, e.getMessage(), new Object[]{}, null);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
