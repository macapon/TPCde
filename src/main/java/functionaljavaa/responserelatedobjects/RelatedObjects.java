/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.responserelatedobjects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author User
 */
public class RelatedObjects {
    
    public enum RelatedObjectsElementNames{
        SCHEMA("schema_name"), TABLE("table_name"), OBJECT_TYPE("object_type"), OBJECT("object_name");
        private RelatedObjectsElementNames(String labelName){
            this.labelName=labelName;            
        }    
        public String getLabelName(){
            return this.labelName;
        }           
        private final String labelName;
    }
    
    private static RelatedObjects mainStructureObject;
    private final JSONArray jArrMainObject;
    
    private RelatedObjects(){
        this.jArrMainObject=new JSONArray();
    }
    
    public static RelatedObjects getInstance() {
        if (mainStructureObject == null) {
            mainStructureObject = new RelatedObjects();
            return mainStructureObject;
        } else {
         return mainStructureObject;
        }                 
    }
    public void killInstance(){   
        mainStructureObject=null;        
    }
    public JSONArray getRelatedObject(){
        return jArrMainObject;
    }   
    public static void addSimpleNode(String schema, String table, String objectType, Object object){
        JSONObject jObj=new JSONObject();
        jObj.put(RelatedObjectsElementNames.SCHEMA.getLabelName(), schema);
        jObj.put(RelatedObjectsElementNames.TABLE.getLabelName(),  table);
        jObj.put(RelatedObjectsElementNames.OBJECT_TYPE.getLabelName(),  objectType);
        jObj.put(RelatedObjectsElementNames.OBJECT.getLabelName(),  object);        
        mainStructureObject.jArrMainObject.add(jObj);
    }
    
}
