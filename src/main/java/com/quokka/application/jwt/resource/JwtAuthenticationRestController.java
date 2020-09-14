package com.quokka.application.jwt.resource;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.quokka.application.entity.User;
import com.quokka.application.jwt.JwtTokenUtil;
import com.quokka.application.service.CustomUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationRestController {

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private CustomUserDetailsService jwtUserDetailsService;
  
  

  @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
			throws AuthenticationException {

		User user = (User) jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		authenticate(user, authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtTokenResponse(token));
	}

  @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);

		User user = (User) jwtUserDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

  @ExceptionHandler({ AuthenticationException.class })
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  private void authenticate(User user, String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);

    String email = user.getEmail();
    String phoneNumber = user.getPhoneNumber();
    
    
    
    try {  
		if (password.length() < 5) {

			if (((email.equals(username)) || phoneNumber.equals(username))
					&& user.getOtp().equals(Integer.parseInt(password))) {

				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, user.getPassword()));
			}

			else
				throw new AuthenticationException("INVALID_CREDENTIALS1", new Exception());
		}
    	
      else if (((email.equals(username)) || phoneNumber.equals(username)) && user.getPassword().equals(password)){
    	  
    	  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      }
    	
		else {

			throw new AuthenticationException("INVALID_CREDENTIALS2", new Exception());
		}
    
    	  
      
    } catch (DisabledException e) {
      throw new AuthenticationException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("INVALID_CREDENTIALS", e);
    }
  }
  
 
  
 
}

