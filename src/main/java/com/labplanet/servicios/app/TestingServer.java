/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitConfig;
import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitData;
import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitDataAudit;
import com.labplanet.servicios.moduleenvmonit.TblsEnvMonitProcedure;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPMath;
import databases.Rdbms;
import databases.TblsApp;
import databases.TblsAppAudit;
import databases.TblsCnfg;
import databases.Token;
import databases.TblsData;
import functionaljavaa.batch.incubator.DataBatchIncubator;
import functionaljavaa.moduleenvironmentalmonitoring.ConfigProgramCalendar;
import functionaljavaa.modulesample.DataModuleSampleAnalysis;
import functionaljavaa.parameter.Parameter;
import functionaljavaa.samplestructure.DataSample;
import functionaljavaa.samplestructure.DataSampleUtilities;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONObject;


/**
 *
 * @author Administrator
 */
public class TestingServer extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);

        String language = LPFrontEnd.setLanguage(request);         
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet testingServer</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet testingServer at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
      
//            out.println(SomeEnumClass.getCell(1));
//            out.println(OtherEnumClass.getCell(1));
//            MyEnum m = null;            
//            out.println("Fran "+m.getByIndexFran(1));

        String tblCreateScript2=TblsApp.Incident.createTableScript(new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript2, new Object[]{});

        tblCreateScript2=TblsAppAudit.Session.createTableScript("", new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript2, new Object[]{});

            String schemaPrefix="genoma-1";
        tblCreateScript2=TblsCnfg.SopMetaData.createTableScript(schemaPrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript2, new Object[]{});
        
        tblCreateScript2=TblsData.UserSop.createTableScript(schemaPrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript2, new Object[]{});

        tblCreateScript2=TblsData.ViewUserAndMetaDataSopView.createTableScript(schemaPrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript2, new Object[]{});

