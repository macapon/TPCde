/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.testingscripts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import databases.Rdbms;
import databases.TblsTesting;
import lbplanet.utilities.LPHashMap;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;
import lbplanet.utilities.LPArray;
import functionaljavaa.parameter.Parameter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPDate;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPJson;
import static lbplanet.utilities.LPPlatform.TRAP_MESSAGE_CODE_POSIC;
import static lbplanet.utilities.LPPlatform.TRAP_MESSAGE_EVALUATION_POSIC;
import org.json.simple.JSONArray;

/*
 *
 * @author Administrator
 */
public class LPTestingOutFormat {

    /**
     * @return the csvHeaderTags
     */
    public HashMap<String, Object> getCsvHeaderTags() {
        return csvHeaderTags;
    }
    
    public enum InputModes{FILE, DATABASE}
    private String inputMode="";
    private Object[][] testingContent=new Object[0][0];
    private String filePathName="";
    private String fileName="";
    private HashMap<String, Object> csvHeaderTags=null;
    private StringBuilder htmlStyleHeader = new StringBuilder(0);
    private Integer numEvaluationArguments = 0;
    
    public LPTestingOutFormat(HttpServletRequest request, String testerFileName){
        String csvPathName ="";
        String csvFileName ="";
        Object[][] csvFileContent = new Object[0][0];
        Object testingSource=request.getAttribute(LPTestingParams.TESTING_SOURCE);
        Integer numEvalArgs=0;
        StringBuilder htmlStyleHdr = new StringBuilder(0);
        HashMap<String, Object> headerTags = new HashMap();   
        if (testingSource!=null && testingSource=="DB"){
            csvPathName ="";
            csvFileName ="";
            if (!LPFrontEnd.servletStablishDBConection(request, null)){return;}     
            numEvalArgs = Integer.valueOf(LPNulls.replaceNull(request.getAttribute(LPTestingParams.NUM_EVAL_ARGS).toString()));
            Integer scriptId = Integer.valueOf(LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCRIPT_ID).toString()));
            String schemaPrefix=LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCHEMA_PREFIX)).toString();
            csvFileContent = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_TESTING), TblsTesting.ScriptSteps.TBL.getName(), 
                    new String[]{TblsTesting.ScriptSteps.FLD_SCRIPT_ID.getName(), TblsTesting.ScriptSteps.FLD_ACTIVE.getName()}, new Object[]{scriptId, true}, 
                    new String[]{TblsTesting.ScriptSteps.FLD_EXPECTED_SYNTAXIS.getName(), TblsTesting.ScriptSteps.FLD_EXPECTED_CODE.getName(), TblsTesting.ScriptSteps.FLD_ESIGN_TO_CHECK.getName(),
                        TblsTesting.ScriptSteps.FLD_CONFIRMUSER_USER_TO_CHECK.getName(), TblsTesting.ScriptSteps.FLD_CONFIRMUSER_PW_TO_CHECK.getName(),
                        TblsTesting.ScriptSteps.FLD_ARGUMENT_01.getName(), TblsTesting.ScriptSteps.FLD_ARGUMENT_02.getName(),
                        TblsTesting.ScriptSteps.FLD_ARGUMENT_03.getName(), TblsTesting.ScriptSteps.FLD_ARGUMENT_04.getName(),
                        TblsTesting.ScriptSteps.FLD_ARGUMENT_05.getName(), TblsTesting.ScriptSteps.FLD_ARGUMENT_06.getName(),
                        TblsTesting.ScriptSteps.FLD_ARGUMENT_07.getName(), TblsTesting.ScriptSteps.FLD_ARGUMENT_08.getName(),
                        TblsTesting.ScriptSteps.FLD_ARGUMENT_09.getName(), TblsTesting.ScriptSteps.FLD_ARGUMENT_10.getName(), TblsTesting.ScriptSteps.FLD_STEP_ID.getName()},
                    new String[]{TblsTesting.ScriptSteps.FLD_STEP_ID.getName()});
            headerTags.put(FILEHEADER_NUM_HEADER_LINES_TAG_NAME, 0);   
            headerTags.put(FILEHEADER_NUM_TABLES_TAG_NAME, "-");  
            headerTags.put(FILEHEADER_NUM_EVALUATION_ARGUMENTS, numEvalArgs);   
        }else{
            csvPathName =(String) request.getAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_PATH);
            csvFileName =(String) request.getAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_NAME);
            if ("".equals(csvPathName) || csvPathName==null){
                csvFileName = testerFileName; 
                csvPathName = LPTestingOutFormat.TESTING_FILES_PATH; }
            csvPathName = csvPathName+csvFileName; 
            String csvFileSeparator=LPTestingOutFormat.TESTING_FILES_FIELD_SEPARATOR;

            csvFileContent = LPArray.convertCSVinArray(csvPathName, csvFileSeparator); 

            String[][] headerInfo = LPArray.convertCSVinArray(csvPathName, "=");
            headerTags = LPTestingOutFormat.getCSVHeader(headerInfo);          
            numEvalArgs = Integer.valueOf(headerTags.get(LPTestingOutFormat.FILEHEADER_NUM_EVALUATION_ARGUMENTS).toString());   
            
        }        
        htmlStyleHdr = new StringBuilder(0);
        htmlStyleHdr.append(LPTestingOutFormat.getHtmlStyleHeader(this.getClass().getSimpleName(), csvFileName));

        this.testingContent=csvFileContent;
        this.inputMode=LPNulls.replaceNull(testingSource).toString();
        this.filePathName=csvPathName;
        this.fileName=csvFileName;
        this.csvHeaderTags=headerTags;
        this.htmlStyleHeader=htmlStyleHdr;
        this.numEvaluationArguments=numEvalArgs;
    }
    
    public StringBuilder publishEvalStep(HttpServletRequest request, Integer stepId, Object[] evaluate, JSONArray functionRelatedObjects, TestingAssert tstAssert){
        StringBuilder fileContentBuilder = new StringBuilder(0);  
                
        String[] updFldNames=new String[]{TblsTesting.ScriptSteps.FLD_DATE_EXECUTION.getName()};
        Object[] updFldValues=new Object[]{LPDate.getCurrentTimeStamp()};        
        if (numEvaluationArguments>0 && ("DB".equals(this.inputMode)) ){
            Integer scriptId = Integer.valueOf(LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCRIPT_ID).toString()));
            String schemaPrefix=LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCHEMA_PREFIX)).toString();
            updFldNames=LPArray.addValueToArray1D(updFldNames, new String[]{TblsTesting.ScriptSteps.FLD_FUNCTION_SYNTAXIS.getName(), TblsTesting.ScriptSteps.FLD_EVAL_SYNTAXIS.getName()});
            updFldValues=LPArray.addValueToArray1D(updFldValues, new Object[]{evaluate[TRAP_MESSAGE_EVALUATION_POSIC], tstAssert.getEvalSyntaxisDiagnostic()});
            if (numEvaluationArguments>1){
                updFldNames=LPArray.addValueToArray1D(updFldNames, new String[]{TblsTesting.ScriptSteps.FLD_FUNCTION_CODE.getName(), TblsTesting.ScriptSteps.FLD_EVAL_CODE.getName(),
                    TblsTesting.ScriptSteps.FLD_DYNAMIC_DATA.getName()});
                updFldValues=LPArray.addValueToArray1D(updFldValues, new Object[]{evaluate[TRAP_MESSAGE_CODE_POSIC], tstAssert.getEvalCodeDiagnostic(),
                    functionRelatedObjects.toJSONString()});                    
            }
            Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_TESTING), TblsTesting.ScriptSteps.TBL.getName(),                         
                    updFldNames, updFldValues,
                    new String[]{TblsTesting.ScriptSteps.FLD_SCRIPT_ID.getName(), TblsTesting.ScriptSteps.FLD_STEP_ID.getName()}, new Object[]{scriptId, stepId});            
        }
        return fileContentBuilder;
    }   
    
    public StringBuilder publishEvalSummary(HttpServletRequest request, TestingAssertSummary tstAssertSummary){
        StringBuilder fileContentBuilder = new StringBuilder(0);  
        tstAssertSummary.notifyResults();
        String[] updFldNames=new String[]{TblsTesting.Script.FLD_DATE_EXECUTION.getName(), TblsTesting.Script.FLD_EVAL_TOTAL_TESTS.getName()};
        Object[] updFldValues=new Object[]{LPDate.getCurrentTimeStamp(), tstAssertSummary.getTotalTests()};        
        if (numEvaluationArguments>0){
            String fileContentSummary = LPTestingOutFormat.createSummaryTable(tstAssertSummary, numEvaluationArguments);
            fileContentBuilder.append(fileContentSummary);            
            if ("DB".equals(this.inputMode)){
                if (!LPFrontEnd.servletStablishDBConection(request, null)){return fileContentBuilder;}          
                Integer scriptId = Integer.valueOf(LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCRIPT_ID).toString()));
                String schemaPrefix=LPNulls.replaceNull(request.getAttribute(LPTestingParams.SCHEMA_PREFIX)).toString();
                updFldNames=LPArray.addValueToArray1D(updFldNames, new String[]{TblsTesting.Script.FLD_EVAL_SYNTAXIS_MATCH.getName(), TblsTesting.Script.FLD_EVAL_SYNTAXIS_UNDEFINED.getName(),
                            TblsTesting.Script.FLD_EVAL_SYNTAXIS_UNMATCH.getName()});
                updFldValues=LPArray.addValueToArray1D(updFldValues, new Object[]{tstAssertSummary.getTotalSyntaxisMatch(), tstAssertSummary.getTotalSyntaxisUndefined(), tstAssertSummary.getTotalSyntaxisUnMatch()});
                if (numEvaluationArguments>1){
                    updFldNames=LPArray.addValueToArray1D(updFldNames, new String[]{TblsTesting.Script.FLD_EVAL_CODE_MATCH.getName(), TblsTesting.Script.FLD_EVAL_CODE_UNDEFINED.getName(),
                                TblsTesting.Script.FLD_EVAL_CODE_UNMATCH.getName()});
                    updFldValues=LPArray.addValueToArray1D(updFldValues, new Object[]{tstAssertSummary.getTotalCodeMatch(), tstAssertSummary.getTotalCodeUndefined(), tstAssertSummary.getTotalCodeUnMatch()});                    
                }
                Rdbms.updateRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_TESTING), TblsTesting.Script.TBL.getName(),                         
                        updFldNames, updFldValues,
                        new String[]{TblsTesting.ScriptSteps.FLD_SCRIPT_ID.getName()}, new Object[]{scriptId});            
            }
        }
        return fileContentBuilder;
    }   
    
    /**
     *
     */
    public static final String TESTING_FILES_PATH = "http://51.75.202.142:8888/testingRepository-20200203/"; 

    /**
     *
     */
    public static final String TESTING_FILES_PATH_NAS = "\\\\FRANCLOUD\\fran\\LabPlanet\\testingRepository\\"; 

    /**
     *
     */
    public static final String TESTING_FILES_PATH_CHEMOS = "C:\\Chemos\\"; 

    /**
     *
     */
    public static final String TESTING_FILES_FIELD_SEPARATOR=";";

    /**
     *
     */
    public static final String TESTING_USER="labplanet";

    /**
     *
     */
    public static final String TESTING_PW="avecesllegaelmomento";

    /**
     *
     */
    public static final String MSG_DB_CON_ERROR="<th>Error connecting to the database</th>";       

    /**
     *
     */
    public static final Integer FILEHEADER_MAX_NUM_HEADER_LINES=25;

    /**
     *
     */
    public static final String FILEHEADER_TAGS_SEPARATOR="=";

    /**
     *
     */
    public static final String FILEHEADER_NUM_HEADER_LINES_TAG_NAME="NUMHEADERLINES";

    /**
     *
     */
    public static final String FILEHEADER_NUM_TABLES_TAG_NAME="NUMTABLES"; 
    public static final String FILEHEADER_TESTER_NAME_TAG_NAME="TESTERNAME"; 

    
    /**
     *
     */
    public static final String FILEHEADER_TABLE_NAME_TAG_NAME="TABLE";

    /**
     *
     */
    public static final String FILEHEADER_NUM_ARGUMENTS="NUMARGUMENTS";

    /**
     *
     */
    public static final String FILEHEADER_NUM_EVALUATION_ARGUMENTS="NUMEVALUATIONARGUMENTS";

    /**
     *
     */
    public static final String FILEHEADER_EVALUATION_POSITION="EVALUATIONPOSITION";
    
    /**
     *
     */
    public static final String ERROR_TRAPPING_FILEHEADER_MISSING_TAGS="There are missing tags in the file header: ";
    
    /**
     *
     */
    public static final String BUNDLE_FILE_NAME="parameter.config.labtimus";

    /**
     *
     */
    public static final String TST_ICON_MATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_iconMatch");

    /**
     *
     */
    public static final String TST_ICON_UNMATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_iconUnMatch");

    /**
     *
     */
    public static final String TST_ICON_UNDEFINED=ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_iconUndefined");

    /**
     *
     */
    public static final String TST_BOOLEANMATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_booleanMatch");

    /**
     *
     */
    public static final String TST_BOOLEANUNMATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_booleanUnMatch");

    /**
     *
     */
    public static final String TST_BOOLEANUNDEFINED=ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_booleanUndefined");

    /**
     *
     */
    public static final String TST_ERRORCODEMATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_errorCodeMatch");

    /**
     *
     */
    public static final String TST_ERRORCODEUNMATCH =ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_errorCodeUnMatch");

    /**
     *
     */
    public static final String TST_ERRORCODEUNDEFINED=ResourceBundle.getBundle(BUNDLE_FILE_NAME).getString("labPLANET_errorCodeUndefined");

    /**
     *
     * @param response
     * @return
     */
    public static HttpServletResponse responsePreparation(HttpServletResponse response){
        response.setCharacterEncoding(LPPlatform.LAB_ENCODER_UTF8);

        ResourceBundle prop = ResourceBundle.getBundle(Parameter.BUNDLE_TAG_PARAMETER_CONFIG_CONF);
        String frontendUrl = prop.getString("frontend_url");

        response.setHeader("Access-Control-Allow-Origin", frontendUrl);
        response.setHeader("Access-Control-Allow-Methods", "GET");          
        response.setContentType("text/html;charset=UTF-8");
        return response;
    }      
    
    /**
     *
     * @return
     */
    public static String htmlStart(){        return "<html>";    }

    /**
     *
     * @return
     */
    public static String htmlEnd(){        return "</html>";    }

    /**
     *
     * @return
     */
    public static String bodyStart(){        return "<body>";    }

    /**
     *
     * @return
     */
    public static String bodyEnd(){        return "</body>";    }

    /**
     *
     * @param cssClassName
     * @return
     */
    public static String tableStart(String cssClassName){        return "<table class=\""+ cssClassName+"\">";    }

    /**
     *
     * @return
     */
    public static String tableEnd(){        return "</table>";    }

    /**
     *
     * @return
     */
    public static String headerStart(){        return "<th>";    }

    /**
     *
     * @return
     */
    public static String headerEnd(){        return "</th>";    }

    /**
     *
     * @return
     */
    public static String rowStart(){        return "<tr>";    }

    /**
     *
     * @return
     */
    public static String rowEnd(){        return "</tr>";    }

    /**
     *
     * @return
     */
    public static String fieldStart(){        return "<td>";    }

    /**
     *
     * @return
     */
    public static String fieldEnd(){        return "</td>";    }

    /**
     *
     * @param field
     * @return
     */
    public static String headerAddField(String field){
        String content="";
        content = content+headerStart()+LPNulls.replaceNull((String) field)+headerEnd();           
        return content;
    }
    
    /**
     *
     * @param fields
     * @return
     */
    public static String headerAddFields(Object[] fields){
        StringBuilder content=new StringBuilder(0);
        for (Object fld: fields){
            content.append(headerStart()).append(LPNulls.replaceNull(fld).toString()).append(headerEnd());           
        }
        return content.toString();
    }

    /**
     *
     * @param fields
     * @return
     */
    public static String headerAddFields(String[] fields){
        StringBuilder content=new StringBuilder(0);
        for (Object fld: fields){
            content.append(headerStart()).append(LPNulls.replaceNull(fld).toString()).append(headerEnd());           
        }
        return content.toString();
    }
    
    /**
     *
     * @param fields
     * @param numEvaluationArguments
     * @return
     */
    public static String[] addUATColumns(String[] fields, Integer numEvaluationArguments){
        String[] newFields = new String[]{"Test #"};
        newFields=LPArray.addValueToArray1D(newFields, fields);
        if (numEvaluationArguments>0){
            newFields=LPArray.addValueToArray1D(newFields, "Syntaxis");
            if (numEvaluationArguments>1) newFields=LPArray.addValueToArray1D(newFields, "Code");
            newFields=LPArray.addValueToArray1D(newFields, "Evaluation");
        }
        return newFields;
    }

    /**
     *
     * @param field
     * @return
     */
    public static String rowAddField(String field){
        StringBuilder content=new StringBuilder(0);
        content.append(headerStart()).append(LPNulls.replaceNull(field)).append(headerEnd());           
        return content.toString();
    }

    /**
     *
     * @param fields
     * @return
     */
    public static String rowAddFields(Object[] fields){
        StringBuilder content=new StringBuilder(0);
        for (Object field: fields){
            if (field==null){
                content.append(fieldStart()).append("").append(fieldEnd());  
            }else{
                content.append(fieldStart()).append(LPNulls.replaceNull(field).toString()).append(fieldEnd());  
            }
        }
        return content.toString();
    }

    /**
     *
     * @param csvPathName
     * @param fileContent
     */
    public static void createLogFile(String csvPathName, String fileContent){
        csvPathName = csvPathName.replace(".txt", ".html");            
        File file = new File(csvPathName);
            try (FileWriter fileWriter = new FileWriter(file)) {
                if (file.exists()) {
                  if (!file.delete()){return;}
//                  file.delete();
                }
                if (!file.createNewFile()){return;}
//                file.createNewFile();
                if (!file.exists()){
                    return;
                }
                fileWriter.write(fileContent);
                fileWriter.flush();
            }catch(IOException er){
            } 
    }
    
    /**
     *
     * @param servletName
     * @param fileName
     * @return
     */
    public static String getHtmlStyleHeader(String servletName, String fileName) {
        String fileContent = "";
        fileContent = fileContent + "<!DOCTYPE html>" + "";
        fileContent = fileContent + "<html>" + "";
        fileContent = fileContent + "<head>" + "";
        fileContent = fileContent + "<style>";
        ResourceBundle prop = ResourceBundle.getBundle(BUNDLE_FILE_NAME);
        fileContent = fileContent + prop.getString("testingTableStyleSummary1");
        fileContent = fileContent + prop.getString("testingTableStyleSummary2");
        fileContent = fileContent + prop.getString("testingTableStyleSummary3");
        fileContent = fileContent + prop.getString("testingTableStyleSummary4");
        fileContent = fileContent + prop.getString("testingTableStyleSummary5");
        fileContent = fileContent + prop.getString("testingTableStyle1");
        fileContent = fileContent + prop.getString("testingTableStyle2");
        fileContent = fileContent + prop.getString("testingTableStyle3");
        fileContent = fileContent + prop.getString("testingTableStyle4");
        fileContent = fileContent + prop.getString("testingTableStyle5");
        fileContent = fileContent + "</style>";
        fileContent = fileContent + "<title>Servlet " + servletName + "</title>" + "";
        fileContent = fileContent + "</head>" + "";
        fileContent = fileContent + "<body>" + "\n";
        fileContent = fileContent + "<h1>Tester for " + servletName + "</h1>" + "";
        fileContent = fileContent + "<h2>File being tested: " + fileName +" on "+LPDate.getCurrentTimeStamp().toString()+"</h2>" + "";
        fileContent = fileContent + "<table id=\"scriptTable\">";
        return fileContent;
    }
    
    /**
     *
     * @param csvContent
     * @return
     */
    
    public static HashMap<String, Object>  getCSVHeaderTester(String[][] csvContent){
        HashMap<String, Object> fieldsRequired = new HashMap();   
        fieldsRequired.put(FILEHEADER_NUM_HEADER_LINES_TAG_NAME, "");   fieldsRequired.put(FILEHEADER_NUM_TABLES_TAG_NAME, ""); 
        fieldsRequired.put(FILEHEADER_TESTER_NAME_TAG_NAME, ""); 
        fieldsRequired.put(FILEHEADER_NUM_EVALUATION_ARGUMENTS, "");   
        return getCSVHeaderManager(fieldsRequired, csvContent);
    }    
    
    public static HashMap<String, Object>  getCSVHeader(String[][] csvContent){
        HashMap<String, Object> fieldsRequired = new HashMap();   
        fieldsRequired.put(FILEHEADER_NUM_HEADER_LINES_TAG_NAME, "");   fieldsRequired.put(FILEHEADER_NUM_TABLES_TAG_NAME, "");  
        fieldsRequired.put(FILEHEADER_NUM_EVALUATION_ARGUMENTS, "");   
        return getCSVHeaderManager(fieldsRequired, csvContent);
    }
    
    private static HashMap<String, Object>  getCSVHeaderManager(HashMap<String, Object> fieldsRequired, String[][] csvContent){
        HashMap<String, Object> hm = new HashMap();   
        
        Integer maxHeaderLines=FILEHEADER_MAX_NUM_HEADER_LINES;
        if (csvContent.length<maxHeaderLines){maxHeaderLines=csvContent.length-1;}
        Integer iLineParsed = 0;
        Boolean continueParsing=true;        
        while (continueParsing){
            String getLineKey = LPNulls.replaceNull(csvContent[iLineParsed][0]).toUpperCase();
            String getLineValue = LPNulls.replaceNull(csvContent[iLineParsed][1]);
            if (fieldsRequired.containsKey(getLineKey)){
                switch (getLineKey.toUpperCase()){
                    case FILEHEADER_NUM_HEADER_LINES_TAG_NAME:
                        maxHeaderLines=Integer.parseInt(getLineValue);
                        break;
                    case FILEHEADER_NUM_TABLES_TAG_NAME:
                        Integer numTbls=Integer.parseInt(getLineValue);
                        for (int iNumTbls=1; iNumTbls<=numTbls; iNumTbls++){
                            fieldsRequired.put(FILEHEADER_TABLE_NAME_TAG_NAME+String.valueOf(iNumTbls), "");
                        }
                        break;
                    default:
                        break;                        
                }
                hm.put(getLineKey, getLineValue);
                fieldsRequired.remove(getLineKey);
            }                
            if (iLineParsed>=maxHeaderLines){continueParsing=false;}
            iLineParsed++;
        }
        if (!fieldsRequired.isEmpty()){
            hm.clear();                 
            hm.put(LPPlatform.LAB_FALSE, LPHashMap.hashMapToStringKeys(fieldsRequired, ", "));
        }        
        return hm;
    }

    /**
     *
     * @param tstAssert
     * @param numArguments
     * @return
     */
    public static String createSummaryTable(TestingAssertSummary tstAssert, Integer numArguments){
        String fileContentHeaderSummary = LPTestingOutFormat.tableStart("summary")+rowStart();
        String fileContentSummary =rowStart();
        if (numArguments>0){
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Total Tests");
            fileContentSummary = fileContentSummary +rowAddField(tstAssert.getTotalTests().toString()); 
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Match "+LPTestingOutFormat.TST_ICON_MATCH);                
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalSyntaxisMatch().toString());
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Undefined "+LPTestingOutFormat.TST_ICON_UNDEFINED);                
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalSyntaxisUndefined().toString()); 
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Unmatch "+LPTestingOutFormat.TST_ICON_UNMATCH);                
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalSyntaxisUnMatch().toString()); 
        }
        if (numArguments>1){
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Code Match "+LPTestingOutFormat.TST_ICON_MATCH);                
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalCodeMatch().toString()); 
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Code Undefined "+LPTestingOutFormat.TST_ICON_UNDEFINED);                
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalCodeUndefined().toString()); 
            fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Total ErrorCode Unmatch "+LPTestingOutFormat.TST_ICON_UNMATCH);       
            fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.getTotalCodeUnMatch().toString()); 
        }
        fileContentSummary = fileContentHeaderSummary+fileContentSummary +rowEnd();            
        fileContentSummary = fileContentSummary +tableEnd();        
        return fileContentSummary;        
    }
    
    /**
     *
     * @param content
     * @return
     */
    public static String convertArrayInHtmlTable(Object[][] content){
        StringBuilder fileContentTable = new StringBuilder(0);
        fileContentTable.append(LPTestingOutFormat.tableStart(""));    
        fileContentTable.append(headerAddFields(content[0])).append(headerEnd());
        for (int iRows=1; iRows< content.length; iRows++){
            fileContentTable.append(rowStart()).append(rowAddFields(content[iRows])).append(rowEnd());
        }
        fileContentTable.append(LPTestingOutFormat.tableEnd());    
        return fileContentTable.toString();
    }
    
    /**
     *
     * @param table1Header
     * @param numEvaluationArguments
     * @return
     */
    public static String createTableWithHeader(String table1Header, Integer numEvaluationArguments){
        String fileContentTable = LPTestingOutFormat.tableStart("");            
        fileContentTable=fileContentTable+headerAddFields(addUATColumns(table1Header.split(TESTING_FILES_FIELD_SEPARATOR), numEvaluationArguments));
        fileContentTable=fileContentTable+rowStart();        
        return fileContentTable;
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static BigDecimal csvExtractFieldValueBigDecimal(Object value){
        if (value==null) return null;
        try{
            return new BigDecimal(value.toString());        
        }catch(Exception e){return null;}        
    }

    /**
     *
     * @param value
     * @return
     */
    public static Boolean csvExtractFieldValueBoolean(Object value){        
        if (value==null) return false;
        if (value.toString().length()==0){return false;}
        try{
            return Boolean.valueOf(value.toString());
        }catch(Exception e){return false;}                    
    }

    /**
     *
     * @param value
     * @return
     */
    public static String csvExtractFieldValueString(Object value){
        if (value==null) return null;
        try{
            return value.toString();        
        }catch(Exception e){return null;}        
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static String[] csvExtractFieldValueStringArr(Object value){
        if (value==null) return new String[0];
        try{
            return value.toString().split("\\|");
        }catch(Exception e){return new String[0];}        
    }

    /**
     *
     * @param value
     * @return
     */
    public static Float csvExtractFieldValueFloat(Object value){
        if (value==null) return null;
        try{
            return Float.valueOf(value.toString());
        }catch(NumberFormatException e){return null;}        
    }

    /**
     *
     * @param value
     * @return
     */
    public static Integer csvExtractFieldValueInteger(Object value){
        if (value==null) return null;
        try{
            return Integer.valueOf(value.toString());
        }catch(NumberFormatException e){return null;}        
    }    

    /**
     *
     * @param value
     * @return
     */
    public static Date csvExtractFieldValueDate(Object value){
        if (value==null) return null;
        try{
            return Date.valueOf(value.toString());
        }catch(NumberFormatException e){return null;}        
    }

    /**
     *
     * @param csvFileName
     * @return
     */
    public static Object[][] getCSVFileContent(String csvFileName) {
        String csvPathName = LPTestingOutFormat.TESTING_FILES_PATH+csvFileName; 
        String csvFileSeparator=LPTestingOutFormat.TESTING_FILES_FIELD_SEPARATOR;        
        return LPArray.convertCSVinArray(csvPathName, csvFileSeparator);         
    } 
    
    private static int getStepObjectPosic(JsonObject jsonObject){
            int stepObjectPosic = 1;
            try{
                stepObjectPosic = jsonObject.get("object_posic").getAsInt();  
            }catch(Exception ex){stepObjectPosic = 1;}
            return stepObjectPosic;
    }
    
    public static String getAttributeValue(Object value, Object[][] scriptSteps){            
        try{
            if (!value.toString().contains("step")) return LPNulls.replaceNull(value.toString());

            JsonObject jsonObject = LPJson.convertToJsonObjectStringedObject(value.toString());
            int stepNumber = jsonObject.get("step").getAsInt();        
            String stepObjectType = jsonObject.get("object_type").getAsString(); 
            int stepObjectPosic=getStepObjectPosic(jsonObject);
            Integer stepPosic=LPArray.valuePosicInArray(LPArray.getColumnFromArray2D(scriptSteps, scriptSteps[0].length-2), stepNumber);
            if (stepPosic==-1) return "";

            JsonArray jsonArr=LPJson.convertToJsonArrayStringedObject(scriptSteps[stepPosic][scriptSteps[0].length-1].toString());
            Integer numObjectsFound=0;
            for (int i = 0; i < jsonArr.size(); i++) {
               JsonObject object = (JsonObject) jsonArr.get(i);
               String objType=object.get("object_type").getAsString();
               if (objType.equalsIgnoreCase(stepObjectType)){
                   numObjectsFound++;
                   if (numObjectsFound.equals(stepObjectPosic)) return object.get("object_name").getAsString();                   
               }
            }
            return "";
        }catch(Exception ex){ return "";}
    }

    
    /**
     * @return the inputMode
     */
    public String getInputMode() {
        return inputMode;
    }

    /**
     * @return the testingContent
     */
    public Object[][] getTestingContent() {
        return testingContent;
    }

    /**
     * @return the filePathName
     */
    public String getFilePathName() {
        return filePathName;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the htmlStyleHeader
     */
    public StringBuilder getHtmlStyleHeader() {
        return htmlStyleHeader;
    }

    /**
     * @return the numEvaluationArguments
     */
    public Integer getNumEvaluationArguments() {
        return numEvaluationArguments;
    }
                
    
}
