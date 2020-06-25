/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.modulegenoma;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPJson;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class GenomaStudyAPIFrontend extends HttpServlet {
    public static final String MANDATORY_PARAMS_MAIN_SERVLET=GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME+"|"+GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN;
    
    public static final String API_ENDPOINT_ALL_ACTIVE_PROJECTS="ALL_ACTIVE_PROJECTS";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);
        String language = LPFrontEnd.setLanguage(request); 
        try (PrintWriter out = response.getWriter()) {
            
        Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            LPFrontEnd.servletReturnResponseError(request, response, 
                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
            return;          
        }             
        String schemaPrefix = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX);            
        String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
        String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);                   
            
        //Token token = new Token(finalToken);

        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}
        
        switch (actionName.toUpperCase()){
            case API_ENDPOINT_ALL_ACTIVE_PROJECTS:
                String schemaName=LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA);
                JSONObject projectsListObj = new JSONObject(); 
                Object[][] projectInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.Project.TBL.getName(), 
                    new String[]{TblsGenomaData.Project.FLD_ACTIVE.getName()}, new Object[]{true}, 
                    TblsGenomaData.Project.getAllFieldNames(), new String[]{TblsGenomaData.Project.FLD_NAME.getName()});
                if (LPPlatform.LAB_FALSE.equalsIgnoreCase(projectInfo[0][0].toString())){
                    Rdbms.closeRdbms();                                           
                    Object[] errMsg = LPFrontEnd.responseError(projectInfo, language, null);
                    response.sendError((int) errMsg[0], (String) errMsg[1]);    
                    return;
                }
                JSONArray programsJsonArr = new JSONArray();     
                for (Object[] curProject: projectInfo){
                    JSONObject curProgramJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.Project.getAllFieldNames(), curProject);
                    
                    String curProjectName=curProject[LPArray.valuePosicInArray(TblsGenomaData.Project.getAllFieldNames(), TblsGenomaData.Project.FLD_NAME.getName())].toString();
                    
                    Object[][] projStudyInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.Study.TBL.getName(), 
                        new String[]{TblsGenomaData.Study.FLD_PROJECT.getName()}, new Object[]{curProjectName}, 
                        TblsGenomaData.Study.getAllFieldNames(), new String[]{TblsGenomaData.Study.FLD_NAME.getName()});
                    JSONArray projStudiesJsonArr = new JSONArray(); 
                    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(projStudyInfo[0][0].toString())){                            
                        for (Object[] curProjStudy: projStudyInfo){
                            JSONObject curProjStudyJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.Study.getAllFieldNames(), curProjStudy);

                            String curStudyName=curProjStudy[LPArray.valuePosicInArray(TblsGenomaData.Study.getAllFieldNames(), TblsGenomaData.Study.FLD_NAME.getName())].toString();

                            Object[][] studySamplesSetInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.StudySamplesSet.TBL.getName(), 
                                new String[]{TblsGenomaData.StudySamplesSet.FLD_STUDY.getName()}, new Object[]{curStudyName}, 
                                TblsGenomaData.StudySamplesSet.getAllFieldNames(), new String[]{TblsGenomaData.StudySamplesSet.FLD_NAME.getName()});
                            JSONArray studySamplesSetJsonArr = new JSONArray();     
                            if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(studySamplesSetInfo[0][0].toString())){
                                for (Object[] curStudySamplesSet: studySamplesSetInfo){
                                    JSONObject curStudySamplesSetJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.StudySamplesSet.getAllFieldNames(), curStudySamplesSet);
                                    studySamplesSetJsonArr.add(curStudySamplesSetJson);
                                }
                            }
                            curProjStudyJson.put(TblsGenomaData.StudySamplesSet.TBL.getName(), studySamplesSetJsonArr);                                
                            
                            Object[][] studyFamilyInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.StudyFamily.TBL.getName(), 
                                new String[]{TblsGenomaData.StudyFamily.FLD_STUDY.getName()}, new Object[]{curStudyName}, 
                                TblsGenomaData.StudyFamily.getAllFieldNames(), new String[]{TblsGenomaData.StudyFamily.FLD_NAME.getName()});
                            JSONArray studyFamiliesJsonArr = new JSONArray();     
                            if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(studyFamilyInfo[0][0].toString())){
                                for (Object[] curStudyFamily: studyFamilyInfo){
                                    JSONObject curStudyFamilyJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.StudyFamily.getAllFieldNames(), curStudyFamily);
                                    studyFamiliesJsonArr.add(curStudyFamilyJson);
                                }
                                curProjStudyJson.put(TblsGenomaData.StudyFamily.TBL.getName(), studyFamiliesJsonArr);

                                Object[][] studyIndividualInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.StudyIndividual.TBL.getName(), 
                                    new String[]{TblsGenomaData.StudyIndividual.FLD_STUDY.getName()}, new Object[]{curStudyName}, 
                                    TblsGenomaData.StudyIndividual.getAllFieldNames(), new String[]{TblsGenomaData.StudyIndividual.FLD_INDIVIDUAL_ID.getName()});
                                JSONArray studyIndividualJsonArr = new JSONArray();     
                                if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(studyIndividualInfo[0][0].toString())){
                                    for (Object[] curStudyIndividual: studyIndividualInfo){
                                        JSONObject curStudyIndividualJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.StudyIndividual.getAllFieldNames(), curStudyIndividual);

                                        Integer curStudyIndividualId=Integer.valueOf(curStudyIndividual[LPArray.valuePosicInArray(TblsGenomaData.StudyIndividual.getAllFieldNames(), TblsGenomaData.StudyIndividual.FLD_INDIVIDUAL_ID.getName())].toString());
                                        
                                        curProjStudyJson.put(TblsGenomaData.StudyFamily.TBL.getName(), studyFamiliesJsonArr);
                                        Object[][] studyIndividualSampleInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsGenomaData.StudyIndividualSample.TBL.getName(), 
                                            new String[]{TblsGenomaData.StudyIndividualSample.FLD_STUDY.getName(), TblsGenomaData.StudyIndividualSample.FLD_INDIVIDUAL_ID.getName()}, new Object[]{curStudyName, curStudyIndividualId}, 
                                            TblsGenomaData.StudyIndividualSample.getAllFieldNames(), new String[]{TblsGenomaData.StudyIndividualSample.FLD_INDIVIDUAL_ID.getName()});
                                        JSONArray studyIndividualSampleJsonArr = new JSONArray();     
                                        if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(studyIndividualSampleInfo[0][0].toString())){
                                            for (Object[] curStudyIndividualSample: studyIndividualSampleInfo){
                                                JSONObject curStudyIndividualSampleJson = LPJson.convertArrayRowToJSONObject(TblsGenomaData.StudyIndividualSample.getAllFieldNames(), curStudyIndividualSample);
                                                studyIndividualSampleJsonArr.add(curStudyIndividualSampleJson);
                                            }
                                            curStudyIndividualJson.put(TblsGenomaData.StudyIndividualSample.TBL.getName(), studyIndividualSampleJsonArr);
                                        }
                                        studyIndividualJsonArr.add(curStudyIndividualJson);
                                    }
                                    curProjStudyJson.put(TblsGenomaData.StudyIndividual.TBL.getName(), studyIndividualJsonArr);
                                }

                            }
                            projStudiesJsonArr.add(curProjStudyJson);
                        }
                    }
                    curProgramJson.put(TblsGenomaData.Study.TBL.getName(), projStudiesJsonArr);
                    
                    programsJsonArr.add(curProgramJson);
                }
                projectsListObj.put(TblsGenomaData.Project.TBL.getName(), programsJsonArr);
                Rdbms.closeRdbms();                 
                response.getWriter().write(projectsListObj.toString());
                Response.ok().build();
                return;                 
