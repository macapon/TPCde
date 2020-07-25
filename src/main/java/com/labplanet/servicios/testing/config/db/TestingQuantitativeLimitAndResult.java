/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.testing.config.db;

import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;
import databases.Rdbms;
import databases.TblsCnfg;
import functionaljavaa.materialspec.ConfigSpecRule;
import functionaljavaa.materialspec.DataSpec;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import functionaljavaa.testingscripts.TestingAssert;
import functionaljavaa.testingscripts.TestingAssertSummary;
import functionaljavaa.unitsofmeasurement.UnitsOfMeasurement;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPNulls;

/**
 *
 * @author Administrator
 */
public class TestingQuantitativeLimitAndResult extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response = LPTestingOutFormat.responsePreparation(response);        
        DataSpec resChkSpec = new DataSpec();   

        TestingAssertSummary tstAssertSummary = new TestingAssertSummary();

        String csvFileName = "dbSchema_config_specQuantitative_resultCheck.txt"; 
                             
        String csvPathName = LPTestingOutFormat.TESTING_FILES_PATH+csvFileName; 
        String csvFileSeparator=LPTestingOutFormat.TESTING_FILES_FIELD_SEPARATOR;
        
        Object[][] csvFileContent = LPArray.convertCSVinArray(csvPathName, csvFileSeparator); 
        StringBuilder fileContentBuilder = new StringBuilder(0);
        try (PrintWriter out = response.getWriter()) {
            fileContentBuilder.append(LPTestingOutFormat.getHtmlStyleHeader(this.getClass().getSimpleName(), csvFileName));
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
            Integer totalLines =csvFileContent.length;
//            numHeaderLines=13;
//            totalLines=14;
            for (Integer iLines=numHeaderLines;iLines<totalLines;iLines++){
                tstAssertSummary.increaseTotalTests();
                TestingAssert tstAssert = new TestingAssert(csvFileContent[iLines], numEvaluationArguments);
                
                Integer lineNumCols = csvFileContent[0].length-1;
                String resultValue = null;
                
                String schemaName="";
                String specCode="";
                Integer specCodeVersion=null;
                String variation="";
                String analysis="";
                String methodName="";
                Integer methodVersion=null;
                String parameterName="";
                String resultUomName=null; 
                if (lineNumCols>=numEvaluationArguments)
                    {schemaName = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments]);}
                if (lineNumCols>=numEvaluationArguments+1)
                    {specCode = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+1]);}
                if (lineNumCols>=numEvaluationArguments+2)
                    {specCodeVersion = LPTestingOutFormat.csvExtractFieldValueInteger(csvFileContent[iLines][numEvaluationArguments+2]);}
                if (lineNumCols>=numEvaluationArguments+3)
                    {variation = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+3]);}
                if (lineNumCols>=numEvaluationArguments+4)
                    { analysis = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+4]);}
                if (lineNumCols>=numEvaluationArguments+5)
                    { methodName = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+5]);}
                if (lineNumCols>=numEvaluationArguments+6)
                    { methodVersion = LPTestingOutFormat.csvExtractFieldValueInteger(csvFileContent[iLines][numEvaluationArguments+6]);}
                if (lineNumCols>=numEvaluationArguments+7)
                    {parameterName = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+7]);}
                if (lineNumCols>=numEvaluationArguments+8)
                    {resultValue = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+8]);}
                if (lineNumCols>=numEvaluationArguments+9)
                    {resultUomName = LPTestingOutFormat.csvExtractFieldValueString(csvFileContent[iLines][numEvaluationArguments+9]);}
                
                String schemaConfigName=LPPlatform.buildSchemaName(schemaName, LPPlatform.SCHEMA_CONFIG);
                String schemaDataName=LPPlatform.buildSchemaName(schemaName, LPPlatform.SCHEMA_DATA);

                Rdbms.stablishDBConection();                                
                Object[] resSpecEvaluation = null;                
                Object[][] specLimits = Rdbms.getRecordFieldsByFilter(schemaConfigName, TblsCnfg.SpecLimits.TBL.getName(), 
                    new String[]{TblsCnfg.SpecLimits.FLD_CODE.getName(), TblsCnfg.SpecLimits.FLD_CONFIG_VERSION.getName(), 
                        TblsCnfg.SpecLimits.FLD_VARIATION_NAME.getName(), TblsCnfg.SpecLimits.FLD_ANALYSIS.getName(), TblsCnfg.SpecLimits.FLD_METHOD_NAME.getName(), TblsCnfg.SpecLimits.FLD_METHOD_VERSION.getName(),TblsCnfg.SpecLimits.FLD_PARAMETER.getName()}, 
                    new Object[]{specCode, specCodeVersion, variation, analysis, methodName, methodVersion, parameterName}, 
                    new String[]{TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName(),TblsCnfg.SpecLimits.FLD_RULE_TYPE.getName(),TblsCnfg.SpecLimits.FLD_RULE_VARIABLES.getName(), 
                        TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName(), TblsCnfg.SpecLimits.FLD_UOM.getName(), TblsCnfg.SpecLimits.FLD_UOM_CONVERSION_MODE.getName()});
                if ( (LPPlatform.LAB_FALSE.equalsIgnoreCase(specLimits[0][0].toString())) && (!"Rdbms_NoRecordsFound".equalsIgnoreCase(specLimits[0][4].toString())) ){
                    fileContentTable1Builder.append(LPTestingOutFormat.rowAddField(Arrays.toString(resSpecEvaluation)));
                }else{
                    Integer limitId = (Integer) specLimits[0][0];
                    String specUomName=(String) specLimits[0][4];
                    ConfigSpecRule specRule = new ConfigSpecRule();
                    specRule.specLimitsRule(schemaName, limitId, null);
                    if (specRule.getRuleIsQualitative()){        
                      resSpecEvaluation = resChkSpec.resultCheck((String) resultValue, specRule.getQualitativeRule(), 
                              specRule.getQualitativeRuleValues(), specRule.getQualitativeRuleSeparator(), specRule.getQualitativeRuleListName());
                    }                   
                    Boolean requiresUnitsConversion=true;
                    BigDecimal resultConverted =  null;
                    UnitsOfMeasurement uom = new UnitsOfMeasurement();     
                    resultUomName = LPNulls.replaceNull(resultUomName);
                    specUomName = LPNulls.replaceNull(specUomName);
                    if (resultUomName.equals(specUomName)){requiresUnitsConversion=false;}
                    if (requiresUnitsConversion){
                        Object[] convDiagnoses = uom.convertValue(schemaName, new BigDecimal(resultValue), resultUomName, specUomName);
                        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(convDiagnoses[0].toString())) {
                            resSpecEvaluation=LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "DataSample_SampleAnalysisResult_ConverterFALSE", new Object[]{limitId.toString(), convDiagnoses[3].toString(), schemaDataName});                  
                        }
                        resultConverted =  new BigDecimal((String) convDiagnoses[1]);        
                    }
                    if (specRule.getRuleIsQuantitative()){
                      BigDecimal resultValueBigDecimal= new BigDecimal(resultValue);
                      if (specRule.getQuantitativeHasControl()){
                          if (requiresUnitsConversion) {
                              resSpecEvaluation = resChkSpec.resultCheck(resultConverted, specRule.getMinSpec(), specRule.getMaxSpec(), specRule.getMinSpecIsStrict(), specRule.getMaxSpecIsStrict(), specRule.getMinControl(), specRule.getMaxControl(), specRule.getMinControlIsStrict(), specRule.getMaxControlIsStrict());
                          } else {
                              resSpecEvaluation = resChkSpec.resultCheck((BigDecimal) resultValueBigDecimal, specRule.getMinSpec(), specRule.getMaxSpec(), specRule.getMinSpecIsStrict(), specRule.getMaxSpecIsStrict(), specRule.getMinControl(), specRule.getMaxControl(), specRule.getMinControlIsStrict(), specRule.getMaxControlIsStrict());
                          }
                          resSpecEvaluation=LPArray.addValueToArray1D(resSpecEvaluation, "Regla: " +specRule.getQualitativeRuleRepresentation());
                      } else {
                          if (requiresUnitsConversion) {
                              resSpecEvaluation = resChkSpec.resultCheck(resultConverted, specRule.getMinSpec(), specRule.getMaxSpec(), specRule.getMinSpecIsStrict(), specRule.getMaxSpecIsStrict());
                          } else {
                              resSpecEvaluation = resChkSpec.resultCheck((BigDecimal) resultValueBigDecimal, specRule.getMinSpec(), specRule.getMaxSpec(), specRule.getMinSpecIsStrict(), specRule.getMaxSpecIsStrict());
                          }
                      }
                      resSpecEvaluation=LPArray.addValueToArray1D(resSpecEvaluation, "Regla: " +specRule.getQuantitativeRuleRepresentation());
                    }
                fileContentTable1Builder.append(LPTestingOutFormat.rowAddFields(new Object[]{iLines-numHeaderLines+1, schemaName, specCode, specCodeVersion, variation, analysis, methodName, methodVersion, parameterName, resultValue, resultUomName}));
                if (numEvaluationArguments>0){                    
                    Object[] evaluate = tstAssert.evaluate(numEvaluationArguments, tstAssertSummary, resSpecEvaluation);
//                    if (specRule.getMinControl()==null){
                        fileContentTable1Builder.append(LPTestingOutFormat.rowAddFields(evaluate)).append(LPTestingOutFormat.rowEnd());
                    }
//                }
            }
        }       
        tstAssertSummary.notifyResults();
        fileContentTable1Builder.append(LPTestingOutFormat.tableEnd());
        String fileContentSummary = LPTestingOutFormat.createSummaryTable(tstAssertSummary, numEvaluationArguments);
        fileContentBuilder.append(fileContentSummary).append(fileContentTable1Builder.toString());
        out.println(fileContentBuilder.toString());            
        LPTestingOutFormat.createLogFile(csvPathName, fileContentBuilder.toString());
        tstAssertSummary=null; resChkSpec=null;
        }
        catch(Exception error){
            PrintWriter out = response.getWriter();
            out.println(error.getMessage());
            tstAssertSummary=null; resChkSpec=null;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response){            
        try {
            processRequest(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(TestingQuantitativeLimitAndResult.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TestingQuantitativeLimitAndResult.class.getName()).log(Level.SEVERE, null, ex);
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
