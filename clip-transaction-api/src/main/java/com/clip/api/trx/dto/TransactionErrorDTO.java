package com.clip.api.trx.dto;

import java.util.List;

public class TransactionErrorDTO {
	
	private ErrorType errorType;
	private List<String> errorMessage;
	
	public ErrorType getErrorType() {
		return errorType;
	}
	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
	public List<String> getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(List<String> errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		return "TransactionErrorDTO [errorType=" + errorType + ", errorMessage=" + errorMessage + "]";
	}

		
		
}
