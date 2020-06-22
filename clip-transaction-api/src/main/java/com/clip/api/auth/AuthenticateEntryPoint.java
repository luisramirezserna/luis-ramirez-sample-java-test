package com.clip.api.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionErrorDTO;
import com.clip.api.util.TransactionMapperUtil;
import com.google.gson.Gson;

@Component
public class AuthenticateEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		
		response.setContentType("application/json");
		response.setStatus(HttpStatus.OK.value());
		response.getWriter().println(new Gson().toJson(TransactionMapperUtil.errorToDto(ErrorType.AUTH_INVALID_TOKEN)));		
	}
}

