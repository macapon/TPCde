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
/*         try (PrintWriter out = response.getWriter()) {
        String sampleData="{\"sample_id\":831,\"sample_config_code\":\"program_smp_template\",\"sample_config_code_version\":1,\"spec_eval\":null,\"received_on\":null,\"received_by\":null,\"status\":\"ￜLR\\u0016\\u001d0ﾲ\\u0010$￹Nwﾾ<￬ﾺ\",\"status_previous\":null,\"sampling_comment\":null,\"sampling_dateTIMEnoTZ\":null,\"sampling_dateDATE\":null,\"spec_code\":\"LlenadoVialesFA2018\",\"spec_code_version\":1,\"spec_variation_name\":\"GradoA\",\"custodian\":null,\"coc_requested_on\":null,\"coc_confirmed_on\":null,\"custodian_candidate\":null,\"volume\":null,\"volume_uom\":null,\"aliquoted\":false,\"aliq_status\":null,\"logged_by\":null,\"logged_on\":null,\"volume_for_aliqreal\":null,\"volume_for_aliq_uom\":null,\"volume_for_aliq\":null,\"incubation_start\":\"2020-03-30T20:21:10.8397\",\"incubation_end\":\"2020-03-30T20:21:11.4119\",\"incubation_passed\":true,\"program_name\":\"LlenadoVialesFA2018\",\"location_name\":\"V03F\",\"program_day_id\":null,\"program_day_date\":null,\"manuf_object\":null,\"shift\":null,\"sampling_time\":null,\"reading_date\":null,\"area\":null,\"spec_analysis_variation\":\"Contacto-Contacto\",\"current_stage\":\"MicroorganismIdentification\",\"incubation2_start\":\"2020-03-30T20:21:42.62266\",\"incubation2_end\":\"2020-03-30T20:21:43.207899\",\"incubation2_passed\":true,\"previous_stage\":\"PlateReading\",\"sampling_date\":\"2020-03-30T20:21:04.146049\",\"production_lot\":null,\"incubation_incubator\":\"INC_1\",\"incubation_start_temperature\":20.5,\"incubation2_incubator\":\"INC_1\",\"incubation2_start_temperature\":20.5,\"incubation_start_temp_event_id\":106,\"incubation_end_temp_event_id\":106,\"incubation2_start_temp_event_id\":107,\"incubation2_end_temp_event_id\":107,\"incubation_end_temperature\":20.5,\"incubation2_end_temperature\":20.5,\"incubation_batch\":\"batch23Incub1\",\"incubation2_batch\":\"batch6Incub2\",\"sampler_area\":null,\"sample_analysis\":[{\"test_id\":1924,\"sample_id\":831,\"analysis\":\"Contacto\",\"method_name\":\"Contacto\",\"method_version\":1,\"added_by\":\"labplanet\",\"status\":\"COMPLETE\",\"spec_eval\":null,\"analyst\":null,\"analyst_assigned_by\":null,\"analyst_certification_mode\":null,\"reviewer\":null,\"reviewer_assigned_on\":null,\"reviewer_assigned_by\":null,\"under_deviation\":null,\"deviation_name\":null,\"deviation_added_on\":null,\"deviation_added_by\":null,\"replica\":null,\"status_previous\":null,\"aliquot_id\":null,\"subaliquot_id\":null,\"added_on\":\"2020-03-30\",\"analyst_assigned_on\":null,\"sample_analysis_result\":[{\"result_id\":863,\"test_id\":1924,\"sample_id\":831,\"analysis\":\"Contacto\",\"method_name\":\"Contacto\",\"method_version\":1,\"param_name\":\"Recuento\",\"raw_value\":\"6\",\"entered_on\":\"2020-03-30T21:39:10.70773+02:00\",\"entered_by\":\"1\",\"reentered\":false,\"status\":\"RE-ENTERED\",\"spec_eval\":\"OUT_SPEC_MAX\",\"param_type\":\"quantitative\",\"requires_limit\":false,\"mandatory\":true,\"pretty_value\":null,\"under_deviation\":false,\"added_deviation_on\":null,\"added_deviation_by\":null,\"deviation_name\":null,\"deviation_status\":null,\"spec_eval_detail\":\"6 > 1.0, this value is greater than then maximum expected\",\"replica\":1,\"status_previous\":null,\"uom\":\"\",\"uom_conversion_mode\":\"\",\"aliquot_id\":null,\"subaliquot_id\":null,\"limit_id\":30}]}],\"sample_audit\":[{\"audit_id\":4603,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_LOGGED\",\"transaction_id\":0,\"fields_updated\":\"[spec_code:LlenadoVialesFA2018, spec_variation_name:GradoA, spec_code_version:1, spec_analysis_variation:Contacto-Contacto, program_name:LlenadoVialesFA2018, location_name:V03F, status:RECEIVED, sample_config_code:program_smp_template, sample_config_code_version:1, current_stage:Sampling]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:00.839794\"},{\"audit_id\":4604,\"table_name\":\"sample_analysis\",\"table_id\":1924,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_ADDED\",\"transaction_id\":0,\"fields_updated\":\"[analysis:Contacto, method_name:Contacto, method_version:1, status:, sample_id:831, added_on:2020-03-30, added_by:labplanet]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4603,\"date\":\"2020-03-30T20:21:01.562223\"},{\"audit_id\":4605,\"table_name\":\"samplesample_analysis_result\",\"table_id\":863,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_ADDED\",\"transaction_id\":0,\"fields_updated\":\"[param_name:Recuento, mandatory:true, analysis:Contacto, param_type:quantitative, replica:1, uom:, uom_conversion_mode:, sample_id:831, test_id:1924, status:, method_name:Contacto, method_version:1, limit_id:30]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":863,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4603,\"date\":\"2020-03-30T20:21:01.783959\"},{\"audit_id\":4606,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_ADDED:SAMPLE_EVALUATE_STATUS\",\"transaction_id\":0,\"fields_updated\":\"[status: keep status RECEIVED]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4603,\"date\":\"2020-03-30T20:21:01.927579\"},{\"audit_id\":4615,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SET_SAMPLING_DATE\",\"transaction_id\":0,\"fields_updated\":\"[sampling_date12020-03-30T20:21:04.146049]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:04.186048\"},{\"audit_id\":4616,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SETSAMPLINGDATE\",\"transaction_id\":0,\"fields_updated\":\"[current_stagelabplanetIncubation, previous_stagelabplanetSampling]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:09.229049\"},{\"audit_id\":4617,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_SET_INCUBATION_STARTED\",\"transaction_id\":0,\"fields_updated\":\"[incubation_start12020-03-30T20:21:10.839745500, incubation_incubator1INC_1, incubation_start_temp_event_id1106, incubation_start_temperature120.5, incubation_passed1false]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:10.877749\"},{\"audit_id\":4618,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_SET_INCUBATION_ENDED\",\"transaction_id\":0,\"fields_updated\":\"[incubation_end12020-03-30T20:21:11.411910300, incubation_incubator1INC_1, incubation_end_temp_event_id1106, incubation_end_temperature120.5, incubation_passed1true]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:11.448928\"},{\"audit_id\":4619,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"EM_BATCH_INCUB_END\",\"transaction_id\":0,\"fields_updated\":\"[current_stagelabplanetPlateReading, previous_stagelabplanetIncubation]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:41.207393\"},{\"audit_id\":4620,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_SET_INCUBATION_STARTED\",\"transaction_id\":0,\"fields_updated\":\"[incubation2_start12020-03-30T20:21:42.622660300, incubation2_incubator1INC_1, incubation2_start_temp_event_id1107, incubation2_start_temperature120.5, incubation2_passed1false]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:42.658665\"},{\"audit_id\":4621,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_SET_INCUBATION_ENDED\",\"transaction_id\":0,\"fields_updated\":\"[incubation2_end12020-03-30T20:21:43.207899300, incubation2_incubator1INC_1, incubation2_end_temp_event_id1107, incubation2_end_temperature120.5, incubation2_passed1true]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:43.245725\"},{\"audit_id\":4622,\"table_name\":\"sample_analysis_result\",\"table_id\":863,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED\",\"transaction_id\":0,\"fields_updated\":\"[raw_value:1, spec_eval:OUT_SPEC_MAX, spec_eval_detail:1 > 1.0, this value is greater than then maximum expected, entered_by:1, entered_on:2020-03-30T20:21:44.522834700, status:RE-ENTERED, limit_id:30]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":863,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:44.565833\"},{\"audit_id\":4623,\"table_name\":\"analysis\",\"table_id\":1924,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED:SAMPLE_ANALYSIS_EVALUATE_STATUS\",\"transaction_id\":0,\"fields_updated\":\"[status:COMPLETE]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4622,\"date\":\"2020-03-30T20:21:44.893725\"},{\"audit_id\":4624,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED:SAMPLE_EVALUATE_STATUS\",\"transaction_id\":0,\"fields_updated\":\"[status: keep status RECEIVED]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4622,\"date\":\"2020-03-30T20:21:45.056934\"},{\"audit_id\":4625,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"MICROORGANISM_ADDED\",\"transaction_id\":0,\"fields_updated\":\"[Added microorganism COVID-19]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:45.597379\"},{\"audit_id\":4626,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"MICROORGANISM_ADDED\",\"transaction_id\":0,\"fields_updated\":\"[Added microorganism PEPE]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:45.75938\"},{\"audit_id\":4627,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"MICROORGANISM_ADDED\",\"transaction_id\":0,\"fields_updated\":\"[Added microorganism ]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T20:21:46.042954\"},{\"audit_id\":4646,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"ENTERRESULT\",\"transaction_id\":0,\"fields_updated\":\"[current_stagelabplanetMicroorganismIdentification, previous_stagelabplanetPlateReading]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:34:58.091185\"},{\"audit_id\":4653,\"table_name\":\"sample_analysis_result\",\"table_id\":863,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED\",\"transaction_id\":0,\"fields_updated\":\"[raw_value:6, spec_eval:OUT_SPEC_MAX, spec_eval_detail:6 > 1.0, this value is greater than then maximum expected, entered_by:1, entered_on:2020-03-30T21:39:10.707729800, status:RE-ENTERED, limit_id:30]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":863,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:39:10.748787\"},{\"audit_id\":4654,\"table_name\":\"analysis\",\"table_id\":1924,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED:SAMPLE_ANALYSIS_EVALUATE_STATUS\",\"transaction_id\":0,\"fields_updated\":\"[status:COMPLETE]\",\"sample_id\":831,\"test_id\":1924,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4653,\"date\":\"2020-03-30T21:39:11.084879\"},{\"audit_id\":4655,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLE_ANALYSIS_RESULT_ENTERED:SAMPLE_EVALUATE_STATUS\",\"transaction_id\":0,\"fields_updated\":\"[status: keep status RECEIVED]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2986,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":4653,\"date\":\"2020-03-30T21:39:11.242318\"},{\"audit_id\":4656,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETOPREVIOUS\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetPlateReading, previous_stagelabplanetMicroorganismIdentification]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:51:46.190951\"},{\"audit_id\":4657,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETOPREVIOUS\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetIncubation, previous_stagelabplanetPlateReading]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:52:33.153172\"},{\"audit_id\":4658,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETOPREVIOUS\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetSampling, previous_stagelabplanetIncubation]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:52:47.412249\"},{\"audit_id\":4659,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETONEXT\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetIncubation, previous_stagelabplanetSampling]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:54:55.758368\"},{\"audit_id\":4660,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETONEXT\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetPlateReading, previous_stagelabplanetIncubation]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:55:25.47362\"},{\"audit_id\":4661,\"table_name\":\"sample\",\"table_id\":831,\"person\":\"1\",\"action_name\":\"SAMPLESTAGE_MOVETONEXT\",\"transaction_id\":1,\"fields_updated\":\"[current_stagelabplanetMicroorganismIdentification, previous_stagelabplanetPlateReading]\",\"sample_id\":831,\"test_id\":null,\"result_id\":null,\"user_role\":\"coordinator\",\"procedure\":null,\"procedure_version\":null,\"picture_after\":null,\"app_session_id\":2995,\"aliquot_id\":null,\"subaliquot_id\":null,\"reviewed\":null,\"reviewed_by\":null,\"reviewed_on\":null,\"revision_note\":null,\"level\":null,\"parent_audit_id\":null,\"date\":\"2020-03-30T21:56:22.157409\"}]}";
        
        JsonObject sampleStructure = LPJson.convertToJsonObjectStringedObject(sampleData);
        String samplingDate=sampleStructure.get("sampling_date").getAsString();
        JsonArray smpAna=sampleStructure.getAsJsonArray("sample_analysis");
        JsonElement jGet = smpAna.get(0);        
        JsonObject asJsonObject = jGet.getAsJsonObject();
        JsonArray asJsonArray = asJsonObject.getAsJsonArray("sample_analysis_result"); //
        jGet = asJsonArray.get(0);        
        asJsonObject = jGet.getAsJsonObject();
        String rawValue=asJsonObject.get("raw_value").getAsString();
        String paramName=asJsonObject.get("param_name").getAsString();
        out.println(paramName + rawValue);
        //sample_analysis_result
        out.println(samplingDate);
        
         }
if (1==1) return;*/
        
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
