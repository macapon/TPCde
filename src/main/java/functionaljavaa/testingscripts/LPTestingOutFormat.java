/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.testingscripts;

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
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPDate;
//import jdk.jfr.internal.LogLevel;
//import jdk.jfr.internal.LogTag;
//import jdk.jfr.internal.Logger;

/**
 *
 * @author Administrator
 */
public class LPTestingOutFormat {
    private LPTestingOutFormat(){    throw new IllegalStateException("Utility class");}    
    
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
     * @return
     */
    public static String tableStart(){        return "<table>";    }

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
        StringBuilder content=new StringBuilder();
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
        StringBuilder content=new StringBuilder();
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
            newFields=LPArray.addValueToArray1D(newFields, "Code");
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
        StringBuilder content=new StringBuilder();
        content.append(headerStart()).append(LPNulls.replaceNull(field)).append(headerEnd());           
        return content.toString();
    }

    /**
     *
     * @param fields
     * @return
     */
    public static String rowAddFields(Object[] fields){
        StringBuilder content=new StringBuilder();
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
                  file.delete();
                  //if ((!file.delete()){return;}
                }
                file.createNewFile();
                if (!file.exists()){
                    return;
                }
                fileWriter.write(fileContent);
                fileWriter.flush();
            }catch(IOException er){
              String errorMessage=er.getMessage();
              //Logger.log(LogTag.JFR, LogLevel.TRACE, errorMessage);
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
     * @return
     */
    public static String createSummaryTable(TestingAssertSummary tstAssert){
        String fileContentHeaderSummary = LPTestingOutFormat.tableStart()+rowStart();
        String fileContentSummary =rowStart();

        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Total Tests");
        fileContentSummary = fileContentSummary +rowAddField(tstAssert.getTotalTests().toString()); 
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Match "+LPTestingOutFormat.TST_ICON_MATCH);                
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetBooleanMatch.toString());
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Undefined "+LPTestingOutFormat.TST_ICON_UNDEFINED);                
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetBooleanUndefined.toString()); 
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Syntaxis Unmatch "+LPTestingOutFormat.TST_ICON_UNMATCH);                
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetBooleanUnMatch.toString()); 
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Code Match "+LPTestingOutFormat.TST_ICON_MATCH);                
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetErrorCodeMatch.toString()); 
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Code Undefined "+LPTestingOutFormat.TST_ICON_UNDEFINED);                
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetErrorCodeUndefined.toString()); 
        fileContentHeaderSummary=fileContentHeaderSummary+headerAddField("Total ErrorCode Unmatch "+LPTestingOutFormat.TST_ICON_UNMATCH);       
        fileContentSummary = fileContentSummary +LPTestingOutFormat.rowAddField(tstAssert.totalLabPlanetErrorCodeUnMatch.toString()); 

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
        StringBuilder fileContentTable = new StringBuilder();
        fileContentTable.append(LPTestingOutFormat.tableStart());    
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
        String fileContentTable = LPTestingOutFormat.tableStart();            
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
            
    
}
