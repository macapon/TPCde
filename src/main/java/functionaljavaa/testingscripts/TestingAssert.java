/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.testingscripts;

import lbplanet.utilities.LPArray;
import static lbplanet.utilities.LPPlatform.TRAP_MESSAGE_CODE_POSIC;
import static lbplanet.utilities.LPPlatform.TRAP_MESSAGE_EVALUATION_POSIC;
import static lbplanet.utilities.LPPlatform.TRAP_MESSAGE_MESSAGE_POSIC;

/**
 *
 * @author Administrator
 */
public class TestingAssert {
    
    String evalBoolean="";
    String errorCode="";    
    
    /**
     *
     * @param line
     * @param numArgs
     */
    public TestingAssert(Object[] line, Integer numArgs){
        switch (numArgs.toString()){                    
            case "1":
                this.evalBoolean=(String) line[0];
                break;
            case "2":
                this.evalBoolean=(String) line[0];
                this.errorCode=(String) line[1];
                break;
            default:                
        }        
    }    
    
    /**
     *
     * @param numEvaluationArguments
     * @param tstAssertSummary
     * @param diagnoses
     * @return
     */
    public Object[] evaluate(Integer numEvaluationArguments, TestingAssertSummary tstAssertSummary, Object[] diagnoses){
        String sintaxisIcon = ""; 
        String codeIcon = "";
        if (numEvaluationArguments>=1){
            if ( (this.evalBoolean==null) || (this.evalBoolean.length()==0) ||("".equals(this.evalBoolean)) ){
                tstAssertSummary.increasetotalLabPlanetBooleanUndefined();sintaxisIcon=LPTestingOutFormat.TST_BOOLEANUNDEFINED;
            }else{
                if (this.evalBoolean.equalsIgnoreCase(diagnoses[0].toString())){
                    tstAssertSummary.increasetotalLabPlanetBooleanMatch(); sintaxisIcon=LPTestingOutFormat.TST_BOOLEANMATCH;
                }else{tstAssertSummary.increasetotalLabPlanetBooleanUnMatch(); sintaxisIcon=LPTestingOutFormat.TST_BOOLEANUNMATCH;}
            }
        }else{
            tstAssertSummary.increasetotalLabPlanetBooleanUndefined();sintaxisIcon=LPTestingOutFormat.TST_BOOLEANUNDEFINED;            
        }
        if (numEvaluationArguments>=2){
            if ( (this.errorCode==null) || (this.errorCode.length()==0) ||("".equals(this.errorCode)) ){
                tstAssertSummary.increasetotalLabPlanetErrorCodeUndefined();codeIcon=LPTestingOutFormat.TST_ERRORCODEUNDEFINED;
            }else{
                if (this.errorCode.equalsIgnoreCase(diagnoses[4].toString())){
                    tstAssertSummary.increasetotalLabPlanetErrorCodeMatch(); codeIcon=LPTestingOutFormat.TST_ERRORCODEMATCH;
                }else{tstAssertSummary.increasetotalLabPlanetErrorCodeUnMatch(); codeIcon=LPTestingOutFormat.TST_ERRORCODEUNMATCH;}
            }    
        }else{
            tstAssertSummary.increasetotalLabPlanetErrorCodeUndefined();codeIcon=LPTestingOutFormat.TST_ERRORCODEUNDEFINED;            
        }
        Object[] diagnostic=new Object[] {sintaxisIcon + " ("+this.evalBoolean+") "};
        String message="";
        if (diagnoses.length>TRAP_MESSAGE_EVALUATION_POSIC) message=message+"Syntaxis:"+diagnoses[TRAP_MESSAGE_EVALUATION_POSIC]+". ";
        if (numEvaluationArguments>=2){
            if (diagnoses.length>TRAP_MESSAGE_CODE_POSIC) message=message+"Code:"+diagnoses[TRAP_MESSAGE_CODE_POSIC]+". ";
            diagnostic=LPArray.addValueToArray1D(diagnostic, codeIcon + "<h8>("+this.errorCode+")</h8> ");
        }
        if (diagnoses.length>TRAP_MESSAGE_MESSAGE_POSIC) message=message+"Message:"+diagnoses[TRAP_MESSAGE_MESSAGE_POSIC]+". ";  
        diagnostic=LPArray.addValueToArray1D(diagnostic, message);
        return diagnostic;
}
    
}
