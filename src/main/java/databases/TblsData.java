/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import static databases.TblsCnfg.fieldsTag;
import static databases.TblsCnfg.ownerTag;
import static databases.TblsCnfg.schemaTag;
import static databases.TblsCnfg.tableTag;
import static databases.TblsCnfg.tablespaceTag;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDatabase;
import lbplanet.utilities.LPPlatform;
import static lbplanet.utilities.LPDatabase.*;

/**
 *
 * @author Administrator
 */
public class TblsData {
    private static final java.lang.String FIELDS_NAMES_LIGHT = "light";
    public static final String FIELDS_NAMES_USER_ID="user_id";
    public static final String FIELDS_NAMES_USER_NAME="user_name";

    /**
     *
     */
    public enum Sample{

        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_sample_id_seq'::regclass)")
        ,        
        TBL("sample", LPDatabase.CreateSequence(FLD_SAMPLE_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_SAMPLE_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_SAMPLE_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_CONFIG_CODE("sample_config_code", "bigint NOT NULL DEFAULT nextval(' #SCHEMA.#TBL_sample_id_seq'::regclass)")
        ,

        /**
         *
         */
        FLD_CONFIG_CODE_VERSION("sample_config_code_version", LPDatabase.Integer())
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS_PREVIOUS(FIELDS_NAMES_STATUS_PREVIOUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_LOGGED_ON("logged_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_LOGGED_BY("logged_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_RECEIVED_ON("received_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_RECEIVED_BY("received_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_VOLUME( LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_UOM( LPDatabase.FIELDS_NAMES_VOLUME_UOM, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_ALIQUOTED("aliquoted", LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_ALIQUOT_STATUS("aliq_status", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ("volume_for_aliq", LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ_UOM("volume_for_aliq_uom", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SAMPLING_DATE("sampling_date", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_SAMPLING_COMMENT("sampling_comment", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_INCUBATION_INCUBATOR("incubation_incubator", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_INCUBATION_START("incubation_start", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_INCUBATION_START_TEMPERATURE("incubation_start_temperature", LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_INCUBATION_START_TEMP_EVENT_ID("incubation_start_temp_event_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_INCUBATION_END("incubation_end", LPDatabase.dateTime())
        ,        

        /**
         *
         */
        FLD_INCUBATION_END_TEMPERATURE("incubation_end_temperature", LPDatabase.Real())        
        ,

        /**
         *
         */
        FLD_INCUBATION_END_TEMP_EVENT_ID("incubation_end_temp_event_id", LPDatabase.Integer())
        ,        

        /**
         *
         */
        FLD_INCUBATION_PASSED("incubation_passed", LPDatabase.Boolean(false))        
        ,

        /**
         *
         */
        FLD_INCUBATION2_INCUBATOR("incubation2_incubator", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_INCUBATION2_START("incubation2_start", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_INCUBATION2_START_TEMPERATURE("incubation2_start_temperature", LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_INCUBATION2_START_TEMP_EVENT_ID("incubation2_start_temp_event_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_INCUBATION2_END("incubation2_end", LPDatabase.dateTime())
        ,        

        /**
         *
         */
        FLD_INCUBATION2_END_TEMPERATURE("incubation2_end_temperature", LPDatabase.Real())        
        ,

        /**
         *
         */
        FLD_INCUBATION2_END_TEMP_EVENT_ID("incubation2_end_temp_event_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_INCUBATION2_PASSED("incubation2_passed", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_SPEC_CODE("spec_code", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_CODE_VERSION("spec_code_version", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_SPEC_VARIATION_NAME("spec_variation_name", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_ANALYSIS_VARIATION("spec_analysis_variation", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_EVAL(FIELDS_NAMES_SPEC_EVAL,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_CUSTODIAN(FIELDS_NAMES_CUSTODIAN,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_CUSTODIAN_CANDIDATE(FIELDS_NAMES_CUSTODIAN_CANDIDATE,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_COC_REQUESTED_ON("coc_requested_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_COC_CONFIRMED_ON(FIELDS_NAMES_COC_CONFIRMED_ON, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_CURRENT_STAGE("current_stage", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_PREVIOUS_STAGE("previous_stage", LPDatabase.StringNotNull())
        ;
        private Sample(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){return this.dbObjName;}
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = Sample.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (Sample obj: Sample.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }        
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }            
    private static final java.lang.String FIELDS_NAMES_STATUS_PREVIOUS = "status_previous";
    private static final java.lang.String FIELDS_NAMES_STATUS = "status";
    private static final java.lang.String FIELDS_NAMES_SPEC_EVAL = "spec_eval";
    private static final java.lang.String FIELDS_NAMES_CUSTODIAN_CANDIDATE = "custodian_candidate";
    private static final java.lang.String FIELDS_NAMES_CUSTODIAN = "custodian";
    private static final java.lang.String FIELDS_NAMES_COC_CONFIRMED_ON = "coc_confirmed_on";

    /**
     *
     */
    public enum SampleAnalysis{

        /**
         *
         */
        FLD_TEST_ID(FIELDS_NAMES_TEST_ID, "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_test_id_seq'::regclass)")
        ,        
        TBL("sample_analysis", LPDatabase.CreateSequence(FLD_TEST_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_TEST_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_TEST_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS_PREVIOUS(FIELDS_NAMES_STATUS_PREVIOUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_ANALYSIS(FIELDS_NAMES_ANALYSIS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_METHOD_NAME(LPDatabase.FIELDS_NAMES_METHOD_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_METHOD_VERSION(LPDatabase.FIELDS_NAMES_METHOD_VERSION, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_REPLICA(FIELDS_NAMES_REPLICA, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_ADDED_ON("added_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_ADDED_BY("added_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_SPEC_EVAL(FIELDS_NAMES_SPEC_EVAL,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_REVIEWER("reviewer", LPDatabase.String())
        ,        

        /**
         *
         */
        FLD_REVIEWER_ASSIGNED_ON("reviewer_assigned_on", LPDatabase.dateTime())        
        ,        

        /**
         *
         */
        FLD_REVIEWER_ASSIGNED_BY("reviewer_assigned_by", LPDatabase.String())        
        ,

        /**
         *
         */
        FLD_ANALYST("analyst", LPDatabase.String())
        ,        

        /**
         *
         */
        FLD_ANALYST_ASSIGNED_ON("analyst_assigned_on", LPDatabase.dateTime())        
        ,        

        /**
         *
         */
        FLD_ANALYST_ASSIGNED_BY("analyst_assigned_by", LPDatabase.String())        
        ,

        /**
         *
         */
        FLD_ANALYST_CERTIFICATION_MODE("analyst_certification_mode", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ALIQUOT_ID(FIELDS_NAMES_ALIQUOT_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_SUBALIQUOT_ID(FIELDS_NAMES_SUBALIQUOT_ID, LPDatabase.Integer())
        //, FLD_UNDER_DEVIATION("under_deviation", LPDatabase.Boolean()) Desviaciones aún no implementadas
/*     Este bloque de campos está a nivel de Sample, es posible que pueda ser interesante tb en sample_analysis   
        , FLD_VOLUME( LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        , FLD_VOLUME_UOM( LPDatabase.FIELDS_NAMES_VOLUME_UOM, LPDatabase.StringNotNull())
        , FLD_ALIQUOTED("aliquoted", LPDatabase.Boolean(false))
        , FLD_ALIQUOT_STATUS("aliq_status", LPDatabase.StringNotNull())
        , FLD_VOLUME_FOR_ALIQ("volume_for_aliq", LPDatabase.Real())
        , FLD_VOLUME_FOR_ALIQ_UOM("volume_for_aliq_uom", LPDatabase.StringNotNull())
        , FLD_SAMPLING_DATE("sampling_date", LPDatabase.dateTime())
        , FLD_SAMPLING_COMMENT("sampling_comment", LPDatabase.String())
        , FLD_INCUBATION_START("incubation_start", LPDatabase.dateTime())
        , FLD_INCUBATION_END("incubation_end", LPDatabase.dateTime())
        , FLD_INCUBATION_PASSED("incubation_passed", LPDatabase.Boolean())*/ 
        ;
        private SampleAnalysis(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleAnalysis.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleAnalysis obj: SampleAnalysis.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }        
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }            
    private static final java.lang.String FIELDS_NAMES_TEST_ID = "test_id";
    private static final java.lang.String FIELDS_NAMES_ANALYSIS = "analysis";
    private static final java.lang.String FIELDS_NAMES_REPLICA = "replica";
    private static final java.lang.String FIELDS_NAMES_SUBALIQUOT_ID = "subaliquot_id";
    private static final java.lang.String FIELDS_NAMES_ALIQUOT_ID = "aliquot_id";

    /**
     *
     */
    public enum SampleAnalysisResult{

        /**
         *
         */
        FLD_RESULT_ID("result_id", "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_result_id_seq'::regclass)")
        ,        
        TBL("sample_analysis_result", LPDatabase.CreateSequence(FLD_RESULT_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_RESULT_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_RESULT_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_TEST_ID(FIELDS_NAMES_TEST_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS_PREVIOUS(FIELDS_NAMES_STATUS_PREVIOUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_ANALYSIS(FIELDS_NAMES_ANALYSIS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_METHOD_NAME(LPDatabase.FIELDS_NAMES_METHOD_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_METHOD_VERSION(LPDatabase.FIELDS_NAMES_METHOD_VERSION, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_REPLICA(FIELDS_NAMES_REPLICA, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_PARAM_NAME("param_name", "")
        ,

        /**
         *
         */
        FLD_PARAM_TYPE("param_type", "")
        ,

        /**
         *
         */
        FLD_MANDATORY("mandatory", LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_REQUIRES_LIMIT("requires_limit", LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_RAW_VALUE("raw_value", "")
        ,

        /**
         *
         */
        FLD_PRETTY_VALUE("pretty_value", "")
        ,

        /**
         *
         */
        FLD_ENTERED_ON("entered_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_ENTERED_BY("entered_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_REENTERED("reentered", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_SPEC_EVAL(FIELDS_NAMES_SPEC_EVAL, LPDatabase.String(200))
        ,

        /**
         *
         */
        FLD_SPEC_EVAL_DETAIL("spec_eval_detail",  LPDatabase.String(200))
        ,        

        /**
         *
         */
        FLD_UOM("uom", LPDatabase.StringNotNull())        
        ,        

        /**
         *
         */
        FLD_UOM_CONVERSION_MODE("uom_conversion_mode", LPDatabase.StringNotNull())        
        ,

        /**
         *
         */
        FLD_ALIQUOT_ID(FIELDS_NAMES_ALIQUOT_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_SUBALIQUOT_ID(FIELDS_NAMES_SUBALIQUOT_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_LIMIT_ID("limit_id", LPDatabase.Integer())
        
        /* Este bloque de campos está a nivel de SampleAnalysis, es posible que pueda ser interesante tb en sample_analysis_result
        , FLD_REVIEWER("reviewer", LPDatabase.String())
        , FLD_REVIEWER_ASSIGNED_ON("reviewer_assigned_on", LPDatabase.dateTime())        
        , FLD_REVIEWER_ASSIGNED_BY("reviewer_assigned_by", LPDatabase.String())        
        , FLD_ANALYST("analyst", LPDatabase.String())
        , FLD_ANALYST_ASSIGNED_ON("analyst_assigned_on", LPDatabase.dateTime())        
        , FLD_ANALYST_ASSIGNED_BY("analyst_assigned_by", LPDatabase.String())        
        , FLD_ANALYST_CERTIFICATION_MODE("analyst_certification_mode", LPDatabase.String()) */
        //, FLD_UNDER_DEVIATION("under_deviation", LPDatabase.Boolean()) Desviaciones aún no implementadas
/*     Este bloque de campos está a nivel de Sample, es posible que pueda ser interesante tb en sample_analysis   
        , FLD_VOLUME( LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        , FLD_VOLUME_UOM( LPDatabase.FIELDS_NAMES_VOLUME_UOM, LPDatabase.StringNotNull())
        , FLD_ALIQUOTED("aliquoted", LPDatabase.Boolean(false))
        , FLD_ALIQUOT_STATUS("aliq_status", LPDatabase.StringNotNull())
        , FLD_VOLUME_FOR_ALIQ("volume_for_aliq", LPDatabase.Real())
        , FLD_VOLUME_FOR_ALIQ_UOM("volume_for_aliq_uom", LPDatabase.StringNotNull())
        , FLD_SAMPLING_DATE("sampling_date", LPDatabase.dateTime())
        , FLD_SAMPLING_COMMENT("sampling_comment", LPDatabase.String())
        , FLD_INCUBATION_START("incubation_start", LPDatabase.dateTime())
        , FLD_INCUBATION_END("incubation_end", LPDatabase.dateTime())
        , FLD_INCUBATION_PASSED("incubation_passed", LPDatabase.Boolean())*/ 
        ;
        private SampleAnalysisResult(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleAnalysisResult.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleAnalysisResult obj: SampleAnalysisResult.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }        
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }            

    /**
     *
     */
    public enum SampleAliq{

        /**
         *
         */
        FLD_ALIQUOT_ID(FIELDS_NAMES_ALIQUOT_ID, "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_aliquot_id_seq'::regclass)")
        ,        
        TBL("sample_aliq", LPDatabase.CreateSequence(FLD_ALIQUOT_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_ALIQUOT_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_ALIQUOT_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_CREATED_ON("created_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_CREATED_BY("created_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_SUBALIQ_STATUS("subaliq_status", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_VOLUME( LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_UOM( LPDatabase.FIELDS_NAMES_VOLUME_UOM, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ("volume_for_aliq", LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ_UOM("volume_for_aliq_uom", LPDatabase.StringNotNull())
        ;
        private SampleAliq(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleAliq.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleAliq obj: SampleAliq.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }  
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

    /**
     *
     */
    public enum SampleAliqSub{

        /**
         *
         */
        FLD_SUBALIQUOT_ID(FIELDS_NAMES_SUBALIQUOT_ID, "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_subaliquot_id_seq'::regclass)")
        ,        
        TBL("sample_aliq_sub", LPDatabase.CreateSequence(FLD_SUBALIQUOT_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_SUBALIQUOT_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_SUBALIQUOT_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_ALIQUOT_ID(FIELDS_NAMES_ALIQUOT_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_CREATED_ON("created_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_CREATED_BY("created_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_VOLUME( LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_UOM( LPDatabase.FIELDS_NAMES_VOLUME_UOM, LPDatabase.StringNotNull())
        ;
        private SampleAliqSub(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleAliqSub.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleAliqSub obj: SampleAliqSub.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }  
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

    /**
     *
     */
    public enum SampleCoc{

        /**
         *
         */
        FLD_ID("id", "integer NOT NULL DEFAULT nextval('#SCHEMA.#TBL_id_seq'::regclass)")
        ,        
        TBL("sample_coc", LPDatabase.CreateSequence(FLD_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_CUSTODIAN(FIELDS_NAMES_CUSTODIAN, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_CUSTODIAN_CANDIDATE(FIELDS_NAMES_CUSTODIAN_CANDIDATE, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STARTED_ON("coc_started_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_CONFIRMED_ON(FIELDS_NAMES_COC_CONFIRMED_ON, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_CUSTODIAN_NOTES("coc_custodian_notes", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_NEW_CUSTODIAN_NOTES("coc_new_custodian_notes", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SAMPLE_PICTURE("sample_picture", "json")
        ;
        private SampleCoc(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleCoc.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleCoc obj: SampleCoc.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }  
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

    /**
     *
     */
    public enum UserAnalysisMethod{

        /**
         *
         */
        FLD_USER_ANALYSIS_METHOD_ID("user_analysis_method_id", "integer NOT NULL DEFAULT nextval('#SCHEMA.#TBL_user_analysis_method_id_seq'::regclass)")
        ,        
        TBL("user_analysis_method", LPDatabase.CreateSequence(FLD_USER_ANALYSIS_METHOD_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_USER_ANALYSIS_METHOD_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_USER_ANALYSIS_METHOD_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_USER_ID(FIELDS_NAMES_USER_ID, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ANALYSIS(FIELDS_NAMES_ANALYSIS, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_METHOD_NAME(LPDatabase.FIELDS_NAMES_METHOD_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_METHOD_VERSION(LPDatabase.FIELDS_NAMES_METHOD_VERSION, LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_ASSIGNED_ON(FIELDS_NAMES_ASSIGNED_ON, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_ASSIGNED_BY("assigned_by", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_MANDATORY_LEVEL(FIELDS_NAMES_MANDATORY_LEVEL, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STARTED("started", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_COMPLETED("completed", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_EXPIRATION_DATE(FIELDS_NAMES_EXPIRATION_DATE, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_SOP_NAME(FIELDS_NAMES_SOP_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_USER_NAME(FIELDS_NAMES_USER_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_LIGHT(FIELDS_NAMES_LIGHT, LPDatabase.StringNotNull())
        ;
        private UserAnalysisMethod(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = UserAnalysisMethod.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (UserAnalysisMethod obj: UserAnalysisMethod.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }  
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }           
    private static final java.lang.String FIELDS_NAMES_ASSIGNED_ON = "assigned_on";
    private static final java.lang.String FIELDS_NAMES_MANDATORY_LEVEL = "mandatory_level";
    private static final java.lang.String FIELDS_NAMES_EXPIRATION_DATE = "expiration_date";
    private static final java.lang.String FIELDS_NAMES_SOP_NAME = "sop_name";

    /**
     *
     */
    public enum UserSop{

        /**
         *
         */
        FLD_USER_SOP_ID("user_sop_id", "integer NOT NULL DEFAULT nextval('#SCHEMA.#TBL_user_sop_id_seq'::regclass)")
        ,
        TBL("user_sop", LPDatabase.CreateSequence(FLD_USER_SOP_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_USER_SOP_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_USER_SOP_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */

        /**
         *
         */
        FLD_USER_ID(FIELDS_NAMES_USER_ID, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_SOP_ID("sop_id", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_SOP_LIST_ID("sop_list_id", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ASSIGNED_ON(FIELDS_NAMES_ASSIGNED_ON, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_ASSIGNED_BY("assigned_by", LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_MANDATORY_LEVEL(FIELDS_NAMES_MANDATORY_LEVEL, LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_READ_STARTED("read_started", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_READ_COMPLETED("read_completed", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_UNDERSTOOD("understood", LPDatabase.Boolean())
        ,

        /**
         *
         */
        FLD_EXPIRATION_DATE(FIELDS_NAMES_EXPIRATION_DATE, LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_SOP_NAME(FIELDS_NAMES_SOP_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_USER_NAME(FIELDS_NAMES_USER_NAME, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_LIGHT(FIELDS_NAMES_LIGHT, LPDatabase.StringNotNull())
        ;
        private UserSop(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = UserSop.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (UserSop obj: UserSop.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }  
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

    /**
     *
     */
    public enum ViewSampleCocNames{

        /**
         *
         */
        TBL("sample_coc_names", CreateView() +
                " SELECT smp_coc.sample_id, smp_coc.custodian, smp_coc.custodian_candidate, smp_coc.coc_started_on, smp_coc.coc_confirmed_on, smp_coc.coc_custodian_notes, "
                + "          smp_coc.coc_new_custodian_notes, smp_coc.sample_picture, smp_coc.id, smp_coc.status, usr_custodian.user_name AS custodian_name," +
                    "         usr_candidate.user_name AS candidate_name " +
                    "   FROM #SCHEMA.sample_coc smp_coc," +
                                "    #SCHEMA_APP.users usr_custodian," +
                                "    #SCHEMA_APP.users usr_candidate" +
                    "  WHERE smp_coc.custodian::text = usr_custodian.person_name::text AND smp_coc.custodian_candidate::text = usr_candidate.person_name::text; "+
                    "ALTER TABLE  #SCHEMA.#TBL  OWNER TO #OWNER;" +
                    "GRANT ALL ON TABLE  #SCHEMA.#TBL TO #OWNER;")
        ,

        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, ""),

        /**
         *
         */
        FLD_CUSTODIAN(FIELDS_NAMES_CUSTODIAN, ""),

        /**
         *
         */
        FLD_CUSTODIAN_CANDIDATE(FIELDS_NAMES_CUSTODIAN_CANDIDATE, "")
        ,

        /**
         *
         */
        FLD_COC_STARTED_ON("coc_started_on", ""),

        /**
         *
         */
        FLD_COC_CONFIRMED_ON(FIELDS_NAMES_COC_CONFIRMED_ON, ""),

        /**
         *
         */
        FLD_COC_CUSTODIAN_NOTES("coc_custodian_notes", "")
        ,

        /**
         *
         */
        FLD_NEW_CUSTODIAN_NOTES("coc_new_custodian_notes",""),

        /**
         *
         */
        FLD_SAMPLE_PICTURE("sample_picture", ""),

        /**
         *
         */
        FLD_ID("id", "")
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, ""),

        /**
         *
         */
        FLD_CUSTODIAN_NAME("custodian_name", ""),

        /**
         *
         */
        FLD_CANDIDATE_NAME("candidate_name", "")
        ;
        private ViewSampleCocNames(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ViewSampleCocNames.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_APP", LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ViewSampleCocNames obj: ViewSampleCocNames.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_APP", LPPlatform.SCHEMA_APP);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }        
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        
    
    /**
     *
     */
    public enum ViewUserAndMetaDataSopView{

        /**
         *
         */
        TBL("user_and_meta_data_sop_vw",  LPDatabase.CreateView() +
                " SELECT '#SCHEMA_CONFIG'::text AS procedure, usr.user_sop_id, usr.user_id, usr.sop_id, usr.sop_list_id, usr.assigned_on, usr.assigned_by, usr.status, usr.mandatory_level," +
                "            usr.read_started, usr.read_completed, usr.understood, usr.expiration_date, usr.sop_name, usr.user_name, usr.light, metadata.brief_summary, metadata.file_link, metadata.author " +
                "   FROM #SCHEMA.user_sop usr," +
                "    #SCHEMA_CONFIG.sop_meta_data metadata" +
                "  WHERE usr.sop_name::text = metadata.sop_name::text; "+
                    "ALTER TABLE  #SCHEMA.#TBL  OWNER TO #OWNER;" +
                    "GRANT ALL ON TABLE  #SCHEMA.#TBL TO #OWNER;")
        ,

        /**
         *
         */
        FLD_PROCEDURE("procedure", ""),

        /**
         *
         */
        FLD_USER_SOP_ID("user_sop_id", ""),

        /**
         *
         */
        FLD_USER_ID(FIELDS_NAMES_USER_ID, "")
        ,

        /**
         *
         */
        FLD_SOP_LIST_ID("sop_list_id", ""),

        /**
         *
         */
        FLD_ASSIGNED_ON(FIELDS_NAMES_ASSIGNED_ON, ""),

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, ""),

        /**
         *
         */
        FLD_MANDATORY_LEVEL(FIELDS_NAMES_MANDATORY_LEVEL, "")
        ,

        /**
         *
         */
        FLD_READ_STARTED("read_started",""),

        /**
         *
         */
        FLD_READ_COMPLETED("read_completed", ""),

        /**
         *
         */
        FLD_UNDERSTOOD("understood", "")
        ,

        /**
         *
         */
        FLD_EXPIRATION_DATE(FIELDS_NAMES_EXPIRATION_DATE, ""),

        /**
         *
         */
        FLD_SOP_NAME(FIELDS_NAMES_SOP_NAME, ""),

        /**
         *
         */
        FLD_USER_NAME(FIELDS_NAMES_USER_NAME, ""),

        /**
         *
         */
        FLD_LIGHT(FIELDS_NAMES_LIGHT, ""),

        /**
         *
         */
        FLD_BRIEF_SUMMARY("brief_summary", "")
        ,

        /**
         *
         */
        FLD_FILE_LINK("file_link", ""),

        /**
         *
         */
        FLD_AUTHOR("author", "")
        ;
        private ViewUserAndMetaDataSopView(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ViewUserAndMetaDataSopView.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_CONFIG", LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_CONFIG));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ViewUserAndMetaDataSopView obj: ViewUserAndMetaDataSopView.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }        
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

    /**
     *
     */
    public enum ViewSampleAnalysisResultWithSpecLimits{

        /**
         *
         */
        TBL("sample_analysis_result_with_spec_limits",  LPDatabase.CreateView() +
                " SELECT #FLDS from #SCHEMA.sample_analysis_result sar " +
                "   INNER JOIN \"em-demo-a-data\".sample s on s.sample_id = sar.sample_id "+
                "    left outer join #SCHEMA_CONFIG.spec_limits spcLim on sar.limit_id=spcLim.limit_id; " +
                "ALTER VIEW  #SCHEMA.#TBL  OWNER TO #OWNER;")
        ,

        /**
         *
         */
        FLD_RESULT_ID("result_id", "sar.result_id")
        ,

        /**
         *
         */
        FLD_TEST_ID(FIELDS_NAMES_TEST_ID, "sar.test_id")
        ,

        /**
         *
         */
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, "sar.sample_id")
        ,

        /**
         *
         */
        FLD_STATUS(FIELDS_NAMES_STATUS, "sar.status")
        ,

        /**
         *
         */
        FLD_STATUS_PREVIOUS(FIELDS_NAMES_STATUS_PREVIOUS, "sar.status_previous")
        ,

        /**
         *
         */
        FLD_ANALYSIS(FIELDS_NAMES_ANALYSIS, "sar.analysis")
        ,

        /**
         *
         */
        FLD_METHOD_NAME(LPDatabase.FIELDS_NAMES_METHOD_NAME, "sar.method_name")
        ,

        /**
         *
         */
        FLD_METHOD_VERSION(LPDatabase.FIELDS_NAMES_METHOD_VERSION, "sar.method_version")
        ,

        /**
         *
         */
        FLD_REPLICA(FIELDS_NAMES_REPLICA, "sar.replica")
        ,

        /**
         *
         */
        FLD_PARAM_NAME("param_name", "sar.param_name")
        ,

        /**
         *
         */
        FLD_PARAM_TYPE("param_type", "sar.param_type")
        ,

        /**
         *
         */
        FLD_MANDATORY("mandatory", "sar.mandatory")
        ,

        /**
         *
         */
        FLD_REQUIRES_LIMIT("requires_limit", "sar.requires_limit")
        ,

        /**
         *
         */
        FLD_RAW_VALUE("raw_value", "sar.raw_value")
        ,

        /**
         *
         */
        FLD_PRETTY_VALUE("pretty_value", "sar.pretty_value")
        ,

        /**
         *
         */
        FLD_ENTERED_ON("entered_on", "sar.entered_on")
        ,

        /**
         *
         */
        FLD_ENTERED_BY("entered_by", "sar.entered_by")
        ,

        /**
         *
         */
        FLD_REENTERED("reentered", "sar.reentered")
        ,

        /**
         *
         */
        FLD_SPEC_EVAL(FIELDS_NAMES_SPEC_EVAL, "sar.spec_eval")
        ,

        /**
         *
         */
        FLD_SPEC_EVAL_DETAIL("spec_eval_detail", "sar.spec_eval_detail")
        ,        

        /**
         *
         */
        FLD_UOM("uom", "sar.uom")        
        ,        

        /**
         *
         */
        FLD_UOM_CONVERSION_MODE("uom_conversion_mode", "sar.uom_conversion_mode")        
        ,

        /**
         *
         */
        FLD_ALIQUOT_ID(FIELDS_NAMES_ALIQUOT_ID, "sar.aliquot_id")
        ,

        /**
         *
         */
        FLD_SUBALIQUOT_ID(FIELDS_NAMES_SUBALIQUOT_ID, "sar.subaliquot_id")
        ,        

        /**
         *
         */
        FLD_SAMPLE_STATUS("sample_status", "s.status as sample_status"),
        FLD_CURRENT_STAGE("current_stage", "s.current_stage"),
        FLD_PROGRAM_NAME("program_name", "s.program_name"),
        FLD_SAMPLING_DATE("sampling_date", "s.sampling_date"),
        FLD_SHIFT("shift", "s.shift"),
        FLD_AREA("area", "s.area"),
        FLD_LOCATION_NAME("location_name", "s.location_name"),
        FLD_PRODUCTION_LOT("production_lot", "s.production_lot"),
        FLD_PROGRAM_DAY_ID("program_day_id", "s.program_day_id"),
        FLD_PROGRAM_DAY_DATE("program_day_date", "s.program_day_date"),
        
        FLD_LIMIT_ID("limit_id", "spcLim.limit_id")        
        ,

        /**
         *
         */
        FLD_SPEC_CODE("spec_code", "spcLim.code")
        ,

        /**
         *
         */
        FLD_SPEC_CONFIG_VERSION("spec_config_version", "spcLim.config_version")
        ,

        /**
         *
         */
        FLD_SPEC_VARIATION_NAME("spec_variation_name", "spcLim.variation_name")
        ,            

        /**
         *
         */
        FLD_ANALYSIS_SPEC_LIMITS("analysis_spec_limits", "spcLim.analysis")            
        ,

        /**
         *
         */
        FLD_METHOD_NAME_SPEC_LIMITS("method_name_spec_limits", "spcLim.method_name")
        ,

        /**
         *
         */
        FLD_METHOD_VERSION_SPEC_LIMITS("method_version_spec_limits", "spcLim.method_version")
        ,

        /**
         *
         */
        FLD_PARAMETER("parameter", "spcLim.parameter")
        ,

        /**
         *
         */
        FLD_RULE_TYPE("rule_type", "spcLim.rule_type")
        ,

        /**
         *
         */
        FLD_RULE_VARIABLES("rule_variables", "spcLim.rule_variables")
        ,

        /**
         *
         */
        FLD_UOM_SPEC_LIMITS("uom_spec_limits", "spcLim.uom")
        ,        

        /**
         *
         */
        FLD_UOM_CONVERSION_MODE_SPEC_LIMITS("uom_conversion_mode_spec_limits", "spcLim.uom_conversion_mode")        
        ;
        private ViewSampleAnalysisResultWithSpecLimits(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix
         * @param fields
         * @return
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ViewSampleAnalysisResultWithSpecLimits.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_CONFIG", LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_CONFIG));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ViewSampleAnalysisResultWithSpecLimits obj: ViewSampleAnalysisResultWithSpecLimits.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[1]).append(" AS ").append(currField[0]);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }      
        public static String[] getAllFieldNames(){
            String[] tableFields=new String[0];
            for (ViewSampleAnalysisResultWithSpecLimits obj: ViewSampleAnalysisResultWithSpecLimits.values()){
                String objName = obj.name();
                if (!"TBL".equalsIgnoreCase(objName)){
                    tableFields=LPArray.addValueToArray1D(tableFields, obj.getName());
                }
            }           
            return tableFields;
        }             
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        
    

        
}