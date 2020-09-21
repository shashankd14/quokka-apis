package com.quokka.application.controller;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quokka.application.dao.UserRepository;
import com.quokka.application.entity.Product;
import com.quokka.application.entity.User;
import com.quokka.application.service.UserService;

@RestController
@CrossOrigin
public class EmailController {

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping("/sendEmail")
	public ResponseEntity<Object> sendEmail(@RequestParam("email") String email) {

		User user = userService.verifyIfEmailIdExists(email);

		if (user != null) {

			Integer otp = OTPgenerator();

//			Optional<User> result = this.userRepo.findById(user.getUserId());
//			
//			User theUser = null;
//			
//			if (result.isPresent()) {
//				theUser = result.get();
//			} else {
//				throw new RuntimeException("Did not find user id - ");
//			}
//			
//			theUser.setOtp(otp);

			user.setOtp(otp);

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);

			msg.setSubject("OTP for Quokka");
			msg.setText("Greetings from Quokka!! \n As requested, your one time password to access quokka is: " + otp);

			javaMailSender.send(msg);

			user.setOtp(otp);
			userService.save(user);

			return new ResponseEntity<Object>("success", HttpStatus.OK);

		}

		else {

			return new ResponseEntity<Object>("no email", HttpStatus.NO_CONTENT);
		}

	}
	
	public int OTPgenerator(){
		
		Random rand = new Random();
		System.out.printf("%04d%n", rand.nextInt(9999));
		
		String otp = String.format("%04d", rand.nextInt(9999));
		
		return Integer.parseInt(otp);
		
	}
	
	
}
