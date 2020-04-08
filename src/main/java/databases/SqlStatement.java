/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import lbplanet.utilities.LPArray;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class SqlStatement {
    enum WHERECLAUSE_TYPES{NULL("NULL"), IN("IN"), NOT_IN("NOT IN"), EQUAL("="), LIKE("LIKE"), BETWEEN("BETWEEN"),
        LESS_THAN_STRICT("<"), LESS_THAN("<="), GREATER_THAN_STRICT(">"), GREATER_THAN(">=");
        private final String clause;
        WHERECLAUSE_TYPES(String cl){this.clause=cl;}
        public String getSqlClause(){
            return clause;
        }
    }
    /**
     *
     * @param operation
     * @param schemaName
     * @param tableName
     * @param whereFieldNames
     * @param whereFieldValues
     * @param fieldsToRetrieve
     * @param setFieldNames
     * @param setFieldValues
     * @param fieldsToOrder
     * @param fieldsToGroup
     * @return
     */
    public HashMap<String, Object[]> buildSqlStatement(String operation, String schemaName, String tableName, String[] whereFieldNames, Object[] whereFieldValues, String[] fieldsToRetrieve, String[] setFieldNames, Object[] setFieldValues, String[] fieldsToOrder, String[] fieldsToGroup) {        
       return buildSqlStatement(operation, schemaName, tableName, whereFieldNames, whereFieldValues, fieldsToRetrieve, setFieldNames, setFieldValues, fieldsToOrder, fieldsToGroup, false);      
    }

    /**
     *
     * @param operation
     * @param schemaName
     * @param tableName
     * @param whereFieldNames
     * @param whereFieldValues
     * @param fieldsToRetrieve
     * @param setFieldNames
     * @param setFieldValues
     * @param fieldsToOrder
     * @param fieldsToGroup
     * @param forceDistinct
     * @return
     */
    public HashMap<String, Object[]> buildSqlStatement(String operation, String schemaName, String tableName, String[] whereFieldNames, Object[] whereFieldValues, String[] fieldsToRetrieve, String[] setFieldNames, Object[] setFieldValues, String[] fieldsToOrder, String[] fieldsToGroup, Boolean forceDistinct) {        
        HashMap<String, Object[]> hm = new HashMap();        
        
        String queryWhere = "";
        schemaName = setSchemaName(schemaName);
        tableName = setSchemaName(tableName);
        
        Object[] whereFieldValuesNew = new Object[0];
        if (whereFieldNames != null) {
            Object[] whereClauseContent = buildWhereClause(whereFieldNames, whereFieldValues);            
            queryWhere=(String) whereClauseContent[0];
            whereFieldValuesNew=(Object[]) whereClauseContent[1];
        }
        String fieldsToRetrieveStr = buildFieldsToRetrieve(fieldsToRetrieve);
        String fieldsToOrderStr = buildOrderBy(fieldsToOrder);
        String fieldsToGroupStr = buildGroupBy(fieldsToGroup);
        
        String insertFieldNamesStr = buildInsertFieldNames(setFieldNames);
        String insertFieldValuesStr = buildInsertFieldNamesValues(setFieldNames);
        
        String query = "";
        switch (operation.toUpperCase()) {
            case "SELECT":
                query = "select ";
                if (forceDistinct){query=query+ " distinct ";}
                query=query+ " " + fieldsToRetrieveStr + " from " + schemaName + "." + tableName + "   where " + queryWhere + " " + fieldsToGroupStr + " " + fieldsToOrderStr;
                break;
            case "INSERT":
                query = "insert into " + schemaName + "." + tableName + " (" + insertFieldNamesStr + ") values ( " + insertFieldValuesStr + ") ";
                break;
            case "UPDATE":
                String updateSetSectionStr=buildUpdateSetFields(setFieldNames);
                query = "update " + schemaName + "." + tableName + " set " + updateSetSectionStr + " where " + queryWhere;
                whereFieldValuesNew= LPArray.addValueToArray1D(setFieldValues, whereFieldValuesNew);
                break;
            default:
                break;
        }
        hm.put(query, whereFieldValuesNew);
        return hm;
    }

    public HashMap<String, Object[]> buildSqlStatementCounter(String schemaName, String tableName, String[] whereFieldNames, Object[] whereFieldValues, String[] fieldsToGroup, String[] fieldsToOrder) {        
        HashMap<String, Object[]> hm = new HashMap();        
        
        String queryWhere = "";
        schemaName = setSchemaName(schemaName);
        tableName = setSchemaName(tableName);
        
        Object[] whereFieldValuesNew = new Object[0];
        if (whereFieldNames != null) {
            Object[] whereClauseContent = buildWhereClause(whereFieldNames, whereFieldValues);            
            queryWhere=(String) whereClauseContent[0];
            whereFieldValuesNew=(Object[]) whereClauseContent[1];
        }
        String fieldsToOrderStr = buildOrderBy(fieldsToOrder);
        String fieldsToGroupStr = buildGroupBy(fieldsToGroup);
        
        String query = "select ";
        query=query+ " " + fieldsToGroupStr.replace("Group By", "") + ", count(*) as COUNTER from " + schemaName + "." + tableName + "   where " + queryWhere + " " + fieldsToGroupStr + " " + fieldsToOrderStr;
        hm.put(query, whereFieldValuesNew);
        return hm;
    }
    
    private Object[] buildWhereClause(String[] whereFieldNames, Object[] whereFieldValues){
        StringBuilder queryWhere = new StringBuilder(0);
        Object[] whereFieldValuesNew = new Object[0];
        for (int iwhereFieldNames=0; iwhereFieldNames<whereFieldNames.length; iwhereFieldNames++){
            String fn = whereFieldNames[iwhereFieldNames];
            if (iwhereFieldNames > 0) {
                queryWhere.append(" and ");
            }
            if (fn.toUpperCase().contains(WHERECLAUSE_TYPES.NULL.getSqlClause())) {
                queryWhere.append(fn);
            } else if (fn.toUpperCase().contains(" "+WHERECLAUSE_TYPES.LIKE.getSqlClause())) {
                queryWhere.append(" ? ");
                whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, whereFieldValues[iwhereFieldNames]);
            } else if (fn.toUpperCase().contains(WHERECLAUSE_TYPES.NOT_IN.getSqlClause())) {
                String separator = inNotInSeparator(fn);
                String textSpecs = (String) whereFieldValues[iwhereFieldNames];
                String[] textSpecArray = textSpecs.split("\\" + separator);
                Integer posicINClause = fn.toUpperCase().indexOf(WHERECLAUSE_TYPES.NOT_IN.getSqlClause());
                queryWhere.append(fn.substring(0, posicINClause + WHERECLAUSE_TYPES.NOT_IN.getSqlClause().length())).append(" (");
                for (String f : textSpecArray) {
                    queryWhere.append("?,");
                    whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, f);
                }
                queryWhere.deleteCharAt(queryWhere.length() - 1);
                queryWhere.append(")");
            } else if (fn.toUpperCase().contains(" "+WHERECLAUSE_TYPES.IN.getSqlClause())) {
                String separator = inNotInSeparator(fn);
                String textSpecs = (String) whereFieldValues[iwhereFieldNames];
                String[] textSpecArray = textSpecs.split("\\" + separator);
                Integer posicINClause = fn.toUpperCase().indexOf(WHERECLAUSE_TYPES.IN.getSqlClause());
                queryWhere.append(fn.substring(0, posicINClause + WHERECLAUSE_TYPES.IN.getSqlClause().length())).append(" (");
                for (String f : textSpecArray) {
                    queryWhere.append("?,");
                    whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, f);
                }
                queryWhere.deleteCharAt(queryWhere.length() - 1);
                queryWhere.append(")");
            } else if (fn.toUpperCase().contains(WHERECLAUSE_TYPES.BETWEEN.getSqlClause())) {
                queryWhere.append(fn.toLowerCase()).append(" ? ").append(" and ").append(" ? ");
                whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, whereFieldValues[iwhereFieldNames]);
                whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, whereFieldValues[iwhereFieldNames+1]);
            } else if ( (fn.toUpperCase().contains(WHERECLAUSE_TYPES.LESS_THAN.getSqlClause())) ||
                (fn.toUpperCase().contains(WHERECLAUSE_TYPES.LESS_THAN_STRICT.getSqlClause())) ||
                (fn.toUpperCase().contains(WHERECLAUSE_TYPES.GREATER_THAN.getSqlClause())) || 
                (fn.toUpperCase().contains(WHERECLAUSE_TYPES.GREATER_THAN_STRICT.getSqlClause()))) {
                queryWhere.append(fn).append(" ? ");
                whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, whereFieldValues[iwhereFieldNames]);
            } else {
                queryWhere.append(fn).append("=? ");
                whereFieldValuesNew = LPArray.addValueToArray1D(whereFieldValuesNew, whereFieldValues[iwhereFieldNames]);
            }
        }
        return new Object[]{queryWhere.toString(), whereFieldValuesNew};
    }
    private String  buildUpdateSetFields(String[] setFieldNames) {
        StringBuilder updateSetSectionStr = new StringBuilder(0);
        for (String setFieldName : setFieldNames) {
            updateSetSectionStr.append(setFieldName).append("=?, ");
        }
        updateSetSectionStr.deleteCharAt(updateSetSectionStr.length() - 1);
        updateSetSectionStr.deleteCharAt(updateSetSectionStr.length() - 1);
        return updateSetSectionStr.toString();
    }

    private String buildInsertFieldNames(String[] setFieldNames) {
        StringBuilder setFieldNamesStr = new StringBuilder(0);
        if (setFieldNames != null) {
            for (String setFieldName: setFieldNames) {
                setFieldNamesStr.append(setFieldName).append(", ");
            }
            setFieldNamesStr.deleteCharAt(setFieldNamesStr.length() - 1);
            setFieldNamesStr.deleteCharAt(setFieldNamesStr.length() - 1);
        }
        return setFieldNamesStr.toString();
    }

    private String buildInsertFieldNamesValues(String[] setFieldNames) {
        StringBuilder setFieldNamesArgStr = new StringBuilder(0);
        if (setFieldNames != null) {
            for (String setFieldName: setFieldNames) {
                setFieldNamesArgStr.append("?, ");
            }
            setFieldNamesArgStr.deleteCharAt(setFieldNamesArgStr.length() - 1);
            setFieldNamesArgStr.deleteCharAt(setFieldNamesArgStr.length() - 1);
        }
        return setFieldNamesArgStr.toString();
    }
    
    private String setSchemaName(String schemaName) {
        schemaName = schemaName.replace("\"", "");
        schemaName = "\"" + schemaName + "\"";
        return schemaName;
    }

    private String buildFieldsToRetrieve(String[] fieldsToRetrieve) {
        StringBuilder fieldsToRetrieveStr = new StringBuilder(0);
        if (fieldsToRetrieve != null) {
            for (String fn : fieldsToRetrieve) {
                if (fn.toUpperCase().contains(" IN")) {
                    Integer posicINClause = fn.toUpperCase().indexOf("IN");
                    fn = fn.substring(0, posicINClause - 1);
                    fieldsToRetrieveStr.append(fn.toLowerCase()).append(", ");
                }
                fieldsToRetrieveStr.append(fn.toLowerCase()).append(", ");
            }
            fieldsToRetrieveStr.deleteCharAt(fieldsToRetrieveStr.length() - 1);
            fieldsToRetrieveStr.deleteCharAt(fieldsToRetrieveStr.length() - 1);
        }
        return fieldsToRetrieveStr.toString();
    }

    private String buildGroupBy(String[] fieldsToGroup) {
        StringBuilder fieldsToGroupStr = new StringBuilder(0);
        if (fieldsToGroup != null) {
            for (String fn : fieldsToGroup) {
                fieldsToGroupStr.append(fn).append(", ");
            }
            if (fieldsToGroupStr.length() > 0) {
                fieldsToGroupStr.deleteCharAt(fieldsToGroupStr.length() - 1);
                fieldsToGroupStr.deleteCharAt(fieldsToGroupStr.length() - 1);
                fieldsToGroupStr.insert(0, "Group By ");
            }
        }
        return fieldsToGroupStr.toString();
    }

    private String buildOrderBy(String[] fieldsToOrder) {
        StringBuilder fieldsToOrderBuilder = new StringBuilder(0);
        if (fieldsToOrder != null) {
            for (String fn : fieldsToOrder) {
                fieldsToOrderBuilder.append(fn).append(", ");
            }
            if (fieldsToOrderBuilder.length() > 0) {
                fieldsToOrderBuilder.deleteCharAt(fieldsToOrderBuilder.length() - 1);
                fieldsToOrderBuilder.deleteCharAt(fieldsToOrderBuilder.length() - 1);
                fieldsToOrderBuilder.insert(0, "Order By ");
            }
        }
        return fieldsToOrderBuilder.toString();
    }
    
    /**
     *
     * @param fn
     * @return
     */
    public String inNotInSeparator(String fn){
        Integer posicNOTINClause = fn.toUpperCase().indexOf("NOT IN");
        Integer posicINClause = fn.toUpperCase().indexOf("IN");
        String separator = fn;
        if (posicNOTINClause==-1){
            if (fn.length()<posicINClause + 3) return "|";
            separator = separator.substring(posicINClause + 2, posicINClause + 3);
            separator = separator.trim();
            separator = separator.replace(" IN", "");
        }else{
            if (fn.length()<posicNOTINClause + 7) return "|";
            separator = separator.substring(posicNOTINClause + 6, posicNOTINClause + 7);
            separator = separator.trim();
            separator = separator.replace("NOT IN", "");
        }
        if (separator.length() == 0) {
            separator = "|";
        }        
        return separator;
    }
    
}
