/**************************************************************************
 * copyright file="UpdateInboxRulesException.java" company="Microsoft"
 *     Copyright (c) Microsoft Corporation.  All rights reserved.
 * 
 * Defines the UpdateInboxRulesException.java.
 **************************************************************************/
package microsoft.exchange.webservices.data.exceptions;

import microsoft.exchange.webservices.data.RuleOperation;
import microsoft.exchange.webservices.data.RuleOperationError;
import microsoft.exchange.webservices.data.RuleOperationErrorCollection;
import microsoft.exchange.webservices.data.ServiceError;
import microsoft.exchange.webservices.data.ServiceResponse;
import microsoft.exchange.webservices.data.UpdateInboxRulesResponse;

/**
 * Represents an exception thrown when an error occurs as a result of calling 
 * the UpdateInboxRules operation.
 */
public class UpdateInboxRulesException extends ServiceRemoteException {
	
	private static final long serialVersionUID = 3444934494648832535L;

	/**
	 * ServiceResponse when service operation failed remotely.
	 */
    private ServiceResponse serviceResponse;
    
    /**
     * Rule operation error collection.
     */
    private RuleOperationErrorCollection errors;
    
    /**
     * Initializes a new instance of the UpdateInboxRulesException class.
     * @param serviceResponse
     * The rule operation service response.
     * @param ruleOperations
     * The original operations.
     */
    public UpdateInboxRulesException(UpdateInboxRulesResponse serviceResponse,
    		Iterable<RuleOperation> ruleOperations){
    	super();
        this.serviceResponse = serviceResponse;
        this.errors = serviceResponse.getErrors();
        for (RuleOperationError error : this.errors) {
            error.setOperationByIndex(ruleOperations.iterator());
        }
    }
    
    /**
     * Gets the ServiceResponse for the exception.
     */
    public ServiceResponse getServiceResponse() {
       return this.serviceResponse; 
    }
    
    /**
     * Gets the rule operation error collection.
     */
    public RuleOperationErrorCollection getErrors() {
        return this.errors; 
    }
    
    /**
     * Gets the rule operation error code.
     */
    public ServiceError getErrorCode() {
        return this.serviceResponse.getErrorCode(); 
    }
    
    /**
     * Gets the rule operation error message.
     */
    public String getErrorMessage() {
        return this.serviceResponse.getErrorMessage(); 
    }

}
