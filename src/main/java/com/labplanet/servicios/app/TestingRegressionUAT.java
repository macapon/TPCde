/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import databases.Rdbms;
import databases.TblsTesting;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import functionaljavaa.testingscripts.LPTestingParams;
import functionaljavaa.testingscripts.LPTestingParams.TestingServletsConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class TestingRegressionUAT extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)            throws ServletException, IOException {
        response = LPTestingOutFormat.responsePreparation(response);        
        String saveDirectory="D:\\LP\\"; //TESTING_FILES_PATH;
        String schemaPrefix="em-demo-a";
        Integer scriptId=2;
        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}     
        Object[][] scriptTblInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_TESTING), TblsTesting.Script.TBL.getName(), 
                new String[]{TblsTesting.Script.FLD_SCRIPT_ID.getName()}, new Object[]{scriptId}, 
                new String[]{TblsTesting.Script.FLD_TESTER_NAME.getName(), TblsTesting.Script.FLD_EVAL_NUM_ARGS.getName()},
                new String[]{TblsTesting.Script.FLD_SCRIPT_ID.getName()});
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(scriptTblInfo[0][0].toString())){
            Logger.getLogger("Script "+scriptId.toString()+" Not found"); 
            return;
        }
        String testerName = scriptTblInfo[0][0].toString();
        Integer numEvalArgs = 0;
        if (scriptTblInfo[0][1]!=null && scriptTblInfo[0][1].toString().length()>0) numEvalArgs=Integer.valueOf(scriptTblInfo[0][1].toString());
        
        request.setAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_PATH, saveDirectory+"\\");
        request.setAttribute(LPTestingParams.TESTING_SOURCE, "DB");
        request.setAttribute(LPTestingParams.NUM_EVAL_ARGS, numEvalArgs);
        request.setAttribute(LPTestingParams.SCRIPT_ID, scriptId);
        request.setAttribute(LPTestingParams.SCHEMA_PREFIX, schemaPrefix);
        request.setAttribute(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN, "eyJ1c2VyREIiOiJsYWJwbGFuZXQiLCJlU2lnbiI6ImhvbGEiLCJ1c2VyREJQYXNzd29yZCI6Imxhc2xlY2h1Z2FzIiwidXNlcl9wcm9jZWR1cmVzIjoiW2VtLWRlbW8tYSwgcHJvY2Vzcy11cywgcHJvY2Vzcy1ldSwgZ2Vub21hLTFdIiwidHlwIjoiSldUIiwiYXBwU2Vzc2lvbklkIjoiMjk4NiIsImFwcFNlc3Npb25TdGFydGVkRGF0ZSI6IlR1ZSBNYXIgMTcgMDI6Mzg6MTkgQ0VUIDIwMjAiLCJ1c2VyUm9sZSI6ImNvb3JkaW5hdG9yIiwiYWxnIjoiSFMyNTYiLCJpbnRlcm5hbFVzZXJJRCI6IjEifQ.eyJpc3MiOiJMYWJQTEFORVRkZXN0cmFuZ2lzSW5UaGVOaWdodCJ9.xiT6CxNcoFKAiE2moGhMOsxFwYjeyugdvVISjUUFv0Y");
         
        TestingServletsConfig endPoints = TestingServletsConfig.valueOf(testerName);

        switch (endPoints){
        case NODB_SCHEMACONFIG_SPECQUAL_RULEFORMAT:
        case NODB_SCHEMACONFIG_SPECQUAL_RESULTCHECK:
        case NODB_SCHEMACONFIG_SPECQUANTI_RULEFORMAT:
        case NODB_SCHEMACONFIG_SPECQUANTI_RESULTCHECK:
        case DB_SCHEMADATA_ENVMONIT_SAMPLES:
            RequestDispatcher rd = request.getRequestDispatcher(endPoints.getServletUrl());
            rd.forward(request,response);   
            return;                       
        default:
            Logger.getLogger("Tester name not recognized, "+testerName+". The tester cannot be completed"); 
            return;
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            processRequest(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(TestingRegressionUAT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            processRequest(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(TestingRegressionUAT.class.getName()).log(Level.SEVERE, null, ex);
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
