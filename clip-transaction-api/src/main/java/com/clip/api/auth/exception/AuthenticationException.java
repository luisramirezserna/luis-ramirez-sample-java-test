package com.clip.api.auth.exception;

import com.clip.api.exception.APIBaseException;
import com.clip.api.trx.dto.ErrorType;

public class AuthenticationException extends APIBaseException {	
	
	private static final long serialVersionUID = 7516200691888303901L;

	public AuthenticationException(ErrorType errorType, String message, Throwable t) {
		super(errorType, message, t);		
	}
        
    public AuthenticationException(ErrorType errorType, String message) {
		super(errorType, message);		
	}
    
    public AuthenticationException(ErrorType errorType, Throwable t) {
		super(errorType, errorType.getMessage(), t);		
	}
    
    public AuthenticationException(ErrorType errorType) {
		super(errorType);		
	}
    	
}
