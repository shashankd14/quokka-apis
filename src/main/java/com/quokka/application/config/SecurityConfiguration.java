package com.quokka.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.quokka.application.dao.UserRepository;
import com.quokka.application.service.CustomUserDetailsService;


@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private  CustomUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(extractedPassword());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
			
			.antMatchers("/user/add").permitAll()
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
				
				System.out.println("raw password: " + rawPassword + "\t encoded password: "+ encodedPassword);
				return rawPassword.equals(encodedPassword);
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
			
			 
		};
	}
	
	
}

