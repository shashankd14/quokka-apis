package com.quokka.application.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quokka.application.entity.User;
import com.quokka.application.service.UserService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/add")
	public ResponseEntity<Object> addUser(
			@RequestParam("username") String username,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email,
			@RequestParam("password") String password) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		User user = new User();
		try {

			user.setUserId(0);
			user.setUsername(username);
			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);
			user.setPassword(password);
			
			user.setCreatedBy(2);
			user.setUpdatedBy(2);
			
			user.setCreatedOn(timestamp);
			user.setUpdatedOn(timestamp);
			
			user.setIsDeleted(false);
			
			User savedUser = userService.save(user);

			return new ResponseEntity<Object>(savedUser, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}
	
	@PutMapping("/update")
	public ResponseEntity<Object> addUser(
			@RequestParam("userId") int userId,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email,
			@RequestParam("password") String password) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		
		try {

			User user = userService.getById(userId);
			
			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);
			user.setPassword(password);
			
			user.setCreatedBy(2);
			user.setUpdatedBy(2);
			
			user.setCreatedOn(timestamp);
			user.setUpdatedOn(timestamp);
			
			user.setIsDeleted(false);
			
			User updatedUser = userService.save(user);

			return new ResponseEntity<Object>(updatedUser, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}
	
	@GetMapping("/getById")
	public ResponseEntity<Object> getById(int userId) {
		
		try {
			
			User user = userService.getById(userId);
			
			return new ResponseEntity<Object>(user, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> getAll(){
		
		try {
			
			List<User> userList = userService.getAll();
			
			return new ResponseEntity<Object>(userList, HttpStatus.OK);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteById")
	public ResponseEntity<Object> deleteById(int userId) {
		
		try {
			
			userService.deleteById(userId);
			
			return new ResponseEntity<Object>("User deleted sucessfully", HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	
		
	}
	
	@GetMapping("/getDetails")
	public ResponseEntity<Object> getUserId(@RequestParam("userString") String userString){
		
		 try {
			 
			 JSONObject user = userService.getUserDetails(userString);
			 
//			 if(user == null) {
//				 return new ResponseEntity<Object>("user not found", HttpStatus.INTERNAL_SERVER_ERROR);
//			 }
			 return new ResponseEntity<Object>(user, HttpStatus.OK);
			 
		 }catch (Exception e) {
			 
			 e.printStackTrace();
			 return new ResponseEntity<Object>("error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
	}

}
