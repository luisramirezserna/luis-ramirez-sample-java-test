package com.clip.api.exception;

import com.clip.api.trx.dto.ErrorType;

public class APIBaseException extends RuntimeException {	
	

	private static final long serialVersionUID = -8379020522010455617L;
	
	private ErrorType errorType;
    
    public APIBaseException(ErrorType errorType, String message, Throwable t) {
		super(message, t);
		this.errorType = errorType;
	}
        
    public APIBaseException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}
    
    public APIBaseException(ErrorType errorType, Throwable t) {
		super(errorType.getMessage(), t);
		this.errorType = errorType;
	}
    
    public APIBaseException(ErrorType errorType) {
		super(errorType.getMessage(), null , false, false);		
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
    	
}
