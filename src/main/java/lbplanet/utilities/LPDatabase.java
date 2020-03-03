/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbplanet.utilities;

/**
 *
 * @author Administrator
 */
public class LPDatabase {
    private LPDatabase() {    throw new IllegalStateException("Utility class");  }
        
    
    public static final String Boolean(){ return "boolean";}    
    public static final String Boolean(Boolean def){ return LPDatabase.Boolean() + " default " + def.toString();}
    public static final String BooleanNotNull(Boolean def){ return LPDatabase.Boolean() + " " + NotNulllClause();}
    public static final String Integer(){ return "integer";}
    public static final String IntegerNotNull(){ return Integer()+" "+NotNulllClause();}
    public static final String Real(){ return "real";}
    public static final String String(){ return "character varying COLLATE pg_catalog.\"default\"";}
    public static final String StringNotNull(){ return "character varying COLLATE pg_catalog.\"default\" "+NotNulllClause();}
    public static final String StringArr(){ return "character varying[] COLLATE pg_catalog.\"default\"";}
    public static final String StringArrNotNull(){ return "character varying[] COLLATE pg_catalog.\"default\" "+NotNulllClause();}
    public static final String String(Integer size){ return "character varying("+size.toString()+") COLLATE pg_catalog.\"default\"";}
    public static final String StringNotNull(Integer size){ return "character varying("+size.toString()+") COLLATE pg_catalog.\"default\" "+NotNulllClause();}
    public static final String Date(){ return "date";}
    public static final String dateTime(){ return "timestamp(4) without time zone";}
    public static final String dateTimeWithDefaultNow(){ return "timestamp default now()";}
    
    private static final String NotNulllClause(){return " not null";}
    
    public static final String POSTGRESQL_TABLE_OWNERSHIP="    OWNER to #OWNER";
    public static final String POSTGRESQL_OIDS="WITH ( OIDS = FALSE ) ";
    
    public static final String CreateView(){return "CREATE OR REPLACE VIEW #SCHEMA.#TBL AS ";}
    public static final String CreateTable(){return "CREATE TABLE #SCHEMA.#TBL";}
    public static final String CreateTableSpace(){return "TABLESPACE #TABLESPACE;";}
    public static final String CreateSequence(String fieldName){return "CREATE SEQUENCE #SCHEMA.#TBL_"+fieldName+"_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1; ";}
    
    public static final String FIELDS_NAMES_ACTIVE="active";
    public static final String FIELDS_NAMES_CREATED_BY="created_by";
    public static final String FIELDS_NAMES_CREATED_ON="created_on";
    public static final String FIELDS_NAMES_VOLUME="volume";
    public static final String FIELDS_NAMES_VOLUME_UOM="volume_uom";
    public static final String FIELDS_NAMES_METHOD_NAME="method_name";
    public static final String FIELDS_NAMES_METHOD_VERSION="method_version";
    public static final String FIELDS_NAMES_PROCEDURE_NAME="procedure_name";
    public static final String FIELDS_NAMES_PROCEDURE_VERSION="procedure_version";
    public static final String FIELDS_NAMES_SAMPLE_ID="sample_id";
    public static final String FIELDS_NAMES_DESCRIPTION="description";
    
    
}
