/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPDatabase;
import lbplanet.utilities.LPPlatform;

/**
 *
 * @author User
 */
public class TblsProcedure {

    /**
     *
     */
    public static final String schemaTag = "#SCHEMA";

    /**
     *
     */
    public static final String tableTag = "#TBL";

    /**
     *
     */
    public static final String ownerTag = "#OWNER";

    /**
     *
     */
    public static final String tablespaceTag = "#TABLESPACE";

    /**
     *
     */
    public static final String fieldsTag = "#FLDS";
    
    /**
     *
     */
    public enum PersonProfile{

        /**
         *
         */
        TBL("person_profile",  LPDatabase.createTable() + " (#FLDS , CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_PERSON_NAME, #FLD_ROLE_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+"  TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_PERSON_NAME("person_name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_ROLE_NAME("role_name", LPDatabase.stringNotNull()),
        FLD_ACTIVE("active", LPDatabase.booleanFld())
/*        , FLD_ANALYSIS("analysis", LPDatabase.StringNotNull())
         , FLD_METHOD_VERSION("method_version", LPDatabase.IntegerNotNull())
        , FLD_MANDATORY("mandatory", LPDatabase.Boolean())
        , FLD_PARAM_TYPE("param_type", LPDatabase.String())
        , FLD_NUM_REPLICAS("num_replicas", LPDatabase.Integer())
        , FLD_UOM("uom", LPDatabase.String())
        , FLD_UOM_CONVERSION_MODE("uom_conversion_mode", LPDatabase.String())*/
        ;
        private PersonProfile(String dbObjName, String dbObjType){
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
            String[] tblObj = PersonProfile.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_PROCEDURE));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (PersonProfile obj: PersonProfile.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        fieldsScript.append(currField[0]).append(" ").append(currField[1]);
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
    public enum ProcedureEvents{

        /**
         *
         */
        TBL("procedure_events",  LPDatabase.createTable() + " (#FLDS , CONSTRAINT #TBL_pkey1 PRIMARY KEY (#FLD_NAME, #FLD_ROLE_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+"  TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_NAME("name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_ROLE_NAME("role_name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_MODE("mode", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_TYPE("type", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_BRANCH_LEVEL("branch_level", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_LABEL_EN("label_en", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_LABEL_ES("label_es", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_ORDER_NUMBER("order_number", LPDatabase.integer())
        ,

        /**
         *
         */
        FLD_SOP("sop", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_ESIGN_REQUIRED("esign_required", LPDatabase.booleanFld())
        ,

        /**
         *
         */
        FLD_LP_FRONTEND_PAGE_NAME("lp_frontend_page_name", LPDatabase.string())
        ;
        private ProcedureEvents(String dbObjName, String dbObjType){
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
            String[] tblObj = ProcedureEvents.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_PROCEDURE));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ProcedureEvents obj: ProcedureEvents.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        fieldsScript.append(currField[0]).append(" ").append(currField[1]);
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
    public enum ProcedureInfo{

        /**
         *
         */
        TBL("procedure_info",  LPDatabase.createTable() + " (#FLDS , CONSTRAINT #TBL_pkey PRIMARY KEY (#FLD_NAME) )" +
                LPDatabase.POSTGRESQL_OIDS+"  TABLESPACE #TABLESPACE; ALTER TABLE  #SCHEMA.#TBL" + LPDatabase.POSTGRESQL_TABLE_OWNERSHIP+";")
        ,

        /**
         *
         */
        FLD_NAME("name", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_VERSION("version", LPDatabase.integerNotNull())
        ,

        /**
         *
         */
        FLD_SCHEMA_PREFIX("schema_prefix", LPDatabase.stringNotNull())
        ,

        /**
         *
         */
        FLD_LABEL_EN("label_en", LPDatabase.string())
        ,

        /**
         *
         */
        FLD_LABEL_ES("label_es", LPDatabase.string())
        ;
        private ProcedureInfo(String dbObjName, String dbObjType){
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

        /**
         *
         * @return
         */
        public String[] getDbFieldDefinitionPostgres(){
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
            String[] tblObj = ProcedureInfo.TBL.getDbFieldDefinitionPostgres();
            tblCreateScript.append(tblObj[1]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, schemaTag, LPPlatform.buildSchemaName(schemaNamePrefix, LPPlatform.SCHEMA_PROCEDURE));
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tableTag, tblObj[0]);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, ownerTag, DbObjects.POSTGRES_DB_OWNER);
            tblCreateScript=LPPlatform.replaceStringBuilderByStringAllReferences(tblCreateScript, tablespaceTag, DbObjects.POSTGRES_DB_TABLESPACE);            
            StringBuilder fieldsScript=new StringBuilder();
            for (ProcedureInfo obj: ProcedureInfo.values()){
                String[] currField = obj.getDbFieldDefinitionPostgres();
                String objName = obj.name();
                if ( (!"TBL".equalsIgnoreCase(objName)) && (fields!=null && (fields[0].length()==0 || (fields[0].length()>0 && LPArray.valueInArray(fields, currField[0]))) ) ){
                        if (fieldsScript.length()>0)fieldsScript.append(", ");
                        fieldsScript.append(currField[0]).append(" ").append(currField[1]);
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
