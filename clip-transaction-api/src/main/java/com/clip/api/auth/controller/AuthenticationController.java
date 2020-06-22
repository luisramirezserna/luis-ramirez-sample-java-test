package com.clip.api.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.clip.api.auth.dto.AuthenticationRequestDTO;
import com.clip.api.auth.dto.AuthenticationResponseDTO;
import com.clip.api.auth.exception.AuthenticationException;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.util.AuthenticationUtil;

@RestController
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserDetailsService authService;
	
	@RequestMapping(value = "/clip/authenticate", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<AuthenticationResponseDTO> getToken(@Valid @RequestBody final AuthenticationRequestDTO user) {
		
		authenticate(user.getName(), user.getPassword());
		
		final UserDetails userDetails = authService.loadUserByUsername(user.getName());

		String token = AuthenticationUtil.generateToken(userDetails);
		AuthenticationResponseDTO authResp = new AuthenticationResponseDTO();
		authResp.setAccessToken("Bearer " + token);
		return ResponseEntity.ok(authResp);
	}

	private void authenticate(String username, String password) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException(ErrorType.AUTH_INVALID_USER, "USER_DISABLED", e);			
		} catch (BadCredentialsException e) {
			throw new AuthenticationException(ErrorType.AUTH_INVALID_USER);			
		}
	}
}
