package com.quokka.application.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quokka.application.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

    @Value("${jwt.get.token.uri}")
    private String authenticationPath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(extractedPassword());
    }

//    @Bean
//    public PasswordEncoder passwordEncoderBean() {
//        return new PasswordEncoder();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.cors().and()
				.csrf().disable().exceptionHandling()
		
				.authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/user/add").permitAll()
				.antMatchers("/authenticate").permitAll()
				.antMatchers("/sendEmail").permitAll()
				.antMatchers("/api/product/add").hasAuthority("ROLE_MANUFACTURER")
				.antMatchers("/api/product/update").hasAuthority("ROLE_MANUFACTURER")
				.antMatchers("/api/product/delete").hasAuthority("ROLE_MANUFACTURER")
				
				.antMatchers("/api/project/add").hasAnyAuthority("ROLE_INTERIOR_DESIGNER", "ROLE_REALTOR")
				.antMatchers("/api/project/update").hasAnyAuthority("ROLE_INTERIOR_DESIGNER", "ROLE_REALTOR")
				.antMatchers("/api/project/delete").hasAnyAuthority("ROLE_INTERIOR_DESIGNER", "ROLE_REALTOR")
				.antMatchers("/api/project/getById/**").hasAnyAuthority("ROLE_INTERIOR_DESIGNER", "ROLE_REALTOR", 
						"ROLE_APPUSER", "ROLE_ADMIN")
				.antMatchers("/api/project/list").hasAnyAuthority("ROLE_INTERIOR_DESIGNER", "ROLE_REALTOR", 
						"ROLE_APPUSER", "ROLE_ADMIN")
				
				
				.antMatchers("/api/project/addAssetBundle").hasAuthority("ROLE_ADMIN")
				.antMatchers("/api/product/addAssetBundle").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated();

		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity.headers().frameOptions().sameOrigin() // H2 Console Needs this setting
				.cacheControl(); // disable caching

	}

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
            .ignoring()
            .antMatchers(
                HttpMethod.POST,
                authenticationPath
            )
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .and()
            .ignoring()
            .antMatchers(
                HttpMethod.GET,
                "/" //Other Stuff You want to Ignore
            )
            .and()
            .ignoring()
            .antMatchers("/h2-console/**/**");//Should not be in Production!
        
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

