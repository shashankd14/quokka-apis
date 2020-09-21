package com.quokka.application.controller;

import com.quokka.application.entity.Cart;
import com.quokka.application.entity.User;
import com.quokka.application.service.CartService;
import com.quokka.application.service.UserService;

import net.minidev.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping({"/api/cart"})
public class CartController {
  @Autowired
  private CartService cartService;
  
  @Autowired
  private JavaMailSender javaMailSender;
  
  @Autowired
  private UserService userService;
  
  @PostMapping({"/add"})
	public ResponseEntity<Object> addToCart(@RequestParam("userId") int userId, 
			@RequestParam("productId") int productId,
			@RequestParam("variant") String variant, 
			@RequestParam("quantity") int quantity) {
	  
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	  
	  Cart cart = new Cart();
	  try {
		  
		  cart.setUserId(userId);
		  cart.setProductId(productId);
		  cart.setQuantity(quantity);
		  cart.setVariant(variant);
		  
		//  cart.setStatusId(3);
		  cart.setCreatedOn(timestamp);
		  cart.setUpdatedOn(timestamp);
		  cart.setUpdatedBy(userId);
		  cart.setIsDeleted(Boolean.valueOf(false));
		  
		  cartService.addToCart(cart);
		  
	  }catch(Exception e) {
		   return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
		return new ResponseEntity<Object>("Product added to cart successfully!!",HttpStatus.OK);
	}
  
  @RequestMapping("/placeOrder")
  public ResponseEntity<Object> placeOrder(@RequestParam("userId") int userId){
	  
	  try {
		  
		  List<Cart> cartItems = cartService.getCartItems(userId);
		 // cartService.updateOrderStatusOnPlaced(userId);
		  
		  User user = userService.getById(userId);
		  SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(user.getEmail());

			msg.setSubject("Order Details");
			msg.setText("Thank you for shopping with Quokka!! \n Your order details are as below: " + cartItems);

			javaMailSender.send(msg);
			
		  return new ResponseEntity<Object>("Order placed successfully!!",HttpStatus.OK);
	  }catch(Exception e) {
		  
		  return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  
  }
  @GetMapping({"/list"})
  public ResponseEntity<Object> cartItems(@RequestParam("userId") int userId) {
	  
	  try {
		  
		  List<Cart> cartItems = cartService.getCartItems(userId);
		  
		  return new ResponseEntity<Object>(cartItems ,HttpStatus.OK);
		  
	  }catch(Exception e) {
		  
		  return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  
	  
  }
  
  @GetMapping("/order")
  public ResponseEntity<Object> orderList(@RequestParam("userId") int userId){
	  
	  try {
		  
		  List<JSONObject> cartItems = null;
		  
		  return new ResponseEntity<Object>(cartItems ,HttpStatus.OK);
	  }catch(Exception e) {
		  
		  return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
  }
  
  
}
