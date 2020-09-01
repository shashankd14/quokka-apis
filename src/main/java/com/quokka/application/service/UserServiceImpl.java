package com.quokka.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quokka.application.dao.UserRepository;
import com.quokka.application.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public User save(User user) {
		return userRepo.save(user);
	}

	@Override
	public User getById(int userId) {
		
		Optional<User> result = userRepo.findById(userId);
		
		User theUser = null;
		
		if (result.isPresent()) {
			theUser = result.get();
		}
		else {
			// we didn't find the employee
			throw new RuntimeException("Did not find employee id - " + userId);
		}
		
		return theUser;
	}

	@Override
	public List<User> getAll() {
		return userRepo.findAll();
	}

	@Override
	public void deleteById(int userId) {

		userRepo.deleteById(userId);
	}

	@Override
	public User verifyIfEmailIdExists(String email) {
		
		User user = userRepo.verifyIfEmailIdExists(email);

		return user;
	}


}
