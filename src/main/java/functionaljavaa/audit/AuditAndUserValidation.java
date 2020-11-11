/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functionaljavaa.audit;

import com.labplanet.servicios.app.GlobalAPIsParams;
import databases.Token;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lbplanet.utilities.LPArray;
import lbplanet.utilities.LPFrontEnd;
import lbplanet.utilities.LPHttp;
import lbplanet.utilities.LPPlatform;

    

/**
 *
 * @author User
 */
public class AuditAndUserValidation {

    /**
     * @return the auditReasonPhrase
     */
    public String getAuditReasonPhrase() {
        return auditReasonPhrase;
    }

    /**
     * @return the checkUserValidationPassesDiag
     */
    public Object[] getCheckUserValidationPassesDiag() {
        return checkUserValidationPassesDiag;
    }
    
    private String auditReasonPhrase="";
    private Object[] checkUserValidationPassesDiag;
    
    public AuditAndUserValidation(HttpServletRequest request, HttpServletResponse response, String language){
        String[] mandatoryParams = new String[]{};
        String schemaPrefix = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_SCHEMA_PREFIX);            
        String actionName = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_ACTION_NAME);
        String finalToken = request.getParameter(GlobalAPIsParams.REQUEST_PARAM_FINAL_TOKEN);                   
        
        Token token = new Token(finalToken);
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(token.getUserName())){
            this.checkUserValidationPassesDiag=LPPlatform.trapMessage(LPPlatform.LAB_FALSE, LPPlatform.API_ERRORTRAPING_INVALID_TOKEN, null);                             
            return;
        }        
        Object[] procActionRequiresUserConfirmation = LPPlatform.procActionRequiresUserConfirmation(schemaPrefix, actionName);
        if (procActionRequiresUserConfirmation[0].toString().contains(LPPlatform.LAB_TRUE)){     
            if (!procActionRequiresUserConfirmation[0].toString().equalsIgnoreCase(LPPlatform.LAB_TRUE))
                mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_AUDIT_REASON_PHRASE);                
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_USER_TO_CHECK);    
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_PSWD_TO_CHECK);    
        }
        Object[] procActionRequiresEsignConfirmation = LPPlatform.procActionRequiresEsignConfirmation(schemaPrefix, actionName);
        if (procActionRequiresEsignConfirmation[0].toString().contains(LPPlatform.LAB_TRUE)){      
            if (!procActionRequiresEsignConfirmation[0].toString().equalsIgnoreCase(LPPlatform.LAB_TRUE))
                mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_AUDIT_REASON_PHRASE);                
            mandatoryParams = LPArray.addValueToArray1D(mandatoryParams, GlobalAPIsParams.REQUEST_PARAM_ESIGN_TO_CHECK);    
        }        
        
        Object[] areMandatoryParamsInResponse = LPHttp.areAPIMandatoryParamsInApiRequest(request, mandatoryParams);                       
        if (LPPlatform.LAB_FALSE.equalsIgnoreCase(areMandatoryParamsInResponse[0].toString())){
            this.checkUserValidationPassesDiag= LPPlatform.trapMessage(LPPlatform.LAB_FALSE, LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()});                             
            return;
        }

        if (LPArray.valueInArray(mandatoryParams , GlobalAPIsParams.REQUEST_PARAM_AUDIT_REASON_PHRASE))
            this.auditReasonPhrase=request.getParameter(GlobalAPIsParams.REQUEST_PARAM_AUDIT_REASON_PHRASE); 

        if ( (procActionRequiresUserConfirmation[0].toString().contains(LPPlatform.LAB_TRUE)) &&     
             (!LPFrontEnd.servletUserToVerify(request, response, token.getUserName(), token.getUsrPw())) ){
            this.checkUserValidationPassesDiag= LPPlatform.trapMessage(LPPlatform.LAB_FALSE, LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()});                             
            return;            
        }
        
        if ( (procActionRequiresEsignConfirmation[0].toString().contains(LPPlatform.LAB_TRUE)) &&    
             (!LPFrontEnd.servletEsignToVerify(request, response, token.geteSign())) ){
            this.checkUserValidationPassesDiag= LPPlatform.trapMessage(LPPlatform.LAB_FALSE, LPPlatform.API_ERRORTRAPING_MANDATORY_PARAMS_MISSING, new Object[]{areMandatoryParamsInResponse[1].toString()});                             
            return;
        }
    
        this.checkUserValidationPassesDiag= LPPlatform.trapMessage(LPPlatform.LAB_TRUE, "", null);
    }
}
