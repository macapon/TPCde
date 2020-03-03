/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.app;

import com.oreilly.servlet.MultipartRequest;
import functionaljavaa.testingscripts.LPTestingOutFormat;
import static functionaljavaa.testingscripts.LPTestingOutFormat.TESTING_FILES_PATH;
import functionaljavaa.testingscripts.LPTestingParams;
import functionaljavaa.testingscripts.LPTestingParams.TestingServletsConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class TesterFromUploadFile extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)           throws ServletException, IOException {
        response = LPTestingOutFormat.responsePreparation(response);        
        try (PrintWriter out = response.getWriter()) {
            String saveDirectory="D:\\LP\\"; //TESTING_FILES_PATH;
            MultipartRequest mReq = new MultipartRequest(request, saveDirectory);
            String filename="";
            Enumeration files = mReq.getFileNames();
            while (files.hasMoreElements()) {
                String upload = (String) files.nextElement();
                filename = mReq.getFilesystemName(upload);
                String fullFileName=mReq.getOriginalFileName(upload);
                
                String csvPathName=saveDirectory+fullFileName;
                StringBuilder fileContentBuilder = new StringBuilder();
                String[][] headerInfo = LPArray.convertCSVinArray(csvPathName, "=");
                HashMap<String, Object> csvHeaderTags = LPTestingOutFormat.getCSVHeaderTester(headerInfo);
                if (csvHeaderTags.containsKey(LPPlatform.LAB_FALSE)){
                    fileContentBuilder.append("There are missing tags in the file header: ").append(csvHeaderTags.get(LPPlatform.LAB_FALSE));
                    out.println(fileContentBuilder.toString()); 
                    return;
                }                        
                String testerName = (String) csvHeaderTags.get(LPTestingOutFormat.FILEHEADER_TESTER_NAME_TAG_NAME);                           
                
                request.setAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_PATH, saveDirectory+"\\");
                request.setAttribute(LPTestingParams.UPLOAD_FILE_PARAM_FILE_NAME, fullFileName);
                
                TestingServletsConfig endPoints = TestingServletsConfig.valueOf(testerName);

                switch (endPoints){
                case NODB_SCHEMACONFIG_SPECQUAL_RULEFORMAT:
                case NODB_SCHEMACONFIG_SPECQUAL_RESULTCHECK:
                case NODB_SCHEMACONFIG_SPECQUANTI_RULEFORMAT:
                case NODB_SCHEMACONFIG_SPECQUANTI_RESULTCHECK:
                    RequestDispatcher rd = request.getRequestDispatcher(endPoints.getServletUrl());
                    rd.forward(request,response);   
                    return;                       
                default:
                    out.println("Tester name not recognized, "+testerName+". The tester cannot be completed"); 
                    return;
                }
            }
        } catch (IOException e){
            PrintWriter out = response.getWriter();
            out.println(e.getMessage()); 
            java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
