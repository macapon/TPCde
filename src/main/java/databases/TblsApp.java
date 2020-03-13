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

/**
 *
 * @author Administrator
 */
public class TblsApp {

    /**
     *
     */
    public enum AppSession{

        /**
         *
         */
        FLD_SESSION_ID("session_id", "integer NOT NULL DEFAULT nextval('#SCHEMA.#TBL_session_id_seq'::regclass)")
        ,        TBL("app_session", LPDatabase.createSequence(FLD_SESSION_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_SESSION_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.createTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_SESSION_ID) )" +
                LPDatabase.POSTGRESQL_OIDS+" TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_DATE_STARTED("date_started", LPDatabase.date())
        ,

        /**
         *
         */
        FLD_PERSON("person", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_ROLE_NAME("role_name", LPDatabase.string());
        
        private AppSession(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = AppSession.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (AppSession obj: AppSession.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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
    public enum UserProcess{ // 'the sequence is session_id integer NOT NULL DEFAULT nextval('app.app_session_session_id_seq1'::regclass)'

        /**
         *
         */
        TBL("user_process",  LPDatabase.createTable() + " (#FLDS , CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_USER_NAME, #FLD_PROC_NAME) ) "+
                LPDatabase.POSTGRESQL_OIDS +  LPDatabase.createTableSpace() + "ALTER TABLE #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_USER_NAME("user_name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_PROC_NAME("proc_name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_ACTIVE("active", LPDatabase.booleanFld());
        private UserProcess(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = UserProcess.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (UserProcess obj: UserProcess.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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
    public enum Users{

        /**
         *
         */
        TBL("users",  LPDatabase.createTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_USER_NAME) ) "
                + LPDatabase.POSTGRESQL_OIDS +  LPDatabase.createTableSpace() + "ALTER TABLE #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")    
        ,

        /**
         *
         */
        FLD_USER_NAME("user_name", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_EMAIL("email", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_ESIGN("e_sign", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_PASSWORD("password", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_PERSON_NAME("person_name", LPDatabase.string()),
        /**
         *
         */
        FLD_TABS_ON_LOGIN("tabs_on_login", LPDatabase.string())
        
        
        //, FLD_PROCEDURES("procedures", "character varying[] COLLATE pg_catalog.\"default\"")
        ;
        private Users(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = Users.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (Users obj: Users.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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
    public enum HolidaysCalendar{

        /**
         *
         */
        TBL("holidays_calendar",  LPDatabase.createTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_CODE) ) "
                + LPDatabase.POSTGRESQL_OIDS +  LPDatabase.createTableSpace() + "ALTER TABLE #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")    
        ,

        /**
         *
         */
        FLD_CODE("code", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_ACTIVE("active", LPDatabase.booleanFld())
        ,

        /**
         *
         */
        FLD_DESCRIPTION("description", LPDatabase.string())
        //, FLD_ESIGN("e_sign", LPDatabase.String())
        //, FLD_PASSWORD("password", LPDatabase.String())
        //, FLD_PERSON_NAME("person_name", LPDatabase.String())
        
        ;
        private HolidaysCalendar(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = HolidaysCalendar.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (HolidaysCalendar obj: HolidaysCalendar.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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
    public enum HolidaysCalendarDate{

        /**
         *
         */
        FLD_ID("id", "bigint NOT NULL DEFAULT nextval(' #SCHEMA.#TBL_id_seq'::regclass)"),
        TBL("holidays_calendar_date", LPDatabase.createSequence(FLD_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.createTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_ID) ) "
                + LPDatabase.POSTGRESQL_OIDS+ LPDatabase.createTableSpace() + "ALTER TABLE #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")    
        ,

        /**
         *
         */


        /**
         *
         */
        FLD_CALENDAR_CODE("calendar_code", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_DATE("date", LPDatabase.date())
        ,

        /**
         *
         */
        FLD_DAY_NAME("day_name", LPDatabase.string())
        
        ;
        private HolidaysCalendarDate(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = HolidaysCalendarDate.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (HolidaysCalendarDate obj: HolidaysCalendarDate.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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

    public enum Incident{
        /**
         *
         */
        FLD_ID("id", "bigint NOT NULL DEFAULT nextval(' #SCHEMA.#TBL_#FLD_ID_seq'::regclass)"),
        TBL("incident", LPDatabase.createSequence(FLD_ID.getName())
                + "ALTER SEQUENCE #SCHEMA.#TBL_#FLD_ID_seq OWNER TO #OWNER;"
                +  LPDatabase.createTable() + " (#FLDS ,  CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_ID) ) "
                + LPDatabase.POSTGRESQL_OIDS+" "+LPDatabase.createTableSpace() + "ALTER TABLE #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")    
        ,
        /**
         *
         */
        FLD_DATE_CREATION("date_creation", LPDatabase.dateTime()),
        FLD_PERSON_CREATION("person_creation", LPDatabase.string()),
        FLD_DATE_CONFIRMED("date_confirmed", LPDatabase.dateTime()),
        FLD_PERSON_CONFIRMED("person_confirmed", LPDatabase.string()),
        FLD_DATE_RESOLUTION("date_resolution", LPDatabase.dateTime()),
        FLD_PERSON_RESOLUTION("person_resolution", LPDatabase.string()),
        FLD_DATE_LAST_UPDATE("date_last_update", LPDatabase.dateTime()),
        FLD_PERSON_LAST_UPDATE("person_last_update", LPDatabase.string()),        /**
         *
         */
        FLD_STATUS("status", LPDatabase.string()),        
        FLD_STATUS_PREVIOUS("status_previous", LPDatabase.string()),        
        /**
         *
         */
        FLD_USER_NAME("user_name", LPDatabase.string()),
        FLD_PERSON_NAME("person_name", LPDatabase.string()),
        /**
         *
         */
        FLD_USER_ROLE("user_role", LPDatabase.string()),
        /**
         *
         */
        FLD_TITLE("item_title", LPDatabase.string()),
        FLD_DETAIL("item_detail", LPDatabase.string()),
        FLD_SESSION_INFO("session_info", LPDatabase.string()),
        ;
        private Incident(String dbObjName, String dbObjType){
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
         * @param fields
         * @return
         */
        public static String createTableScript(String[] fields){
            return createTableScriptPostgres(fields);
        }
        private static String createTableScriptPostgres(String[] fields){
            StringBuilder tblCreateScript=new StringBuilder();
            String[] tblObj = Incident.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.SCHEMA_APP);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (Incident obj: Incident.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        StringBuilder currFieldDefBuilder = new StringBuilder(currField[1]);
                        currFieldDefBuilder=LPPlatform.replaceStringBuilderByStringAllReferences(currFieldDefBuilder, schemaTag, LPPlatform.SCHEMA_APP);
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
            for (Incident obj: Incident.values()){
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
