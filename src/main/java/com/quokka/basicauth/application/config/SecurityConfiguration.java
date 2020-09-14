package com.quokka.basicauth.application.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.quokka.application.dao.UserRepository;
import com.quokka.application.service.CustomUserDetailsService;


@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private  CustomUserDetailsService userDetailsService;
	

	
	
//	public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        String username = auth.getName();
//        String password = auth.getCredentials().toString();
//        
//        System.out.println("username: "+username);
//        System.out.println("password: "+password);
//        // to add more logic
//        List<GrantedAuthority> grantedAuths = new ArrayList<>();
//        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
//        return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
//    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
			
			.antMatchers("/user/add").permitAll()
			.antMatchers("/user/getUserDetails").authenticated()
			//.antMatchers("/api/**").authenticated()
			.antMatchers("/api/**").authenticated()
			
			//.anyRequest().authenticated()
			//.antMatchers("/api/**").authenticated()
			.antMatchers("/").permitAll()
			
			.and()
			//.formLogin().disable()
			.httpBasic()
			.authenticationEntryPoint(new NoPopUpBasicAuthenticationEntryPoint());
			//.formLogin().loginPage("/signup").permitAll();
		
		
		}
	
	private PasswordEncoder extractedPassword() {
		return new PasswordEncoder() {
			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				
				return rawPassword.equals(encodedPassword);
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
			
			 
		};
	}
	
	
}

