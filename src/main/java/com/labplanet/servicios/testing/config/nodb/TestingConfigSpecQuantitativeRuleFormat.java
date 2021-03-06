/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.testing.config.nodb;

import databases.Rdbms;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import functionaljavaa.materialspec.ConfigSpecRule;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import functionaljavaa.testingscripts.LPTestingParams;
import functionaljavaa.testingscripts.TestingAssert;
import functionaljavaa.testingscripts.TestingAssertSummary;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class TestingConfigSpecQuantitativeRuleFormat extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException exception not handled
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)            throws ServletException, IOException {
        response = LPTestingOutFormat.responsePreparation(response);        
        ConfigSpecRule mSpec = new ConfigSpecRule();
        
        TestingAssertSummary tstAssertSummary = new TestingAssertSummary();

        String csvPathName =(String) request.getAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_PATH);
        String csvFileName =(String) request.getAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_NAME);
        if ("".equals(csvPathName) || csvPathName==null){
            csvFileName = LPTestingParams.TestingServletsConfig.NODB_SCHEMACONFIG_SPECQUANTI_RULEFORMAT.getTesterFileName();                         
            csvPathName = LPTestingOutFormat.TESTING_FILES_PATH; }
        csvPathName = csvPathName+csvFileName; 
        String csvFileSeparator=LPTestingOutFormat.TESTING_FILES_FIELD_SEPARATOR;
        
        Object[][] csvFileContent = LPArray.convertCSVinArray(csvPathName, csvFileSeparator); 
        StringBuilder fileContentBuilder = new StringBuilder(0);
        fileContentBuilder.append(LPTestingOutFormat.getHtmlStyleHeader(this.getClass().getSimpleName(), csvFileName));                
        try (PrintWriter out = response.getWriter()) {            
            HashMap<String, Object> csvHeaderTags = LPTestingOutFormat.getCSVHeader(LPArray.convertCSVinArray(csvPathName, "="));
            if (csvHeaderTags.containsKey(LPPlatform.LAB_FALSE)){
                fileContentBuilder.append("There are missing tags in the file header: ").append(csvHeaderTags.get(LPPlatform.LAB_FALSE));
                out.println(fileContentBuilder.toString()); 
                return;
            }            
            
            Integer numEvaluationArguments = Integer.valueOf(csvHeaderTags.get(LPTestingOutFormat.FILEHEADER_NUM_EVALUATION_ARGUMENTS).toString());   
            Integer numHeaderLines = Integer.valueOf(csvHeaderTags.get(LPTestingOutFormat.FILEHEADER_NUM_HEADER_LINES_TAG_NAME).toString());   
            
            String table1Header = csvHeaderTags.get(LPTestingOutFormat.FILEHEADER_TABLE_NAME_TAG_NAME+"1").toString();               
            StringBuilder fileContentTable1Builder = new StringBuilder(0);
            fileContentTable1Builder.append(LPTestingOutFormat.createTableWithHeader(table1Header, numEvaluationArguments));

            String table2Header = csvHeaderTags.get(LPTestingOutFormat.FILEHEADER_TABLE_NAME_TAG_NAME+"2").toString();            
            StringBuilder fileContentTable2Builder = new StringBuilder(0);
            fileContentTable2Builder.append(LPTestingOutFormat.createTableWithHeader(table2Header, numEvaluationArguments));
            
            for (Integer iLines=numHeaderLines;iLines<csvFileContent.length;iLines++){
                tstAssertSummary.increaseTotalTests();
                    
                TestingAssert tstAssert = new TestingAssert(csvFileContent[iLines], numEvaluationArguments);
                
                if (csvFileContent[iLines][0]==null){tstAssertSummary.increasetotalLabPlanetBooleanUndefined();}
                if (csvFileContent[iLines][1]==null){tstAssertSummary.increasetotalLabPlanetErrorCodeUndefined();}

                Integer lineNumCols = csvFileContent[0].length-1;
                Float minSpec = null;
                if (lineNumCols>=numEvaluationArguments)
                    {minSpec = LPTestingOutFormat.csvExtractFieldValueFloat(csvFileContent[iLines][numEvaluationArguments]);}
                Float minControl = null;
                if (lineNumCols>=numEvaluationArguments+1)
                    {minControl = LPTestingOutFormat.csvExtractFieldValueFloat(csvFileContent[iLines][numEvaluationArguments+1]);}
                Float maxControl = null;
                if (lineNumCols>=numEvaluationArguments+2)
                    {maxControl = LPTestingOutFormat.csvExtractFieldValueFloat(csvFileContent[iLines][numEvaluationArguments+2]);}
                Float maxSpec = null;
                if (lineNumCols>=numEvaluationArguments+3)
                    {maxSpec = LPTestingOutFormat.csvExtractFieldValueFloat(csvFileContent[iLines][numEvaluationArguments+3]);}
                    
                Object[] resSpecEvaluation = new Object[0];                
                if (minControl==null){
                    fileContentTable1Builder.append(LPTestingOutFormat.rowAddFields(new Object[]{iLines-numHeaderLines+1, minSpec, maxSpec}));
                    resSpecEvaluation = mSpec.specLimitIsCorrectQuantitative(minSpec,maxSpec, minControl, maxControl);
                }else{
                    fileContentTable2Builder.append(LPTestingOutFormat.rowAddFields(new Object[]{iLines-numHeaderLines+1, minSpec, minControl, maxControl, maxSpec}));
                    resSpecEvaluation = mSpec.specLimitIsCorrectQuantitative(minSpec,maxSpec, minControl, maxControl);
                }        
                if (numEvaluationArguments==0){                    
                    if (minControl==null){
                        fileContentTable1Builder.append(LPTestingOutFormat.rowAddField(Arrays.toString(resSpecEvaluation)));                     
                    }else{
                        fileContentTable2Builder.append(LPTestingOutFormat.rowAddField(Arrays.toString(resSpecEvaluation)));                     
                    }
                }                                
                if (numEvaluationArguments>0){                    
                    Object[] evaluate = tstAssert.evaluate(numEvaluationArguments, tstAssertSummary, resSpecEvaluation);
                    if (minControl==null){
                        fileContentTable1Builder.append(LPTestingOutFormat.rowAddFields(evaluate));                        
                        fileContentTable1Builder.append(LPTestingOutFormat.rowEnd());                                                
                    }else{
                        fileContentTable2Builder.append(LPTestingOutFormat.rowAddFields(evaluate));                        
                        fileContentTable2Builder.append(LPTestingOutFormat.rowEnd());                                                
                    }
                }
            }    
            tstAssertSummary.notifyResults();
            fileContentTable1Builder.append(LPTestingOutFormat.tableEnd());                                                
            fileContentTable2Builder.append(LPTestingOutFormat.tableEnd());                                                
            if (numEvaluationArguments>0){
                String fileContentSummary = LPTestingOutFormat.createSummaryTable(tstAssertSummary, numEvaluationArguments);
                fileContentBuilder.append(fileContentSummary).append(fileContentTable1Builder).append(fileContentTable2Builder);
            }
            fileContentBuilder.append(LPTestingOutFormat.bodyEnd()).append(LPTestingOutFormat.htmlEnd());
            out.println(fileContentBuilder.toString());            
            LPTestingOutFormat.createLogFile(csvPathName, fileContentBuilder.toString());
            tstAssertSummary=null; mSpec=null;
        }
        catch(IOException error){
            tstAssertSummary=null; mSpec=null;
            String exceptionMessage = error.getMessage();     
            LPFrontEnd.servletReturnResponseError(request, response, exceptionMessage, null, null);                    
        } finally {
            // release database resources
            try {
                Rdbms.closeRdbms();   
            } catch (Exception ex) {Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
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
