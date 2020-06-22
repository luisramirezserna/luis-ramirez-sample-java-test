package com.clip.api.trx.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.clip.api.properties.ErrorMessageProperties;
import com.clip.api.trx.dto.TransactionRequestDTO;

@Component
public class TransactionRequestValidator implements Validator {

	private static final String DATE_REGEX = "\\d{4}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])";
	
	@Autowired
	ErrorMessageProperties errorMessageProperties;
	
	@Override
	public boolean supports(Class<?> clazz) {		
		return TransactionRequestDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (null == target) {
			errors.reject("request.empty", errorMessageProperties.getEmptyBody());
		} else {
			TransactionRequestDTO req = (TransactionRequestDTO) target;
			ValidationUtils.rejectIfEmpty(errors, "amount", "amount.empty", errorMessageProperties.getEmptyAmount());
			if (StringUtils.isEmpty(req.getDate())) {
				errors.rejectValue("date", "date.empty", errorMessageProperties.getEmptyDate());
			} else {
				Pattern pattern = Pattern.compile(DATE_REGEX);
				Matcher matcher = pattern.matcher(req.getDate());
				if (!matcher.matches()) {					
					errors.rejectValue("date", "date.invalid", errorMessageProperties.getInvalidDate());
				}
			}			
		}		
		
	}

}
