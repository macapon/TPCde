/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Rdbms;
import databases.Token;
import functionaljavaa.instruments.incubator.DataIncubatorNoteBook;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class EnvMonIncubationAPIfrontend extends HttpServlet {

    /**
     *
     */
    public static final String API_ENDPOINT_INCUBATOR_TEMP_READINGS="INCUBATOR_TEMP_READINGS";  

    /**
     *
     */
    public static final String API_ENDPOINT_INCUBATORS_LIST="INCUBATORS_LIST";  

    /**
     *
     */
    public static final String MANDATORY_PARAMS_MAIN_SERVLET="actionName|finalToken";  

    /**
     *
     */
    public static final String MANDATORY_PARAMS_INCUBATOR_TEMP_READINGS="incubatorName";

    /**
     *
     */
    public static final String MANDATORY_PARAMS_INCUBATION_LAST_TEMP_READINGS="incubatorName";
    
    
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

    String[] mandatoryParams = new String[]{""};
    Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
        LPFrontEnd.servletReturnResponseError(request, response, 
            LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
        return;          
    }             
    String schemaPrefix = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX);            
    String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
    String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);                   
            
    Token token = new Token(finalToken);

    if (!LPFrontEnd.servletStablishDBConection(request, response))return;
        
    switch (actionName.toUpperCase()){
        case API_ENDPOINT_INCUBATORS_LIST: 
            String[] fieldsToRetrieve=new String[]{TblsEnvMonitConfig.InstrIncubator.FLD_NAME.getName()};
            String[] fieldsToRetrieveReadings=new String[]{TblsEnvMonitData.InstrIncubatorNoteBook.FLD_ID.getName(), TblsEnvMonitData.InstrIncubatorNoteBook.FLD_EVENT_TYPE.getName(),
                        TblsEnvMonitData.InstrIncubatorNoteBook.FLD_CREATED_ON.getName(), TblsEnvMonitData.InstrIncubatorNoteBook.FLD_CREATED_BY.getName(),
                        TblsEnvMonitData.InstrIncubatorNoteBook.FLD_TEMPERATURE.getName()};     
            Rdbms.stablishDBConection();
            Object[][] incubatorsList=Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_CONFIG), TblsEnvMonitConfig.InstrIncubator.TBL.getName(), 
                    new String[]{TblsEnvMonitConfig.InstrIncubator.FLD_ACTIVE.getName()}, new Object[]{true}, 
                    fieldsToRetrieve, new String[]{TblsEnvMonitConfig.InstrIncubator.FLD_NAME.getName()});
            JSONArray jArr = new JSONArray();
            for (Object[] currInstrument: incubatorsList){
                JSONObject jObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currInstrument);
                Object[][] instrReadings=DataIncubatorNoteBook.getLastTemperatureReading(schemaPrefix, currInstrument[0].toString(), 5);                    
                JSONArray jReadingsArr = new JSONArray();
                for (Object[] curReading: instrReadings){
                    JSONObject jReadingsObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieveReadings, curReading);
                    jReadingsArr.add(jReadingsObj);
                }
                jObj.put("LAST_READINGS", jReadingsArr);
                jArr.add(jObj);
            }
            Rdbms.closeRdbms();  
            LPFrontEnd.servletReturnSuccess(request, response, jArr);
            break;
        case API_ENDPOINT_INCUBATOR_TEMP_READINGS:
            areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_INCUBATOR_TEMP_READINGS.split("\\|"));                       
            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
                LPFrontEnd.servletReturnResponseError(request, response, 
                    LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
                return;          
            }  
            String instrName=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_INCUBATOR_NAME);                  
            String numPoints=request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_INCUBATOR_NUM_POINTS);                     
            Integer numPointsInt=null;
            fieldsToRetrieve=new String[]{TblsEnvMonitData.InstrIncubatorNoteBook.FLD_ID.getName(), TblsEnvMonitData.InstrIncubatorNoteBook.FLD_EVENT_TYPE.getName(),
                        TblsEnvMonitData.InstrIncubatorNoteBook.FLD_CREATED_ON.getName(), TblsEnvMonitData.InstrIncubatorNoteBook.FLD_CREATED_BY.getName(),
                        TblsEnvMonitData.InstrIncubatorNoteBook.FLD_TEMPERATURE.getName()};            
            if (numPoints!=null) numPointsInt=Integer.valueOf(numPoints);                    
            Object[][] instrReadings=DataIncubatorNoteBook.getLastTemperatureReading(schemaPrefix, instrName, numPointsInt);                    
            Rdbms.closeRdbms();  
            jArr = new JSONArray();
            for (Object[] currReading: instrReadings){
                JSONObject jObj=LPJson.convertArrayRowToJSONObject(fieldsToRetrieve, currReading);
                jArr.add(jObj);
            }
            LPFrontEnd.servletReturnSuccess(request, response, jArr);
            break;
        default:      
            Rdbms.closeRdbms(); 
                LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);                                                                  
    }
    }catch(Exception e){      
        String exceptionMessage =e.getMessage();
        if (exceptionMessage==null){exceptionMessage="null exception";}
        response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);     
        LPFrontEnd.servletReturnResponseError(request, response, exceptionMessage, null, null);      
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
