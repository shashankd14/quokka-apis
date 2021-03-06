package com.quokka.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quokka.application.dao.CustomUserDetails;
import com.quokka.application.dao.UserRepository;
import com.quokka.application.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> optionalUser = null;

		if (username.contains("@"))
			optionalUser = userRepo.findByEmail(username);

		else
			optionalUser = userRepo.findByPhoneNumber(username);

		optionalUser.orElseThrow(() -> new UsernameNotFoundException("user not found"));

		return optionalUser.map(CustomUserDetails::new).get();
	}
	


}
