package com.clip.api.trx.controller.advisor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.clip.api.exception.APIBaseException;
import com.clip.api.properties.ErrorMessageProperties;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionErrorDTO;
import com.clip.api.util.TransactionMapperUtil;

@ControllerAdvice
public class TransactionControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionControllerExceptionHandler.class);
	
	@Autowired
	ErrorMessageProperties errorMessageProperties;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<TransactionErrorDTO> generalException(Exception e) {
		if(e.getCause() instanceof APIBaseException) {
			APIBaseException baseEx = (APIBaseException) e.getCause();
			return handleApiBaseException(baseEx);
		}
		LOGGER.error(e.getMessage(), e);
		return ResponseEntity.ok( TransactionMapperUtil.errorToDto(ErrorType.GENERAL_ERROR));
	}					
	
	@ExceptionHandler(APIBaseException.class)
	public ResponseEntity<TransactionErrorDTO> handleApiBaseException(APIBaseException e) {					
		LOGGER.error(e.getMessage(), e);		
		return ResponseEntity.ok( TransactionMapperUtil.errorToDto(e.getErrorType()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessages = ex.getBindingResult().getFieldErrors()
				.stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
		return ResponseEntity.ok(TransactionMapperUtil.errorToDto(ErrorType.REQUEST_VALIDATION, errorMessages));
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMessages = new ArrayList<String>();
		if (ex instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException mtme = (MethodArgumentTypeMismatchException) ex;
			switch (mtme.getName()) {
			case "userID":
				errorMessages.add(errorMessageProperties.getInvalidUseridParam());
				break;
			case "transactionID":
				errorMessages.add(errorMessageProperties.getInvalidTrxidParam());
				break;	
			default:
				break;
			}
			
		} else {
			errorMessages.add(ex.getMessage());
		}
		return ResponseEntity.ok(TransactionMapperUtil.errorToDto(ErrorType.INVALID_PARAM_VALUE, errorMessages));
	}
	
}
