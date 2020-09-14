package com.quokka.application.jwt.resource;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider implements AuthenticationProvider {

		
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// UserDetails userDetails = this.jwtInMemoryUserDetailsService.loadUserByUsername(username);
		String username = auth.getName();
        String password = auth.getCredentials()
            .toString();
 
        if ("externaluser".equals(username) && "pass".equals(password)) {
            return new UsernamePasswordAuthenticationToken
              (username, password, Collections.emptyList());
        } else {
            throw new 
              BadCredentialsException("External system authentication failed");
        }
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
