package com.clip.api.trx.dto;

import java.util.EnumSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.clip.api.properties.ErrorMessageProperties;

public enum ErrorType {
		
	GENERAL_ERROR {

		@Override
		public String getMessage() {			
			return errorMessageProperties.getGeneral();
		}
		
	},
	FILE_ERROR {
		@Override
		public String getMessage() {			
			return errorMessageProperties.getFile();
		}
	},
	TRANSACTION_NOT_FOUND {
		@Override
		public String getMessage() {			
			return errorMessageProperties.getTransactionNotFound();
		}
	},
	
	REQUEST_VALIDATION {
		@Override
		public String getMessage() {			
			return !StringUtils.isEmpty(getCustomErrorMessage().trim()) ? getCustomErrorMessage() :  errorMessageProperties.getInvalidRequest();
		}
	},
	
	AUTH_INVALID_USER {
		@Override
		public String getMessage() {			
			return errorMessageProperties.getInvalidUser();
		}
	}, 
	AUTH_INVALID_USER_NAME {
		@Override
		public String getMessage() {			
			return errorMessageProperties.getInvalidUserName();
		}
	},
	AUTH_INVALID_TOKEN {
		@Override
		public String getMessage() {			
			return errorMessageProperties.getInvalidToken();
		}
	},
	INVALID_PARAM_VALUE {
		@Override
		public String getMessage() {			
			return !StringUtils.isEmpty(getCustomErrorMessage().trim()) ? getCustomErrorMessage() :  errorMessageProperties.getInvalidRequest();
		}
	};
						

	ErrorMessageProperties errorMessageProperties;
	private String customErrorMessage;
	
	public abstract String getMessage ();
	
	public String getCustomErrorMessage() {
		return customErrorMessage;
	}
	public void setCustomErrorMessage(String customErrorMessage) {
		this.customErrorMessage = customErrorMessage;
	}	
	
}

@Component
class ErrorTypeMessageInjector {
	@Autowired
	ErrorMessageProperties errorMessageProperties;
	
	@PostConstruct
    public void postConstruct() {
        for (ErrorType et : EnumSet.allOf(ErrorType.class)) {
           et.errorMessageProperties = errorMessageProperties;
        }
    }
	
}