/*                
                String programFldNameList = request.getParameter("programFldNameList");   
                  if (programFldNameList==null) programFldNameList = DEFAULT_PARAMS_PROGRAMS_LIST_PROGRAM_TO_GET;
                String[] programFldNameArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programFldNameList);

                String programFldSortList = request.getParameter("programFldSortList");   
                  if (programFldSortList==null) programFldSortList = DEFAULT_PARAMS_PROGRAMS_LIST_PROGRAM_SORT_FLDS;                     
                String[] programFldSortArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programFldSortList);

                String programLocationFldNameList = request.getParameter("programLocationFldNameList");   
                  if (programLocationFldNameList==null) programLocationFldNameList = DEFAULT_PARAMS_PROGRAMS_LIST_PROGRAM_LOCATION_TO_GET;                     
                String[] programLocationFldNameArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programLocationFldNameList);

                String programLocationFldSortList = request.getParameter("programLocationFldSortList");   
                if (programLocationFldSortList==null)programLocationFldSortList = DEFAULT_PARAMS_PROGRAMS_LIST_PROGRAM_LOCATION_SORT_FLDS;                     
                String[] programLocationFldSortArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programLocationFldSortList);
                
                if (LPArray.valuePosicInArray(programLocationFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName())==-1){
                    programLocationFldNameArray = LPArray.addValueToArray1D(programLocationFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName());
                }                    
                if (LPArray.valuePosicInArray(programLocationFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName())==-1){
                    programLocationFldNameArray = LPArray.addValueToArray1D(programLocationFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName());
                }
                String programLocationCardInfoFldNameList = request.getParameter("programLocationCardInfoFldNameList");   
                  if (programLocationCardInfoFldNameList==null) programLocationCardInfoFldNameList = DEFAULT_PARAMS_PROGRAMS_LIST_CARD_FIELDS;
                String[] programLocationCardInfoFldNameArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programLocationCardInfoFldNameList);

                String programLocationCardInfoFldSortList = request.getParameter("programLocationCardInfoFldSortList");   
                  if (programLocationCardInfoFldSortList==null) programLocationCardInfoFldSortList = DEFAULT_PARAMS_PROGRAMS_LIST_CARD_SORT_FLDS;                     
                String[] programLocationCardInfoFldSortArray = LPTestingOutFormat.csvExtractFieldValueStringArr(programLocationCardInfoFldSortList);
                
                if (LPArray.valuePosicInArray(programLocationCardInfoFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName())==-1){
                    programLocationCardInfoFldNameArray = LPArray.addValueToArray1D(programLocationCardInfoFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName());
                }                    
                if (LPArray.valuePosicInArray(programLocationCardInfoFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName())==-1){
                    programLocationCardInfoFldNameArray = LPArray.addValueToArray1D(programLocationCardInfoFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName());
                }
                Object[] statusList = DataSampleUtilities.getSchemaSampleStatusList(schemaPrefix);
                Object[] statusListEn = DataSampleUtilities.getSchemaSampleStatusList(schemaPrefix, LPPlatform.REQUEST_PARAM_LANGUAGE_ENGLISH);
                Object[] statusListEs = DataSampleUtilities.getSchemaSampleStatusList(schemaPrefix, LPPlatform.REQUEST_PARAM_LANGUAGE_SPANISH);

                if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}                    
                Object[][] programInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsEnvMonitData.Program.TBL.getName(), 
                    new String[]{TblsEnvMonitData.Program.FLD_ACTIVE.getName()}, new Object[]{true}, programFldNameArray, programFldSortArray);
                if (LPPlatform.LAB_FALSE.equalsIgnoreCase(programInfo[0][0].toString())){
                    Rdbms.closeRdbms();                                           
                    Object[] errMsg = LPFrontEnd.responseError(programInfo, language, null);
                    response.sendError((int) errMsg[0], (String) errMsg[1]);    
                    return;
                }
                JSONArray programsJsonArr = new JSONArray();     
                for (Object[] curProgram: programInfo){
                    JSONObject programJsonObj = new JSONObject();  
                    String currProgram = curProgram[0].toString();

                    String[] programSampleSummaryFldNameArray = new String[]{TblsEnvMonitData.Sample.FLD_STATUS.getName(), TblsEnvMonitData.Sample.FLD_LOCATION_NAME.getName()};
                    String[] programSampleSummaryFldSortArray = new String[]{TblsEnvMonitData.Sample.FLD_STATUS.getName()};
                    Object[][] programSampleSummary = Rdbms.getRecordFieldsByFilter(schemaName, TblsEnvMonitData.Sample.TBL.getName(), 
                            new String[]{TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName(), }, new String[]{currProgram}, programSampleSummaryFldNameArray, programSampleSummaryFldSortArray);

                    for (int yProc=0; yProc<programInfo[0].length; yProc++){              
                        programJsonObj.put(programFldNameArray[yProc], curProgram[yProc]);
                    }
                    programJsonObj.put(JSON_TAG_NAME_TYPE, JSON_TAG_NAME_TYPE_VALUE_TREE_LIST); 
                    programJsonObj.put(JSON_TAG_NAME_TOTAL, programSampleSummary.length); 
                    // Program Location subStructure. Begin
                    Object[][] programLocations = Rdbms.getRecordFieldsByFilter(schemaName, TblsEnvMonitData.ProgramLocation.TBL.getName(), 
                            new String[]{TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName()}, new String[]{currProgram}, 
                            programLocationFldNameArray, programLocationFldSortArray);
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(programLocations[0][0].toString())){
//                            programJsonObj.put("program_location_error", programLocations[0][programLocations[0].length-1]);        
                        if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}                           
                        programLocations = Rdbms.getRecordFieldsByFilter(schemaName, TblsEnvMonitData.ProgramLocation.TBL.getName(), 
                                                       new String[]{TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName()}, new String[]{currProgram}, 
                                                       programLocationFldNameArray, programLocationFldSortArray);                            
                    }
                    String[] fieldToRetrieveArr=new String[]{TblsEnvMonitData.Sample.FLD_CURRENT_STAGE.getName()};
                    String[] whereFieldNames=new String[]{TblsEnvMonitData.Sample.FLD_PROGRAM_NAME.getName()}; 
                    Object[] whereFieldValues=new Object[]{currProgram};
                    Object[][] samplesCounterPerStage=Rdbms.getGrouper(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                            fieldToRetrieveArr, 
                            whereFieldNames, whereFieldValues,
                            new String[]{"COUNTER desc"}); 
                    fieldToRetrieveArr=LPArray.addValueToArray1D(fieldToRetrieveArr, "COUNTER");                            
                    JSONArray programSampleSummaryByStageJsonArray=new JSONArray();
                    for (Object[] curRec: samplesCounterPerStage){
                      JSONObject jObj= LPJson.convertArrayRowToJSONObject(fieldToRetrieveArr, curRec);
                      programSampleSummaryByStageJsonArray.add(jObj);
                    }    
                    programJsonObj.put(JSON_TAG_GROUP_NAME_SAMPLES_SUMMARY_BY_STAGE, programSampleSummaryByStageJsonArray); 
                    
                    JSONObject jObj= new JSONObject();
                    String[] fieldsToRetrieve = new String[]{TblsEnvMonitData.ViewProgramScheduledLocations.FLD_PROGRAM_NAME.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_DATE.getName(),
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_PROGRAM_DAY_ID.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_PROGRAM_DAY_DATE.getName(),
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SAMPLE_CONFIG_CODE.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SAMPLE_CONFIG_CODE_VERSION.getName(),
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_LOCATION_NAME.getName(),            
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_AREA.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SPEC_CODE.getName(), 
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SPEC_CODE_VERSION.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_AREA.getName(), 
                        TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SPEC_VARIATION_NAME.getName(), TblsEnvMonitData.ViewProgramScheduledLocations.FLD_SPEC_ANALYSIS_VARIATION.getName() 
                    };                            
                    Object[][] programCalendarDatePending=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.ViewProgramScheduledLocations.TBL.getName(), 
                            new String[]{TblsEnvMonitData.ViewProgramScheduledLocations.FLD_PROGRAM_NAME.getName()+" IS NOT NULL"}, new Object[]{}, 
                            fieldsToRetrieve, new String[]{TblsEnvMonitData.ViewProgramScheduledLocations.FLD_PROGRAM_DAY_DATE.getName()});
                    JSONArray programConfigScheduledPointsJsonArray=new JSONArray();
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(programCalendarDatePending[0][0].toString())){
                        jObj.put("message", "Nothing pending in procedure "+schemaPrefix+" for the filter "+programCalendarDatePending[0][6].toString());
                        programConfigScheduledPointsJsonArray.add(jObj);
                    }
                    for (Object[] curRecord: programCalendarDatePending){
                        jObj= new JSONObject();
                        for (int i=0;i<curRecord.length;i++){ jObj.put(fieldsToRetrieve[i], curRecord[i].toString());}
                        jObj.put("title", curRecord[LPArray.valuePosicInArray(fieldsToRetrieve, TblsEnvMonitData.ViewProgramScheduledLocations.FLD_LOCATION_NAME.getName())].toString());
                        jObj.put("content", curRecord[LPArray.valuePosicInArray(fieldsToRetrieve, TblsEnvMonitData.ViewProgramScheduledLocations.FLD_LOCATION_NAME.getName())].toString());
                        jObj.put("date",curRecord[LPArray.valuePosicInArray(fieldsToRetrieve, TblsEnvMonitData.ViewProgramScheduledLocations.FLD_DATE.getName())].toString());
                        jObj.put("category","orange");
                        jObj.put("color","#000");
                        programConfigScheduledPointsJsonArray.add(jObj);
                    }    
                    programJsonObj.put(JSON_TAG_GROUP_NAME_CONFIG_CALENDAR, programConfigScheduledPointsJsonArray); 
                    
                    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(programLocations[0][0].toString())){     
                        JSONArray programLocationsJsonArray = new JSONArray();                              
                        for (Object[] programLocations1 : programLocations) {
                            String locationName = programLocations1[LPArray.valuePosicInArray(programLocationFldNameArray, TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName())].toString();

                            JSONObject programLocationJsonObj = new JSONObject();     
                            for (int yProcEv = 0; yProcEv<programLocations[0].length; yProcEv++) {
                                programLocationJsonObj.put(programLocationFldNameArray[yProcEv], programLocations1[yProcEv]);
                            }
                            Object[][] programLocationCardInfo = Rdbms.getRecordFieldsByFilter(schemaName, TblsEnvMonitData.ProgramLocation.TBL.getName(), 
                                    new String[]{TblsEnvMonitData.ProgramLocation.FLD_PROGRAM_NAME.getName(), TblsEnvMonitData.ProgramLocation.FLD_LOCATION_NAME.getName()}, new String[]{currProgram, locationName}, 
                                    programLocationCardInfoFldNameArray, programLocationCardInfoFldSortArray);
                            JSONArray programLocationCardInfoJsonArr = new JSONArray(); 

                            JSONObject programLocationCardInfoJsonObj = new JSONObject();  
                            for (int xProc=0; xProc<programLocationCardInfo.length; xProc++){   
                                for (int yProc=0; yProc<programLocationCardInfo[0].length; yProc++){              
                                    programLocationCardInfoJsonObj = new JSONObject();
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_NAME, programLocationCardInfoFldNameArray[yProc]);
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_LABEL_EN, programLocationCardInfoFldNameArray[yProc]);
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_LABEL_ES, programLocationCardInfoFldNameArray[yProc]);
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_VALUE, programLocationCardInfo[xProc][yProc]);
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_TYPE, JSON_TAG_NAME_TYPE_VALUE_TEXT);
                                    String fieldName=programLocationCardInfoFldNameArray[yProc];
                                    Integer posicInArray=LPArray.valuePosicInArray(PROGRAM_LOCATION_CARD_FIELDS_INTEGER, fieldName);
                                    if (posicInArray>-1){
                                        programLocationCardInfoJsonObj.put(JSON_TAG_NAME_DB_TYPE, JSON_TAG_NAME_DB_TYPE_VALUE_INTEGER);
                                    }else{ 
                                        posicInArray=LPArray.valuePosicInArray(PROGRAM_LOCATION_CARD_FIELDS_NO_DBTYPE, fieldName);
                                        if (posicInArray==-1){
                                            programLocationCardInfoJsonObj.put(JSON_TAG_NAME_DB_TYPE, JSON_TAG_NAME_DB_TYPE_VALUE_STRING);
                                        }else{
                                            programLocationCardInfoJsonObj.put(JSON_TAG_NAME_DB_TYPE, "");
                                        }
                                    }
                                    programLocationCardInfoJsonObj.put(JSON_TAG_NAME_PSWD, JSON_TAG_NAME_PSWD_VALUE_FALSE);
                                    // programLocationCardInfoJsonObj.put(programLocationCardInfoFldNameArray[yProcEv], "test"); //programLocations1[yProcEv]);
                                    programLocationCardInfoJsonArr.add(programLocationCardInfoJsonObj);                                    
                                }    
                            }
                            programLocationJsonObj.put(JSON_TAG_GROUP_NAME_CARD_INFO, programLocationCardInfoJsonArr);  
                            Object[] samplesStatusCounter = new Object[0];
                            for (Object statusList1 : statusList) {
                                String currStatus = statusList1.toString();
                                Integer contSmpStatus=0;
                                for (Object[] smpStatus: programSampleSummary){
                                    if (currStatus.equalsIgnoreCase(smpStatus[0].toString()) && 
                                            ( smpStatus[1]!=null) && locationName.equalsIgnoreCase(smpStatus[1].toString()) ){contSmpStatus++;}
                                }
                                samplesStatusCounter = LPArray.addValueToArray1D(samplesStatusCounter, contSmpStatus);
                            }
                            JSONArray programSampleSummaryJsonArray = new JSONArray();  
                            for (int iStatuses=0; iStatuses < statusList.length; iStatuses++){
                                JSONObject programSampleSummaryJsonObj = new JSONObject();  
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_NAME, statusList[iStatuses]);
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_LABEL_EN, statusListEn[iStatuses]);
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_LABEL_ES, statusListEs[iStatuses]);
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_VALUE, samplesStatusCounter[iStatuses]);
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_TYPE, JSON_TAG_NAME_TYPE_VALUE_TEXT);
                                programSampleSummaryJsonObj.put(JSON_TAG_NAME_PSWD, JSON_TAG_NAME_PSWD_VALUE_FALSE);
                                programSampleSummaryJsonArray.add(programSampleSummaryJsonObj);
                            }
                            programLocationJsonObj.put(JSON_TAG_GROUP_NAME_SAMPLES_SUMMARY, programSampleSummaryJsonArray); 
                            fieldToRetrieveArr=new String[]{TblsEnvMonitData.Sample.FLD_CURRENT_STAGE.getName()};
                            whereFieldNames=new String[]{TblsEnvMonitData.Sample.FLD_PROGRAM_NAME.getName(), TblsEnvMonitData.Sample.FLD_LOCATION_NAME.getName()}; 
                            whereFieldValues=new Object[]{currProgram, locationName};
                            samplesCounterPerStage=Rdbms.getGrouper(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                                    fieldToRetrieveArr, 
                                    whereFieldNames, whereFieldValues,
                                    new String[]{"COUNTER desc"}); 
                            fieldToRetrieveArr=LPArray.addValueToArray1D(fieldToRetrieveArr, "COUNTER");                            
                            programSampleSummaryByStageJsonArray=new JSONArray();
                            for (Object[] curRec: samplesCounterPerStage){
                              jObj= LPJson.convertArrayRowToJSONObject(fieldToRetrieveArr, curRec);
                              programSampleSummaryByStageJsonArray.add(jObj);
                            }    
                            programLocationJsonObj.put(JSON_TAG_GROUP_NAME_SAMPLES_SUMMARY_BY_STAGE, programSampleSummaryByStageJsonArray); 
                            programLocationsJsonArray.add(programLocationJsonObj);
                        }                    
                        programJsonObj.put(JSON_TAG_GROUP_NAME_SAMPLE_POINTS, programLocationsJsonArray);   
                    }
                    if (!LPPlatform.LAB_FALSE.equalsIgnoreCase(programLocations[0][0].toString())){     
                        JSONArray programSampleSummaryJsonArray = new JSONArray();   
                        Object[] samplesStatusCounter = new Object[0];
                        for (Object statusList1 : statusList) {
                            String currStatus = statusList1.toString();
                            Integer contSmpStatus=0;
                            for (Object[] smpStatus: programSampleSummary){
                                if (currStatus.equalsIgnoreCase(smpStatus[0].toString())){contSmpStatus++;}
                            }
                            samplesStatusCounter = LPArray.addValueToArray1D(samplesStatusCounter, contSmpStatus);
                        }
                        for (int iStatuses=0; iStatuses < statusList.length; iStatuses++){
                            JSONObject programSampleSummaryJsonObj = new JSONObject();  
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_NAME, LPNulls.replaceNull(statusList[iStatuses]));
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_LABEL_EN, LPNulls.replaceNull(statusListEn[iStatuses]));
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_LABEL_ES, LPNulls.replaceNull(statusListEs[iStatuses]));
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_VALUE, LPNulls.replaceNull(samplesStatusCounter[iStatuses]));
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_TYPE, JSON_TAG_NAME_TYPE_VALUE_TEXT);
                            programSampleSummaryJsonObj.put(JSON_TAG_NAME_PSWD, JSON_TAG_NAME_PSWD_VALUE_FALSE);
                            programSampleSummaryJsonArray.add(programSampleSummaryJsonObj);
                        }
                        programJsonObj.put(JSON_TAG_GROUP_NAME_SAMPLES_SUMMARY, programSampleSummaryJsonArray);                             
                    }
                    programsJsonArr.add(programJsonObj);
                    JSONObject programDataTemplateDefinition = new JSONObject();
                    JSONObject templateProgramInfo=EnvMonFrontEndUtilities.dataProgramInfo(schemaPrefix, currProgram, null, null);
                    programDataTemplateDefinition.put(TblsEnvMonitData.Program.TBL.getName(), templateProgramInfo);
                    JSONArray templateProgramLocationInfo=EnvMonFrontEndUtilities.dataProgramLocationInfo(schemaPrefix, currProgram, null, null);
                    programDataTemplateDefinition.put(TblsEnvMonitData.ProgramLocation.TBL.getName(), templateProgramLocationInfo);
                    programJsonObj.put(JSON_TAG_PROGRAM_DATA_TEMPLATE_DEFINITION, programDataTemplateDefinition); 
                    Object specCode = templateProgramInfo.get(TblsEnvMonitData.Program.FLD_SPEC_CODE.getName());
                    Object specConfigVersion = templateProgramInfo.get(TblsEnvMonitData.Program.FLD_SPEC_CONFIG_VERSION.getName());                    
                    JSONObject specDefinition = new JSONObject();
                    if (!(specCode==null || specCode=="" || specConfigVersion==null || "".equals(specConfigVersion.toString()))){
                      JSONObject specInfo=SpecFrontEndUtilities.configSpecInfo(schemaPrefix, (String) specCode, (Integer) specConfigVersion, 
                              null, null);
                      specDefinition.put(TblsCnfg.Spec.TBL.getName(), specInfo);
                      JSONArray specLimitsInfo=SpecFrontEndUtilities.configSpecLimitsInfo(schemaPrefix, (String) specCode, (Integer) specConfigVersion, 
                              null, new String[]{TblsCnfg.SpecLimits.FLD_VARIATION_NAME.getName(), TblsCnfg.SpecLimits.FLD_ANALYSIS.getName(), 
                              TblsCnfg.SpecLimits.FLD_METHOD_NAME.getName(), TblsCnfg.SpecLimits.FLD_LIMIT_ID.getName()});
                      specDefinition.put(TblsCnfg.SpecLimits.TBL.getName(), specLimitsInfo);
                    }
                    programJsonObj.put(JSON_TAG_SPEC_DEFINITION, specDefinition); 
                }          
                JSONObject programsListObj = new JSONObject();
                programsListObj.put(JSON_TAG_GROUP_NAME_CARD_PROGRAMS_LIST, programsJsonArr);
                response.getWriter().write(programsListObj.toString());
                Rdbms.closeRdbms();                    
                Response.ok().build();
                return;  */
            default:      
                Rdbms.closeRdbms(); 
                //RequestDispatcher rd = request.getRequestDispatcher(SampleAPIParams.SERVLET_FRONTEND_URL);
                //rd.forward(request,response);   
        }
        }catch(Exception e){      
            Rdbms.closeRdbms();                   
            String[] errObject = new String[]{e.getMessage()};
            Object[] errMsg = LPFrontEnd.responseError(errObject, language, null);
            LPFrontEnd.servletReturnResponseErrorLPFalseDiagnostic(request, response, errMsg);
        } finally {
            // release database resources
            try {
                Rdbms.closeRdbms();   
            } catch (Exception ignore) {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException | ServletException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException | ServletException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
