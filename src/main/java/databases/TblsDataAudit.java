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
import static lbplanet.utilities.LPDatabase.dateTime;
import lbplanet.utilities.LPEnums.CellHelper;
import lbplanet.utilities.LPEnums.Indexed;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author Administrator
 */
public class TblsDataAudit {

public enum MyEnum implements Indexed<MyEnum> {
    ONE,
    TWO,
    THREE,
    ;
}

public enum SomeEnumClass {
    ONE(1), TWO(2), THREE(3);
    SomeEnumClass(int n){}
    // This variable hosts your static data, along with shared behavior
    private static  final CellHelper<SomeEnumClass> helper = new CellHelper(SomeEnumClass.values(), SomeEnumClass.class);
    // Delegate the calls for shared functionality to the helper object
    public static SomeEnumClass getCell(int i) {return helper.getCell(i);}
}

public enum OtherEnumClass {
    Monday(1), Tuesday(2), Wednesday(3), Thrusday(4), Friday(5), Saturday(6), Sunday(7);
    OtherEnumClass(int n){}
    private static  final CellHelper<OtherEnumClass> helper = new CellHelper(OtherEnumClass.values(), OtherEnumClass.class);
    public static OtherEnumClass getCell(int i) {return helper.getCell(i);}
}
    /**
     *
     */
    public enum Session{

        /**
         *
         */
        FLD_SESSION_ID("session_id", "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_session_id_seq'::regclass)")
        ,
        TBL("session", LPDatabase.CreateSequence(FLD_SESSION_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_SESSION_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT app_session_pkey1 PRIMARY KEY (#FLD_SESSION_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+"  TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + "    OWNER to #OWNER;")
        ,

        /**
         *
         */

        /**
         *
         */
        FLD_PERSON("person", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_ROLE_NAME("role_name", LPDatabase.String())
        ,

        /**
         *
         */
        FLD_DATE_STARTED("date_started", dateTime())
        ,

        /**
         *
         */
        FLD_DATE_ENDED("date_ended", dateTime())
        ,

        /**
         *
         */
        FLD_USER_SESSION_ID("user_session_id", LPDatabase.Integer())
        ;
        private Session(String dbObjName, String dbObjType){
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
            String[] tblObj = Session.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA_AUDIT));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (Session obj: Session.values()){
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

    /**
     *
     */
    public enum Sample{

        /**
         *
         */
        FLD_AUDIT_ID("audit_id", "bigint NOT NULL DEFAULT nextval('#SCHEMA.#TBL_audit_id_seq'::regclass)")
        ,        
        TBL("sample", LPDatabase.CreateSequence(FLD_AUDIT_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_AUDIT_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.CreateTable() + " (#FLDS ,  CONSTRAINT app_session_pkey PRIMARY KEY (#FLD_AUDIT_ID) ) " +
                LPDatabase.POSTGRESQL_OIDS+"  TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + "    OWNER to #OWNER;")
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
        FLD_TABLE_ID("table_id", LPDatabase.Integer())
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
        FLD_SAMPLE_ID("sample_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_TEST_ID("test_id", LPDatabase.Integer())
        ,

        /**
         *
         */
        FLD_RESULT_ID("result_id", LPDatabase.Integer())
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
        FLD_ALIQUOT_ID("aliquot_id", LPDatabase.Integer())
        ,        

        /**
         *
         */
        FLD_SUBALIQUOT_ID("subaliquot_id", LPDatabase.Integer())        
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
        FLD_REVIEWED_ON("reviewed_on", dateTime())
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
        private Sample(String dbObjName, String dbObjType){
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
            String[] tblObj = Sample.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_DATA_AUDIT));
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