String batchContentStr="";     
Integer batchNumRows=5;
Integer batchNumCols=3;
out.println(batchContentStr);
//add       
Integer sampleId=-1;
Integer pendingIncubationStage=-1;
Integer row=-1;
Integer col=-1;
String[] batchContent1D=new String[0];
String[][] batchContent2D=new String[0][0];        
/*
Object[][] objectsToAdd=new Object[][]{{12,1,1,2},{55,2,5,1},{53,2,5,3}};
    for (Object[] curObj: objectsToAdd){
        sampleId=(Integer) curObj[0];
        pendingIncubationStage=(Integer) curObj[1];
        row=(Integer) curObj[2];
        col=(Integer) curObj[3];
        if ((batchContentStr==null) || (batchContentStr.length()==0)){
            batchContent2D=new String[batchNumRows][0];
            for (int i=0;i<batchNumCols;i++)
                batchContent2D=LPArray.convertObjectArrayToStringArray(LPArray.addColumnToArray2D(batchContent2D, "EMPTY"));
                //batchContent2D=LPArray.setColumnValueToArray2D(batchContent2D, i, " ");
            batchContent1D=LPArray.array2dTo1d(batchContent2D);
        }else{
            batchContent1D=batchContentStr.split(batchContentSeparatorStructuredBatch);
            batchContent2D=LPArray.array1dTo2d(batchContent1D, batchNumCols);            
        }
        batchContent2D[row-1][col-1]=DataBatchIncubatorStructured.buildBatchPositionValue(sampleId, pendingIncubationStage);
        batchContentStr=LPArray.convertArrayToString(LPArray.array2dTo1d(batchContent2D), batchContentSeparatorStructuredBatch, "");        
        out.println(batchContentStr);
        out.println("batchContent1D:"+batchContent1D.length);        
        out.println("batchContent2D:"+batchContent2D.length+"x"+batchContent2D[0].length);        
    }


//remove 
row=5;
col=3;
String positionValueToFind=sampleId.toString()+"*"+pendingIncubationStage.toString();
        batchContent1D=batchContentStr.split(batchContentSeparatorStructuredBatch);
        Integer valuePosition=LPArray.valuePosicInArray(batchContent1D, positionValueToFind);
        if (valuePosition==-1)
            out.println("The sample "+sampleId.toString()+" is not part of the batch or not in position "+row.toString()+"x"+col.toString());
            //Object[] m=LPPlatform.trapMessage(LPPlatform.LAB_FALSE, "The sample <*1*> is not part of the batch <*1*> in procedure <*2*>", null);
        out.println(batchContentStr);
        batchContent1D[valuePosition]="";  
        batchContent2D=LPArray.array1dTo2d(batchContent1D, batchNumCols);

        batchContentStr=LPArray.convertArrayToString(LPArray.array2dTo1d(batchContent2D), batchContentSeparatorStructuredBatch, "");        
out.println("batchContent1D:"+batchContent1D.length);        
out.println("batchContent2D:"+batchContent2D.length+"x"+batchContent2D[0].length);        
out.println(batchContentStr);
out.println("FIN");        
//if (1==1) return;
*/
            Object[] isConnected = new Object[0];
            isConnected=Rdbms.stablishDBConectionTester();
            //isConnected = Rdbms.getRdbms().startRdbms(LPTestingOutFormat.TESTING_USER, LPTestingOutFormat.TESTING_PW);      
            if ((Boolean) isConnected[0]){out.println("Connected to the db !:)");
            }else{out.println("NOT Connected to the db :( "+ Arrays.toString(isConnected));}
            Token token = new Token("eyJ1c2VyREIiOiJsYWJwbGFuZXQiLCJlU2lnbiI6Im1hbG90YSIsInVzZXJEQlBhc3N3b3JkIjoibGFzbGVjaHVnYXMiLCJ0eXAiOiJKV1QiLCJhcHBTZXNzaW9uSWQiOiIyOCIsImFwcFNlc3Npb25TdGFydGVkRGF0ZSI6IlNhdCBBdWcgMTcgMDE6NTU6NTUgQ0VTVCAyMDE5IiwidXNlclJvbGUiOiJjb29yZGluYXRvciIsImFsZyI6IkhTMjU2IiwiaW50ZXJuYWxVc2VySUQiOiIxIn0.eyJpc3MiOiJMYWJQTEFORVRkZXN0cmFuZ2lzSW5UaGVOaWdodCJ9.TYIUehSPitkr4p7_fSYCNCcF8PzoxC24qsYg5V4rxQw");
            out.println("Today in Date format: "+LPDate.getTimeStampLocalDate().toString());
            //out.println("Today in DateTime format: "+LPDate.getDateTimeLocalDate().toString());
            
            sampleId=138;

            
            DataModuleSampleAnalysis smpAna = new DataModuleSampleAnalysis();   
            DataSample smp = new DataSample(smpAna);   
            Object[] dataSample = smp.setSamplingDate(schemaPrefix, token, sampleId);
            out.println("log for setSamplingDate: "+Arrays.toString(dataSample));
            
            out.println("Testing Files Path: "+LPTestingOutFormat.TESTING_FILES_PATH);
            out.println(LPDate.getTimeStampLocalDate().toString());
            String procName="process-us";
            
            out.println("Extract portion, extraer 5 sobre una cantidad de 4: "+Arrays.toString(LPMath.extractPortion(procName, 
                    BigDecimal.valueOf(4), "MG",1, BigDecimal.valueOf(5), "MG", 2)));
            out.println("Extract portion, extraer 4 sobre una cantidad de 4: "+Arrays.toString(LPMath.extractPortion(procName, 
                    BigDecimal.valueOf(4), "MG",1, BigDecimal.valueOf(4), "MG", 2)));
            
            out.println("Statuses en inglés: "+Arrays.toString(DataSampleUtilities.getSchemaSampleStatusList(procName)));
            out.println("Statuses en castellano: "+Arrays.toString(DataSampleUtilities.getSchemaSampleStatusList(procName, "es")));
            
            out.println("First sample analysis status for process-us is: "+Parameter.getParameterBundle("process-us-data", "sampleAnalysis_statusFirst"));
            
            String[] errObject = new String[]{"Servlet sampleAPI at " + request.getServletPath()};          
            
            String[] sampleFieldName = new String[]{TblsData.Sample.FLD_SPEC_CODE_VERSION.getName()};
            String[][] specFields = new String[][]{{TblsData.Sample.FLD_SPEC_CODE.getName(), ""}, {TblsData.Sample.FLD_SPEC_CODE_VERSION.getName(), ""}, {TblsData.Sample.FLD_SPEC_VARIATION_NAME.getName(), ""}};
            String[] specMissingFields = new String[0];
            for (String[] curValue: specFields){
                Integer posicField = LPArray.valuePosicInArray(sampleFieldName, curValue[0]);
                if (posicField == -1){specMissingFields = LPArray.addValueToArray1D(specMissingFields, curValue);
                }else{curValue[1] = "H";}                
            }
            for (String[] curField: specFields){
                out.println("specFields "+Arrays.toString(curField));    
            }
            Object[] firstArray=new Object[]{"A", "B"};
            Object[] secondArray=new Object[]{"1", "2", 3};
                String[] myJoinedArray=LPArray.joinTwo1DArraysInOneOf1DString(firstArray, secondArray, ":");
                out.println("joining two arrays of "+Arrays.toString(firstArray)+" and "+Arrays.toString(secondArray)+" with the separator "+":"+" I obtained "+Arrays.toString(myJoinedArray));
            firstArray=new Object[]{"A", "B","c", "Z"};
                myJoinedArray=LPArray.joinTwo1DArraysInOneOf1DString(firstArray, secondArray, ":");
                out.println("joining two arrays of "+Arrays.toString(firstArray)+" and "+Arrays.toString(secondArray)+" with the separator "+":"+" I obtained "+Arrays.toString(myJoinedArray));
            
            out.println("The name for the table Session in db is "+ TblsApp.AppSession.valueOf("TBL").getName());
            out.println("The name for the field Session_id in db is "+TblsApp.AppSession.valueOf("FLD_SESSION_ID").getName());
            String myTableName=TblsApp.Users.TBL.getName();
            out.println("The table name with NO valueOf() is "+myTableName);
            myTableName=TblsApp.Users.valueOf("TBL").getName();
            out.println("The table name WITH valueOf() is "+myTableName);
            
            
            String schemaPrefixSampleInfo="oil-pl1";
            Integer selSample=134;
            Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefixSampleInfo, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                    new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{selSample}, 
                    new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName(), TblsData.Sample.FLD_STATUS.getName()});
            out.println("Info from "+schemaPrefixSampleInfo+".sample "+selSample.toString()+": "+Arrays.toString(sampleInfo[0]));

            schemaPrefixSampleInfo="em-demo-a";
            selSample=160;
            sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefixSampleInfo, LPPlatform.SCHEMA_DATA), TblsData.Sample.TBL.getName(), 
                    new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName()}, new Object[]{selSample}, 
                    new String[]{TblsData.Sample.FLD_SAMPLE_ID.getName(), TblsData.Sample.FLD_STATUS.getName(), TblsData.Sample.FLD_STATUS_PREVIOUS.getName()});
            out.println("Info from "+schemaPrefixSampleInfo+".sample "+selSample.toString()+": "+Arrays.toString(sampleInfo[0]));
        
        JSONObject jsonObj = new JSONObject();
        String tblCreateScript="";

        String schemaNamePrefix="em-demo-a";
        tblCreateScript=TblsEnvMonitConfig.InstrIncubator.createTableScript(schemaNamePrefix, new String[]{""});
        tblCreateScript=TblsEnvMonitConfig.IncubBatch.createTableScript(schemaNamePrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
        jsonObj.put("TblsEnvMonitConfig.InstrIncubator", tblCreateScript);

        tblCreateScript=TblsEnvMonitDataAudit.IncubBatch.createTableScript(schemaNamePrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
        jsonObj.put("TblsEnvMonitConfig.InstrIncubator", tblCreateScript);

        tblCreateScript=TblsEnvMonitProcedure.IncubatorTempReadingViolations.createTableScript(schemaNamePrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
        jsonObj.put("TblsEnvMonitProcedure.IncubatorTempReadingViolations", tblCreateScript);
        out.println("TblsEnvMonitProcedure.IncubatorTempReadingViolations ");
        
/*        tblCreateScript=TblsEnvMonitData.ViewSampleMicroorganismList.createTableScript(schemaNamePrefix, new String[]{""});        
        //tblCreateScript=TblsEnvMonitData.IncubBatch.createTableScript(schemaNamePrefix, new String[]{""});
        //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});        
        //jsonObj.put("TblsEnvMonitData.InstrIncubatorNoteBook", tblCreateScript);
        
        tblCreateScript=TblsData.ViewUserAndMetaDataSopView.createTableScript(schemaNamePrefix, new String[]{""});
        Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
        jsonObj.put("TblsData.ViewUserAndMetaDataSopView", tblCreateScript);

        tblCreateScript=TblsApp.HolidaysCalendarDate.createTableScript(new String[]{""});
        Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
        jsonObj.put("TblsApp.HolidaysCalendarDate", tblCreateScript);
            */
            
 //           DbObjects.createPlatformSchemas();
//            DbObjects.createDBTables("", new String[]{});
            //EnvMonitSchemaDefinition.createDBTables(schemaNamePrefix, new String[]{});
//            tblCreateScript=TblsCnfg.zzzPropertiesMissing.createTableScript(schemaNamePrefix, new String[]{""});
//            Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
//            tblCreateScript=TblsEnvMonitConfig.ProgramCalendarDate.createTableScript(schemaNamePrefix, new String[]{""});
//            Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
//            tblCreateScript=TblsEnvMonitConfig.ProgramCalendarRecursiveEntries.createTableScript(schemaNamePrefix, new String[]{""});
//            Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
procName="LlenadoVialesFA2018";
Integer calendarId=5;
String locationName="E01";
String[] fieldName=new String[]{"day_of_week"};
Object[] fieldValue=new Object[]{"TUESDAYS"};
Object[] addRecDiag=new Object[]{};
addRecDiag=ConfigProgramCalendar.addRecursiveScheduleForLocation(schemaNamePrefix, procName, calendarId, locationName, 
        fieldName, fieldValue);
//out.println("Adding recursive schedule for location E01 on TUESDAYS: "+Arrays.toString(addRecDiag));
 locationName="E02";
 fieldName=new String[]{"day_of_week"};
 fieldValue=new Object[]{"FRIDAYS"};
 //addRecDiag=ConfigProgramCalendar.addRecursiveScheduleForLocation(schemaNamePrefix, procName, calendarId, locationName, 
 //       fieldName, fieldValue);
out.println("Adding recursive schedule for location E01 on FRIDAYS: "+Arrays.toString(addRecDiag));
String holidaysCalendar="España Comunidad X 2019";

//Object[] addHolidayCal=ConfigProgramCalendar.importHolidaysCalendarSchedule(schemaNamePrefix, procName, calendarId, holidaysCalendar);
//out.println("Adding holidays calendar for "+holidaysCalendar+": "+Arrays.toString(addHolidayCal));
            //ProcedureDefinitionToInstance.createDBProcessSchemas("samples");
            //ProcedureDefinitionToInstance.createDBProcessTables("pepe", "", new String[]{});
            //String schemaNamePrefix="";
            //String tblCreateScript=TblsCnfg.zzzDbErrorLog.createTableScript(schemaNamePrefix, new String[]{""});
            //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
            //schemaNamePrefix="em-demo-a";
            //String tblCreateScript=TblsData.ViewSampleAnalysisResultWithSpecLimits.createTableScript(schemaNamePrefix, new String[]{""});
            //Rdbms.prepRdQuery(tblCreateScript, new Object[]{});
//             schemaNamePrefix="em-demo-a";
             tblCreateScript=TblsEnvMonitData.ProductionLot.createTableScript(schemaNamePrefix, new String[]{""});
//            Rdbms.prepRdQuery(tblCreateScript, new Object[]{});

//Structured Batches. Begin
    out.println("Start testing for Structured Batches");
    schemaPrefix="em-demo-a";
    String batchName="Testeo Estructurada3";
    Object[] diagn=new Object[0];
    //diagn=DataBatchIncubator.createBatch(schemaPrefix, token, batchName, 2, 1, null, null);
    //out.println("createBatch"+Arrays.toString(diagn));
    //diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, 2, 1, 69);
    //out.println("batchRemoveSample"+Arrays.toString(diagn));
    //diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, 2, 1, 71);
    //out.println("batchRemoveSample"+Arrays.toString(diagn));
    /*diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, 2, 1, 73);
    out.println("batchRemoveSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, 2, 1, 74);
    out.println("batchRemoveSample"+Arrays.toString(diagn));*/

    //diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, 2, 1, 99, 2, 1, true);
    //out.println("batchAddSample"+Arrays.toString(diagn));
    //diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, 2, 1, 101, 1, 2, false);
    //out.println("batchAddSample"+Arrays.toString(diagn));
    //diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, 2, 1, 102, 5, 5, false);
    //out.println("batchAddSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchRemoveSample(schemaPrefix, token, batchName, 2, 1, 101);
    out.println("batchRemoveSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, 2, 1, 101, 5, 5, false);
    out.println("batchMoveSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, 2, 1, 103, 3, 3, true);
    out.println("batchAddSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchAddSample(schemaPrefix, token, batchName, 2, 1, 104, 2, 3, true);
    out.println("batchAddSample"+Arrays.toString(diagn));
    diagn=DataBatchIncubator.batchMoveSample(schemaPrefix, token, batchName, 2, 1, 104, 3, 3, true);
    out.println("batchMoveSample"+Arrays.toString(diagn));

//Structured Batches. End
            
            out.println("Before creating the token");
            String myToken = token.createToken(LPTestingOutFormat.TESTING_USER, LPTestingOutFormat.TESTING_PW, "3", "Admin", "", "", "");
            out.println("Token created: "+myToken);
            
            out.println("Reading web text file");
            String exampleUrl = "http://51.75.202.142:8888/myfiles/txtfile.txt";
            final URL url = new URL(exampleUrl);
            final StringBuilder sb = new StringBuilder();

            final char[] buf = new char[4096];

            final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT);

            try (
                final InputStream in = url.openStream();
                final InputStreamReader reader = new InputStreamReader(in, decoder);
            ) {
                int nrChars;
                while ((nrChars = reader.read(buf)) != -1)
                    sb.append(buf, 0, nrChars);
            }

            final String test = sb.toString();
            out.println("Test File Content: <br>"+test);
            
        String csvFileName = "noDBSchema_config_SpecQualitativeRuleGeneratorChecker.txt"; 
                             
        String csvPathName = LPTestingOutFormat.TESTING_FILES_PATH+csvFileName; 
        String csvFileSeparator=LPTestingOutFormat.TESTING_FILES_FIELD_SEPARATOR;
        
        Object[][] csvFileContent = LPArray.convertCSVinArray(csvPathName, csvFileSeparator);
        out.println("csv File Content: <br>"+LPTestingOutFormat.convertArrayInHtmlTable(csvFileContent));
        
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
