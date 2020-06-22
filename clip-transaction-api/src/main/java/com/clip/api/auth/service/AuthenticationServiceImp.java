package com.clip.api.auth.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.clip.api.auth.exception.AuthenticationException;
import com.clip.api.properties.AuthenticationProperties;
import com.clip.api.trx.dto.ErrorType;

@Service
public class AuthenticationServiceImp implements UserDetailsService {

	@Autowired
	AuthenticationProperties authProperties;
		

	@Override
	public UserDetails loadUserByUsername(String name) throws AuthenticationException {		
		if (name.equals(authProperties.getUserName()) ) {
			return new User(authProperties.getUserName(), authProperties.getUserPassword(), new ArrayList<>());
		} else {
			throw new AuthenticationException(ErrorType.AUTH_INVALID_USER_NAME);
		}
	}

}
