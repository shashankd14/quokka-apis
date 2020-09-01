package com.quokka.application.service;

import java.util.List;

import com.quokka.application.entity.User;

public interface UserService {

	public User save(User user);
	
	public User getById(int userId);
	
	public List<User>getAll();
	
	public void deleteById(int userId);
	
	public User verifyIfEmailIdExists(String email);
	
}
