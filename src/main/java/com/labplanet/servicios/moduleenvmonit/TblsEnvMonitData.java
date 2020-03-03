/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.labplanet.servicios.moduleenvmonit;

import databases.DbObjects;
import static databases.TblsCnfg.fieldsTag;
import static databases.TblsCnfg.ownerTag;
import static databases.TblsCnfg.schemaTag;
import static databases.TblsCnfg.tableTag;
import static databases.TblsCnfg.tablespaceTag;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDatabase;
import static lbplanet.utilities.LPDatabase.dateTime;
import static lbplanet.utilities.LPDatabase.dateTimeWithDefaultNow;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author Administrator
 */
public class TblsEnvMonitData {

    /**
     *
     */
    public enum Program{

        /**
         *
         */
        TBL("program",  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_NAME("name",  LPDatabase.StringNotNull(100))
        ,

        /**
         *
         */
        FLD_PROGRAM_CONFIG_ID("program_config_id", LPDatabase.IntegerNotNull())
        ,

        /**
         *
         */
        FLD_PROGRAM_CONFIG_VERSION("program_config_version", LPDatabase.IntegerNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_CODE("spec_code", LPDatabase.String())
        ,        

        /**
         *
         */
        FLD_SPEC_CONFIG_VERSION("spec_config_version", LPDatabase.Integer())        
        ,

        /**
         *
         */
        FLD_ACTIVE( LPDatabase.FIELDS_NAMES_ACTIVE, LPDatabase.Boolean())
        // ...
        ;
        
        private Program(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = Program.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_DATA);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (Program obj: Program.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_DATA);
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
    public enum ProgramLocation{

        /**
         *
         */
        TBL("program_location",  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_PROGRAM_NAME(FIELDS_NAMES_PROGRAM_NAME, "character varying2100) COLLATE pg_catalog.\"default\" NOT NULL")
        ,

        /**
         *
         */
        FLD_LOCATION_NAME(FIELDS_NAMES_LOCATION_NAME,  LPDatabase.String(200))
        ,
        /**
         *
         */
        FLD_REQUIRES_PERSON_ANA("requires_person_ana", LPDatabase.Boolean()),
        /**
         *
         */
        FLD_PERSON_ANA_DEFINITION("person_ana_definition",LPDatabase.StringNotNull()),
        
//        , FLD_PROGRAM_CONFIG_VERSION("program_config_version", LPDatabase.String())
        // ...
        ;        
        private ProgramLocation(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ProgramLocation.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_DATA);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ProgramLocation obj: ProgramLocation.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_DATA);
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
    private static final String FIELDS_NAMES_LOCATION_NAME = "location_name";
    private static final String FIELDS_NAMES_PROGRAM_NAME = "program_name";

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
        FLD_STATUS("status",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_STATUS_PREVIOUS("status_previous",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_LOGGED_ON("logged_on", LPDatabase.Date())
        ,

        /**
         *
         */
        FLD_LOGGED_BY("logged_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_RECEIVED_ON("received_on", LPDatabase.Date())
        ,

        /**
         *
         */
        FLD_RECEIVED_BY("received_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_VOLUME(LPDatabase.FIELDS_NAMES_VOLUME, LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_UOM(LPDatabase.FIELDS_NAMES_VOLUME_UOM,LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_ALIQUOTED("aliquoted", LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_ALIQUOT_STATUS("aliq_status",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ("volume_for_aliq", LPDatabase.Real())
        ,

        /**
         *
         */
        FLD_VOLUME_FOR_ALIQ_UOM("volume_for_aliq_uom",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_CODE("spec_code",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_CODE_VERSION("spec_code_version", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_SPEC_VARIATION_NAME("spec_variation_name",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_SPEC_EVAL("spec_eval",  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_CUSTODIAN("custodian",  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_CUSTODIAN_CANDIDATE("custodian_candidate",  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_COC_REQUESTED_ON("coc_requested_on", LPDatabase.Date())
        ,

        /**
         *
         */
        FLD_COC_CONFIRMED_ON("coc_confirmed_on", LPDatabase.Date())
        ,

        /**
         *
         */
        FLD_PROGRAM_NAME(FIELDS_NAMES_PROGRAM_NAME,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_LOCATION_NAME(FIELDS_NAMES_LOCATION_NAME,  LPDatabase.StringNotNull(2))
        ,

        /**
         *
         */
        FLD_PRODUCTION_LOT("production_lot",  LPDatabase.StringNotNull(2)),
        /**
         *
         */
        FLD_SAMPLER_AREA("sampler_area",LPDatabase.StringNotNull()),
        FLD_SAMPLING_DATE("sampling_date", dateTime())
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

        FLD_INCUBATION_BATCH("incubation_batch", LPDatabase.String())
        ,
        /**
         *
         */
        FLD_INCUBATION_START(FIELDS_NAMES_INCUBATION_START, dateTime())
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
        FLD_INCUBATION_END(FIELDS_NAMES_INCUBATION_END, dateTime())
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
        FLD_INCUBATION2_BATCH("incubation2_batch", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_INCUBATION2_START(FIELDS_NAMES_INCUBATION2_START, dateTime())
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
        FLD_INCUBATION2_END(FIELDS_NAMES_INCUBATION2_END, dateTime())
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
        FLD_CURRENT_STAGE("current_stage",LPDatabase.StringNotNull())
        ,

        /**
         *
         */
        FLD_PREVIOUS_STAGE("previous_stage",LPDatabase.StringNotNull())
        
        ;
        private Sample(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){return this.dbObjName;}
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
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
        public static String[] getAllFieldNames(){
            String[] tableFields=new String[0];
            for (Sample obj: Sample.values()){
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
    private static final String FIELDS_NAMES_INCUBATION2_END = "incubation2_end";
    private static final String FIELDS_NAMES_INCUBATION2_START = "incubation2_start";
    private static final String FIELDS_NAMES_INCUBATION_START = "incubation_start";
    private static final String FIELDS_NAMES_INCUBATION_END = "incubation_end";

    /**
     *
     */
    public enum SampleMicroorganism{

        /**
         *
         */
        FLD_ID("id", "bigint NOT NULL DEFAULT nextval(' #SCHEMA.#TBL_id_seq'::regclass)")
        ,
        TBL("sample_microorganism", LPDatabase.CreateSequence(FLD_ID.getName())
                
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
        //, FLD_TEST_ID("test_id", LPDatabase.Integer())
        //, FLD_RESULT_ID("result_id", LPDatabase.Integer())    
        ,

        /**
         *
         */
        FLD_CREATED_ON( LPDatabase.FIELDS_NAMES_CREATED_ON, dateTimeWithDefaultNow())
        ,

        /**
         *
         */
        FLD_CREATED_BY( LPDatabase.FIELDS_NAMES_CREATED_BY, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_MICROORG_NAME("microorganism_name", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_NOTE("note", LPDatabase.String())
        ;
        private SampleMicroorganism(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){return this.dbObjName;}
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = SampleMicroorganism.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (SampleMicroorganism obj: SampleMicroorganism.values()){
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
    public enum ProductionLot{

        /**
         *
         */
        TBL("production_lot",  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_LOT_NAME) ) " +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_LOT_NAME("lot_name",LPDatabase.StringNotNull())
        //, FLD_TEST_ID("test_id", LPDatabase.Integer())
        //, FLD_RESULT_ID("result_id", LPDatabase.Integer())    
        ,

        /**
         *
         */
        FLD_CREATED_ON( LPDatabase.FIELDS_NAMES_CREATED_ON, dateTimeWithDefaultNow())
        ,

        /**
         *
         */
        FLD_CREATED_BY( LPDatabase.FIELDS_NAMES_CREATED_BY, LPDatabase.String())
        ,

        /**
         *
         */
        FLD_CLOSED_ON("closed_on", dateTimeWithDefaultNow())
        ,

        /**
         *
         */
        FLD_CLOSED_BY("closed_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ACTIVE( LPDatabase.FIELDS_NAMES_ACTIVE, LPDatabase.Boolean())
        ;
        private ProductionLot(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){return this.dbObjName;}
        private String[] getDbFieldDefinitionPostgres(){return new String[]{this.dbObjName, this.dbObjTypePostgres};}

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ProductionLot.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ProductionLot obj: ProductionLot.values()){
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

    /*CREATE OR REPLACE VIEW "em-demo-a-data".sample_microorganism_list_vw AS
select s.sample_id, s.current_stage, s.program_name, s.location_name, s.incubation_start, s.incubation_end, s.incubation2_start, s.incubation2_end, sar.raw_value,
array_to_string(array_agg(distinct sorg.microorganism_name), ', ') as microorganism_list
-- array_to_string(ARRAY(sorg.microorganism_name), ', ') as microorganism_list
from "em-demo-a-data".sample_analysis_result as sar 
 inner join "em-demo-a-data".sample as s on sar.sample_id=s.sample_id
 left outer join "em-demo-a-data".sample_microorganism as sorg on sorg.sample_id=sar.sample_id
where sar.param_name='Recuento'       
group by s.sample_id, s.current_stage, s.program_name, s.location_name, s.incubation_start, s.incubation_end, s.incubation2_start, s.incubation2_end, sar.raw_value    
*/

    /**
     *
     */

    public enum ViewSampleMicroorganismList{

        /**
         *
         */
        TBL("sample_microorganism_list_vw", " CREATE OR REPLACE VIEW #SCHEMA.#TBL AS " +
               " SELECT distinct s.sample_id, s.sample_config_code, s.status, s.sampling_date, s.current_stage, s.program_name, s.location_name, s.incubation_start, s.incubation_end, s.incubation2_start, s.incubation2_end, "
               + "sar.raw_value, sar.result_id, sar.test_id, count(distinct sorg.id) as microorganism_count,"
               + "  array_to_string(array_agg(distinct sorg.microorganism_name), ', ') as microorganism_list" 
               + "   FROM #SCHEMA.sample_analysis_result as sar " +
                  "    inner join #SCHEMA.sample as s on sar.sample_id=s.sample_id " +
                  "       left outer join #SCHEMA.sample_microorganism as sorg on sorg.sample_id=sar.sample_id " +
                   "  where sar.param_name='Recuento' "+
                   " group by s.sample_id, s.status, s.sampling_date, s.current_stage, s.program_name, s.location_name, s.incubation_start, s.incubation_end, s.incubation2_start, s.incubation2_end, sar.raw_value, sar.result_id, sar.test_id;"+
                   "ALTER TABLE  #SCHEMA.#TBL  OWNER TO #OWNER;" +
                   "GRANT ALL ON TABLE  #SCHEMA.#TBL TO #OWNER;")
       ,
        FLD_SAMPLE_ID(LPDatabase.FIELDS_NAMES_SAMPLE_ID, ""),
        FLD_SAMPLE_TEMPLATE("sample_config_code", ""),
        FLD_STATUS("status", ""),
        FLD_CURRENT_STAGE("current_stage", ""),
        FLD_SAMPLING_DATE("sampling_date", "")
       ,

        /**
         *
         */
        FLD_PROGRAM_NAME(FIELDS_NAMES_PROGRAM_NAME, ""),

        /**
         *
         */
        FLD_LOCATION_NAME(FIELDS_NAMES_LOCATION_NAME, "")
       ,

        /**
         *
         */
        FLD_INCUBATION_START(FIELDS_NAMES_INCUBATION_START, ""),

        /**
         *
         */
        FLD_INCUBATION_END(FIELDS_NAMES_INCUBATION_END, "")
       ,

        /**
         *
         */
        FLD_INCUBATION2_START(FIELDS_NAMES_INCUBATION2_START, ""),

        /**
         *
         */
        FLD_INCUBATION2_END(FIELDS_NAMES_INCUBATION2_END, "")
       ,

        /**
         *
         */
        FLD_RESULT_ID(FIELDS_NAMES_INCUBATION2_START, ""),

        /**
         *
         */
        FLD_TEST_ID(FIELDS_NAMES_INCUBATION2_END, "")
       ,

        /**
         *
         */
        FLD_RAW_VALUE("raw_value",""),

        /**
         *
         */
        FLD_MICROORGANISM_COUNT("microorganism_count", ""),

        /**
         *
         */
        FLD_MICROORGANISM_LIST("microorganism_list", "")
       ;
       private ViewSampleMicroorganismList(String dbObjName, String dbObjType){
           this.dbObjName=dbObjName;
           this.dbObjTypePostgres=dbObjType;
       }

        /**
         *
         * @return entry name
         */
        public String getName(){
           return this.dbObjName;
       }
       private String[] getDbFieldDefinitionPostgres(){
           return new String[]{this.dbObjName, this.dbObjTypePostgres};
       }

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
           return createTableScriptPostgres(schemaNamePrefix, fields);
       }
       private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
           StringBuilder tblCreateScript=new StringBuilder();
           String[] tblObj = ViewSampleMicroorganismList.TBL.getDbFieldDefinitionPostgres();
           tblCreateScript.append(tblObj[1]);
           tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_APP", LPPlatform.SCHEMA_APP);
           tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
           tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
           tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
           tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
           StringBuilder fieldsScript=new StringBuilder();
           for (ViewSampleMicroorganismList obj: ViewSampleMicroorganismList.values()){
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
        public static String[] getAllFieldNames(){
            String[] tableFields=new String[0];
            for (ViewSampleMicroorganismList obj: ViewSampleMicroorganismList.values()){
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

    /**
     *
     */
    public enum InstrIncubatorNoteBook{

        /**
         *
         */
        FLD_ID("id", "bigint NOT NULL DEFAULT nextval(' #SCHEMA.#TBL_id_seq'::regclass)")
        ,
        TBL("instrument_incubator_notebook", LPDatabase.CreateSequence(FLD_ID.getName())
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
        FLD_NAME("name",  LPDatabase.StringNotNull(100))
        ,

        /**
         *
         */
        FLD_CREATED_BY( LPDatabase.FIELDS_NAMES_CREATED_BY,  LPDatabase.String(200))
        ,

        /**
         *
         */
        FLD_CREATED_ON( LPDatabase.FIELDS_NAMES_CREATED_ON, lbplanet.utilities.LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_EVENT_TYPE("event_type",  LPDatabase.String(200))
        ,

        /**
         *
         */
        FLD_TEMPERATURE("temperature", LPDatabase.Real())
        ;        
        private InstrIncubatorNoteBook(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = InstrIncubatorNoteBook.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (InstrIncubatorNoteBook obj: InstrIncubatorNoteBook.values()){
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
    public enum IncubBatch{

        /**
         *
         */
        TBL("incub_batch",  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+LPDatabase.CreateTableSpace()+"  ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_NAME("name",  LPDatabase.StringNotNull(100))
        ,

        /**
         *
         */
        FLD_INCUB_BATCH_CONFIG_ID("incub_batch_config_id", LPDatabase.IntegerNotNull())
        ,

        /**
         *
         */
        FLD_INCUB_BATCH_CONFIG_VERSION("incub_batch_config_version", LPDatabase.IntegerNotNull())
        ,

        /**
         *
         */
        FLD_TYPE("type", LPDatabase.String())
        ,        

        /**
         *
         */
        FLD_INCUBATION_INCUBATOR("incubation_incubator", LPDatabase.String())        
        ,

        /**
         *
         */
        FLD_INCUBATION_START(FIELDS_NAMES_INCUBATION_START, dateTime())
        ,        

        /**
         *
         */
        FLD_INCUBATION_END(FIELDS_NAMES_INCUBATION_END, dateTime())        
        ,

        /**
         *
         */
        FLD_ACTIVE( LPDatabase.FIELDS_NAMES_ACTIVE, LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_COMPLETED("completed", LPDatabase.Boolean(false)),
        FLD_UNSTRUCT_CONTENT("unstruct_content", LPDatabase.String()),
        FLD_STRUCT_NUM_ROWS("struct_num_rows", LPDatabase.Integer()),
        FLD_STRUCT_NUM_COLS("struct_num_cols", LPDatabase.Integer()),
        FLD_STRUCT_TOTAL_POSITIONS("struct_total_positions", LPDatabase.Integer()),
        FLD_STRUCT_TOTAL_OBJECTS("struct_total_objects", LPDatabase.Integer()),
        FLD_STRUCT_CONTENT("struct_content", "character varying[] COLLATE pg_catalog.\"default\""),
        FLD_STRUCT_ROWS_NAME("struct_rows_name", "character varying[] COLLATE pg_catalog.\"default\""),
        FLD_STRUCT_COLS_NAME("struct_cols_name", "character varying[] COLLATE pg_catalog.\"default\""),        
        // ...
        ;
        
        private IncubBatch(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix,String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix,String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = IncubBatch.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (IncubBatch obj: IncubBatch.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_DATA);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, tableTag, tblObj[0]);                        
                        fieldsScript.append(currField[0]).append(" ").append(currFieldDefBuilder);
                        tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#"+obj.name(), currField[0]);
                }
            }
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, fieldsTag, fieldsScript.toString());
            return tblCreateScript.toString();
        }    
        public static String[] getAllFieldNames(){
            String[] tableFields=new String[0];
            for (IncubBatch obj: IncubBatch.values()){
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
    public enum ViewProgramScheduledLocations{
        /**
         *
         */
        TBL("pr_scheduled_locations",  LPDatabase.CreateView() +
                " select  dpr.sample_config_code, dpr.sample_config_code_version, "+
                "         cnfpcd.*, dpl.area, dpl.spec_code, dpl.spec_variation_name, dpl.spec_analysis_variation, dpl.spec_code_version, dpl.requires_person_ana, dpl.person_ana_definition "+
                "   from #SCHEMA_CONFIG.program_calendar_date  cnfpcd"+
                "  inner join #SCHEMA_DATA.program  dpr on dpr.name=cnfpcd.program_id "+
                "  inner join #SCHEMA_DATA.program_location dpl on dpl.program_name=cnfpcd.program_id and dpl.location_name=cnfpcd.location_name;"+
                "ALTER VIEW  #SCHEMA.#TBL  OWNER TO #OWNER;")
        ,

        /**
         *
         */
        FLD_SAMPLE_CONFIG_CODE("sample_config_code", "dpr.sample_config_code"),
        FLD_SAMPLE_CONFIG_CODE_VERSION("sample_config_code_version", "dpr.sample_config_code_version"),
        FLD_PROGRAM_NAME("program_name", "cnfpcd.program_id as program_name"),
        FLD_PROGRAM_DAY_ID("program_day_id", "cnfpcd.id as program_day_id"),
        FLD_PROGRAM_DAY_DATE("program_day_date", "cnfpcd.date as program_day_date"),
        FLD_AREA("area", "dpl.area"),
        FLD_SPEC_CODE("spec_code", "dpl.spec_code"),
        FLD_SPEC_CODE_VERSION("spec_code_version", "dpl.spec_code_version"),
        FLD_SPEC_VARIATION_NAME("spec_variation_name", "dpl.spec_variation_name"),
        FLD_SPEC_ANALYSIS_VARIATION("spec_analysis_variation", "dpl.spec_analysis_variation"),
        FLD_REQUIRES_PERSON_ANA("requires_person_ana", "dpl.requires_person_ana"),
        FLD_PERSON_ANA_DEFINITION("person_ana_definition", "dpl.person_ana_definition"),
        FLD_LOCATION_NAME("location_name", "cnfpcd.location_name"),
        FLD_ID("id", "cnfpcd.id"),
        FLD_PROGRAM_ID("program_id", "cnfpcd.program_id"),
        FLD_DATE("date", "cnfpcd.date"),
        ;
        private ViewProgramScheduledLocations(String dbObjName, String dbObjType){
            this.dbObjName=dbObjName;
            this.dbObjTypePostgres=dbObjType;
        }

        /**
         *
         * @return entry name
         */
        public String getName(){
            return this.dbObjName;
        }
        private String[] getDbFieldDefinitionPostgres(){
            return new String[]{this.dbObjName, this.dbObjTypePostgres};
        }

        /**
         *
         * @param schemaNamePrefix procedure prefix
         * @param fields fields , ALL when this is null
         * @return One Create-Table script for this given table, for this given procedure and for ALL or the given fields.
         */
        public static String createTableScript(String schemaNamePrefix, String[] fields){
            return createTableScriptPostgres(schemaNamePrefix, fields);
        }
        private static String createTableScriptPostgres(String schemaNamePrefix, String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = ViewProgramScheduledLocations.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_CONFIG", LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_CONFIG));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, "#SCHEMA_DATA", LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ViewProgramScheduledLocations obj: ViewProgramScheduledLocations.values()){
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
        private final String dbObjName;             
        private final String dbObjTypePostgres;                     
    }        

}
