/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import com.labplanet.servicios.app.GlobalAPIsParams;
import static com.labplanet.servicios.moduleenvmonit.EnvMonitAPIParams.MANDATORY_PARAMS_MAIN_SERVLET;
import databases.Rdbms;
import static functionaljavaa.testingscripts.LPTestingOutFormat.getAttributeValue;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPAPIArguments;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import static lbplanet.utilities.LPFrontEnd.noRecordsInTableMessage;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPJson;
import static lbplanet.utilities.LPKPIs.getKPIs;
import lbplanet.utilities.LPNulls;
import lbplanet.utilities.LPPlatform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class EnvMonAPIStats extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public enum EnvMonAPIstatsEndpoints{
        /**
         *
         */                
        KPI_PRODUCTION_LOT_SAMPLES("KPI_PRODUCTION_LOT_SAMPLES", new LPAPIArguments[]{
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_LOT_NAME, LPAPIArguments.ArgumentType.STRING.toString(), true, 6),
                new LPAPIArguments(EnvMonitAPIParams.REQUEST_PARAM_PROGRAM_NAME, LPAPIArguments.ArgumentType.STRING.toString(), false, 7),
                new LPAPIArguments(EnvMonitAPIParams.REQUEST_PARAM_PROD_LOT_FIELD_TO_RETRIEVE, LPAPIArguments.ArgumentType.STRING.toString(), false, 8),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_TO_RETRIEVE, LPAPIArguments.ArgumentType.STRING.toString(), false, 9),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_GROUPS, LPAPIArguments.ArgumentType.STRING.toString(), false, 10),
                //new LPAPIArguments(EnvMonitAPIParams., LPAPIArguments.ArgumentType.STRING.toString(), false, 7)
                }),
        KPIS("KPIS", new LPAPIArguments[]{
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_OBJ_GROUP_NAME, LPAPIArguments.ArgumentType.STRING.toString(), false, 6),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_TABLE_CATEGORY, LPAPIArguments.ArgumentType.STRINGARR.toString(), false, 7),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_TABLE_NAME, LPAPIArguments.ArgumentType.STRINGARR.toString(), false, 8),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_WHERE_FIELDS_NAME, LPAPIArguments.ArgumentType.STRINGARR.toString(), false, 9),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_WHERE_FIELDS_VALUE, LPAPIArguments.ArgumentType.STRINGARR.toString(), true, 10),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_FIELDS_TO_RETRIEVE_OR_GROUPING, LPAPIArguments.ArgumentType.STRINGARR.toString(), false, 12),
                new LPAPIArguments(GlobalAPIsParams.REQUEST_PARAM_GROUPED, LPAPIArguments.ArgumentType.BOOLEANARR.toString(), true, 11),
                }),        
        ;
        private EnvMonAPIstatsEndpoints(String name, LPAPIArguments[] argums){
            this.name=name;
            this.arguments=argums;  
        } 
        public  HashMap<HttpServletRequest, Object[]> testingSetAttributesAndBuildArgsArray(HttpServletRequest request, Object[][] contentLine, Integer lineIndex){  
            HashMap<HttpServletRequest, Object[]> hm = new HashMap();
            Object[] argValues=new Object[0];
            for (LPAPIArguments curArg: this.arguments){                
                argValues=LPArray.addValueToArray1D(argValues, curArg.getName()+":"+getAttributeValue(contentLine[lineIndex][curArg.getTestingArgPosic()], contentLine));
                request.setAttribute(curArg.getName(), getAttributeValue(contentLine[lineIndex][curArg.getTestingArgPosic()], contentLine));
            }  
            hm.put(request, argValues);            
            return hm;
        }        
        public String getName(){
            return this.name;
        }
        /**
         * @return the arguments
         */
        public LPAPIArguments[] getArguments() {
            return arguments;
        }     
        private final String name;
        private final LPAPIArguments[] arguments;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request=LPHttp.requestPreparation(request);
        response=LPHttp.responsePreparation(response);
        String language = LPFrontEnd.setLanguage(request);         
            
        Object[] areMandatoryParamsInResponse = LPHttp.areMandatoryParamsInApiRequest(request, MANDATORY_PARAMS_MAIN_SERVLET.split("\\|"));                       
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            LPFrontEnd.servletReturnResponseError(request, response, 
                LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);              
            return;          
        }             
        String schemaPrefix = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX);            
        String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
            
        //Token token = new Token(finalToken);
        EnvMonAPIstatsEndpoints endPoint = null;
        try{
            endPoint = EnvMonAPIstatsEndpoints.valueOf(actionName.toUpperCase());
        }catch(Exception e){
            LPFrontEnd.servletReturnResponseError(request, response, LPPlatform.API_ERRORTRAPING_PROPERTY_ENDPOINT_NOT_FOUND, new Object[]{actionName, this.getServletName()}, language);              
            return;                   
        }
        areMandatoryParamsInResponse = LPHttp.areEndPointMandatoryParamsInApiRequest(request, endPoint.getArguments());
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            LPFrontEnd.servletReturnResponseError(request, response,
                    LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()}, language);
            return;
        }        
        Object[] argValues=LPAPIArguments.buildAPIArgsumentsArgsValues(request, endPoint.getArguments());
        try (PrintWriter out = response.getWriter()) {
        
            if (!LPFrontEnd.servletStablishDBConection(request, response)){return;}

            switch (endPoint){
                case KPI_PRODUCTION_LOT_SAMPLES: 
                    JSONObject jObjMainObject=new JSONObject();
                    String prodLotName=argValues[0].toString();
                    String prodLotFieldToRetrieve = request.getParameter(EnvMonitAPIParams.REQUEST_PARAM_PROD_LOT_FIELD_TO_RETRIEVE);
                    String[] prodLotFieldToRetrieveArr=new String[0];
                    if ((prodLotFieldToRetrieve!=null) && (prodLotFieldToRetrieve.length()>0))
                        if ("ALL".equalsIgnoreCase(prodLotFieldToRetrieve)) prodLotFieldToRetrieveArr=TblsEnvMonitData.ProductionLot.getAllFieldNames();
                        else prodLotFieldToRetrieveArr=prodLotFieldToRetrieve.split("\\|");
                    if (prodLotFieldToRetrieve==null)
                        prodLotFieldToRetrieveArr=TblsEnvMonitData.ProductionLot.getAllFieldNames();
                    String sampleFieldToRetrieve = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_FIELD_TO_RETRIEVE);                    
                    String[] sampleFieldToRetrieveArr=new String[0];
                    if ((sampleFieldToRetrieve!=null) && (sampleFieldToRetrieve.length()>0))
                        if ("ALL".equalsIgnoreCase(sampleFieldToRetrieve)) sampleFieldToRetrieveArr=TblsEnvMonitData.Sample.getAllFieldNames();
                        else sampleFieldToRetrieveArr=sampleFieldToRetrieve.split("\\|");
                    if (sampleFieldToRetrieve==null)
                        sampleFieldToRetrieveArr=TblsEnvMonitData.Sample.getAllFieldNames();
                    Object[][] prodLotInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.ProductionLot.TBL.getName(), 
                            new String[]{TblsEnvMonitData.ProductionLot.FLD_LOT_NAME.getName()}, new Object[]{prodLotName}
                            , prodLotFieldToRetrieveArr, new String[]{TblsEnvMonitData.ProductionLot.FLD_CREATED_ON.getName()+" desc"} ); 
                    JSONObject jObj=new JSONObject();
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(prodLotInfo[0][0].toString())){
                         jObj= noRecordsInTableMessage();                    
                    }else{
                       for (Object[] curRec: prodLotInfo){
                         jObj= LPJson.convertArrayRowToJSONObject(prodLotFieldToRetrieveArr, curRec);
                       }
                    }
                    jObjMainObject.put(TblsEnvMonitData.ProductionLot.TBL.getName(), jObj);
                    
                    Object[][] sampleInfo = Rdbms.getRecordFieldsByFilter(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                            new String[]{TblsEnvMonitData.Sample.FLD_PRODUCTION_LOT.getName()}, new Object[]{prodLotName}
                            , sampleFieldToRetrieveArr , new String[]{TblsEnvMonitData.Sample.FLD_SAMPLE_ID.getName()+" desc"} ); 
                    jObj=new JSONObject();
                    JSONArray sampleJsonArr = new JSONArray();
                    if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())){
                         jObj= noRecordsInTableMessage();                    
                    }else{                       
                        for (Object[] curRec: sampleInfo){
                            jObj= LPJson.convertArrayRowToJSONObject(sampleFieldToRetrieveArr, curRec);
                            sampleJsonArr.add(jObj);
                        }
                    }    
                    jObjMainObject.put(TblsEnvMonitData.Sample.TBL.getName(), sampleJsonArr);
                    
                    String sampleGroups=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SAMPLE_GROUPS);
                    if (sampleGroups!=null){
                        String[] sampleGroupsArr=sampleGroups.split("\\|");
                        for (String currGroup: sampleGroupsArr){
                            JSONArray sampleGrouperJsonArr = new JSONArray();
                            String[] groupInfo = currGroup.split("\\*");
                            String[] smpGroupFldsArr=groupInfo[0].split(",");
                            Object[][] groupedInfo = Rdbms.getGrouper(LPPlatform.buildSchemaName(schemaPrefix, LPPlatform.SCHEMA_DATA), TblsEnvMonitData.Sample.TBL.getName(), 
                                    smpGroupFldsArr, new String[]{TblsEnvMonitData.Sample.FLD_PRODUCTION_LOT.getName()}, new Object[]{prodLotName}, 
                                    null);
                            smpGroupFldsArr=LPArray.addValueToArray1D(smpGroupFldsArr, "count");
                            jObj=new JSONObject();
                            if (LPPlatform.LAB_FALSE.equalsIgnoreCase(sampleInfo[0][0].toString())){
                                jObj= noRecordsInTableMessage();                    
                            }else{                       
                                for (Object[] curRec: groupedInfo){
                                    jObj= LPJson.convertArrayRowToJSONObject(smpGroupFldsArr, curRec);
                                    sampleGrouperJsonArr.add(jObj);
                                }
                            } 
                            jObjMainObject.put(groupInfo[1], sampleGrouperJsonArr);
                        }
                    }                    
                    
                    LPFrontEnd.servletReturnSuccess(request, response, jObjMainObject);
                    return;
                case KPIS: 
                    jObjMainObject=new JSONObject();
                    String[] objGroupName = LPNulls.replaceNull(argValues[0]).toString().split("\\/");
                    String[] tblCategory=argValues[1].toString().split("\\/");
                    String[] tblName=argValues[2].toString().split("\\/");
                    String[] whereFieldsNameArr=argValues[3].toString().split("\\/");
                    String[] whereFieldsValueArr=argValues[4].toString().split("\\/");
                    String[] fldToRetrieve=argValues[5].toString().split("\\/");
                    String[] dataGrouped=argValues[6].toString().split("\\/");

                    jObjMainObject=getKPIs(schemaPrefix, objGroupName, tblCategory, tblName, whereFieldsNameArr, whereFieldsValueArr, 
                        fldToRetrieve, dataGrouped);
                    LPFrontEnd.servletReturnSuccess(request, response, jObjMainObject);
            }
        }catch(Exception e){   
 /*           try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(sampleAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
*/            
            Rdbms.closeRdbms();                   
            String[] errObject = new String[]{e.getMessage()};
            Object[] errMsg = LPFrontEnd.responseError(errObject, language, null);
            response.sendError((int) errMsg[0], (String) errMsg[1]);           
        } finally {
            // release database resources
            try {
                //con.close();
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
