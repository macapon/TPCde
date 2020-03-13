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
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class TblsEnvMonitDataAudit {

    /**
     *
     */
    public enum IncubBatch{

        /**
         *
         */
        FLD_AUDIT_ID("audit_id", "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_audit_id_seq'::regclass)")
        ,        TBL("incub_batch", LPDatabase.CreateSequence(FLD_AUDIT_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_AUDIT_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT app_session_pkey PRIMARY KEY (#FLD_AUDIT_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+" TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + "    OWNER to #OWNER;")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_TABLE_NAME("table_name", " character varying COLLATE pg_catalog.\"default\"")
        ,

        /**
         *
         */
        FLD_TRANSACTION_ID("transaction_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_TABLE_ID("table_id", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_DATE("date", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_PERSON("person", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ACTION_NAME("action_name", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_FIELDS_UPDATED("fields_updated", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_BATCH_NAME("batch", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_USER_ROLE("user_role", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_PROCEDURE("procedure", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_PROCEDURE_VERSION("procedure_version", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_APP_SESSION_ID("app_session_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_PICTURE_BEFORE("picture_before", "json")
        ,

        /**
         *
         */
        FLD_PICTURE_AFTER("picture_after", "json")
        ,

        /**
         *
         */
        FLD_REVIEWED("reviewed", LPDatabase.Boolean(false))
        ,

        /**
         *
         */
        FLD_REVIEWED_BY("reviewed_by", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_REVIEWED_ON("reviewed_on", LPDatabase.dateTime())
        ,

        /**
         *
         */
        FLD_REVISION_NOTE("revision_note", LPDatabase.String())
        ,        

        /**
         *
         */
        FLD_PARENT_AUDIT_ID("parent_audit_id", LPDatabase.Integer())        
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
            String[] tblObj = IncubBatch.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA_AUDIT));
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
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA_AUDIT));
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
    
}