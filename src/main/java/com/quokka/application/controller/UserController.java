package com.quokka.application.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
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
			
			user.setIsActive(false);
			
			user.setCreatedBy(1);
			user.setUpdatedBy(1);
			
			user.setCreatedOn(timestamp);
			user.setUpdatedOn(timestamp);
			
			user.setIsDeleted(false);
			
			User savedUser = userService.save(user);

			return new ResponseEntity<Object>(savedUser, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}
	
	@PostMapping("/addRetailerUser")
	public ResponseEntity<Object> addManufacturerUser(
			@RequestParam("username") String username,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email,
			@RequestParam("retailerId") int createdBy) {
		


		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		User user = new User();
		try {
			
			Integer otp = OTPgenerator();

			user.setUserId(0);
			user.setUsername(username);
			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);
			user.setOtp(otp);
			
			user.setCreatedBy(createdBy);
			user.setUpdatedBy(createdBy);
			
			user.setCreatedOn(timestamp);
			user.setUpdatedOn(timestamp);
			
			user.setIsActive(false);
			user.setIsDeleted(false);
			
			User savedUser = userService.save(user);
			
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);

			msg.setSubject("Welcome to Quokka!!");
			msg.setText("Greetings!! \n You have been invited to explore the world of quokka. "
					+ "You can access quokka @app.quokkalens.com with your email address or phone number and your one time access code is: " + otp);

			javaMailSender.send(msg);

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
	public ResponseEntity<Object> getAll(@RequestParam("createdBy") int createdBy){
		
		try {
			
			List<User> userList = userService.getAll(createdBy);
			
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
			 
			 JSONObject entity = new JSONObject();
			  entity = userService.getUserDetails(userString);
			 
			 if(entity == null) {
				 return new ResponseEntity<Object>("user not found", HttpStatus.INTERNAL_SERVER_ERROR);
			 }
			 
				else {

					int userId = Integer.parseInt(entity.getAsString("userId"));

					User user = userService.getById(userId);
					user.setIsActive(true);
					userService.save(user);
					
					System.out.println("user \n"+user);

				}
			 
			 
			 
			 
//			 User loggedInUser = userService.getById(user.getUserId());
//			 loggedInUser.setIsActive(true);
			 
	//		 userService.save(loggedInUser);
			 return new ResponseEntity<Object>(entity, HttpStatus.OK);
			 
		 }catch (Exception e) {
			 
			 e.printStackTrace();
			 return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
	}
	
	public int OTPgenerator() {

		Random rand = new Random();
		System.out.printf("%04d%n", rand.nextInt(9999));

		String otp = String.format("%04d", rand.nextInt(9999));

		return Integer.parseInt(otp);

	}

}
