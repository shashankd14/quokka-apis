package com.quokka.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quokka.application.dao.UserRepository;
import com.quokka.application.entity.User;

import net.minidev.json.JSONObject;

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
	public List<User> getAll(int createdBy) {
		//return userRepo.findAll();
		return userRepo.getCustomersByManufacturer(createdBy);
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

	@Override
	public List<JSONObject> getUserDetails(String userString) {
		
		User user = null;
		
		List<JSONObject> entity = new ArrayList<JSONObject>();
		if(userString.contains("@")) {
			
			entity = userRepo.getUserDetailsByEmailId(userString);
		}
		else
			
			entity = userRepo.getUserDetailsByPhoneNumber(userString);
		
		return entity;
	}


}
