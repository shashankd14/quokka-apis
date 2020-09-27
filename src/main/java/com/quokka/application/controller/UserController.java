package com.quokka.application.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestMethod;
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
	public ResponseEntity<Object> addUser(@RequestParam("username") String username,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("email") String email,
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

	@RequestMapping(value="/addRetailerUser", method= {RequestMethod.PUT, RequestMethod.POST})  
	public ResponseEntity<Object> addManufacturerUser(@RequestParam("username") String username,
			@RequestParam("phoneNumber") String phoneNumber, 
			@RequestParam("email") String email,
			@RequestParam("retailerId") int createdBy,
			@RequestParam(value= "userId", required = false, defaultValue = "0")int userId) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	
		User userEntity = null;
		
		try {

			Integer otp = OTPgenerator();

			
			
			 if(userId == 0) {
				
				User user = new User();
				
				user.setUserId(userId);
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
				
				

				 userEntity = userService.save(user);
				 
				 SimpleMailMessage msg = new SimpleMailMessage();
					msg.setTo(email);

					msg.setSubject("Welcome to Quokka!!");
					msg.setText("Greetings!! \n You have been invited to explore the world of quokka. "
							+ "You can access quokka @ \n https://frontend-dot-quokka-project-alpha.uc.r.appspot.com/signup with your email address or phone number. \n"
							+ "Your one time access code is: "
							+ otp);

					javaMailSender.send(msg);
			}
			
			 else {
					
					User existinguser = userService.getById(userId);
					existinguser.setUsername(username);
					existinguser.setPhoneNumber(phoneNumber);
					existinguser.setEmail(email);
					//existinguser.setCreatedBy(createdBy);
					userEntity = userService.save(existinguser);
					
				}

			

			return new ResponseEntity<Object>(userEntity, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	public ResponseEntity<Object> addUser(@RequestParam("userId") int userId,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try {

			User user = userService.getById(userId);

			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);
			//user.setPassword(password);

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
	
	@RequestMapping("/resendInvite")
	public ResponseEntity<Object> resendUserInvite(@RequestParam int userId) {
		
		try {
			
			User user = userService.getById(userId);
			
			Integer otp = OTPgenerator();
			
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(user.getEmail());

			msg.setSubject("Reminder from Quokka!!");
			msg.setText("Greetings!! \n This is a reminder to explore the world of quokka. "
					+ "You can access quokka @ \n https://frontend-dot-quokka-project-alpha.uc.r.appspot.com/signup with your email address or phone number. \n"
					+ "Your one time access code is: "
					+ otp);

			javaMailSender.send(msg);
			
			return new ResponseEntity<Object>("Email sent successfully", HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
}
	

	
	


	@GetMapping("/getById")
	public ResponseEntity<Object> getById(@RequestParam int userId) {

		try {

			User user = userService.getById(userId);

			return new ResponseEntity<Object>(user, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/list")
	public ResponseEntity<Object> getAll(@RequestParam("createdBy") int createdBy) {

		try {

			List<User> userList = userService.getAll(createdBy);

			return new ResponseEntity<Object>(userList, HttpStatus.OK);

		} catch (Exception e) {

			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteById")
	public ResponseEntity<Object> deleteById(int userId) {

		try {

			userService.deleteById(userId);

			return new ResponseEntity<Object>("User deleted sucessfully", HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getDetails")
	public ResponseEntity<Object> getUserDetails(@RequestParam("userString") String userString) {

		try {

			List<JSONObject> entity = new ArrayList<JSONObject>();
			entity = userService.getUserDetails(userString);

			if (entity == null) {
				return new ResponseEntity<Object>("user not found", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			else {

			//	int userId = Integer.parseInt(entity.get(0).toString());
				
				int userId = (int) entity.get(0).get("userId");

					
					System.out.println("--userId : "+userId);
				
				User user = userService.getById(userId);
				user.setIsActive(true);
				userService.save(user);

			}

//			 User loggedInUser = userService.getById(user.getUserId());
//			 loggedInUser.setIsActive(true);

			// userService.save(loggedInUser);
			return new ResponseEntity<Object>(entity, HttpStatus.OK);

		} catch (Exception e) {

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
