package com.clip.api.trx.exception;

import com.clip.api.exception.APIBaseException;
import com.clip.api.trx.dto.ErrorType;

public class TransactionException extends APIBaseException {	
	
	private static final long serialVersionUID = 7424897263899710728L;

	public TransactionException(ErrorType errorType, String message, Throwable t) {
		super(errorType, message, t);		
	}
        
    public TransactionException(ErrorType errorType, String message) {
		super(errorType, message);		
	}
    
    public TransactionException(ErrorType errorType, Throwable t) {
		super(errorType, errorType.getMessage(), t);		
	}
    
    public TransactionException(ErrorType errorType) {
		super(errorType);		
	}
    	
}
