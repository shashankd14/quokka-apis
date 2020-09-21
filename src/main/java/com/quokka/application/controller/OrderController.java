package com.quokka.application.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quokka.application.entity.Cart;
import com.quokka.application.entity.Order;
import com.quokka.application.entity.User;
import com.quokka.application.service.CartService;
import com.quokka.application.service.OrderService;
import com.quokka.application.service.UserService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@PostMapping("/place")
	public ResponseEntity<Object> addNew(@RequestParam("userId") int userId,
			@RequestParam(value = "address", required = false) String address) {

		Order order = new Order();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {

			User user = userService.getById(userId);
			order.setAddress(address);
			order.setStatusId(7);

			order.setCreatedBy(user);
			order.setUpdatedBy(user);

			order.setCreatedOn(timestamp);
			order.setUpdatedOn(timestamp);

			orderService.add(order);

			List<Cart> cartItems = cartService.getCartItems(userId);

			for (Cart item : cartItems) {

				item.setOrderId(order.getOrderId());

				cartService.update(item);
			}

		} catch (Exception e) {

		}

		return null;
	}

	@PutMapping("/updateStatus")
	public ResponseEntity<Object> updateStatus(
			@RequestParam("orderId") int orderId,
			@RequestParam("statusId") int statusId) {
		
		try {
			
			Order order = orderService.updateStatus(orderId, statusId);
			
			return new ResponseEntity<Object>(order, HttpStatus.OK);
		}
		catch (Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/list")
	public ResponseEntity<Object> orderItems(@RequestParam("userId") int userId) {
		
		try {
			
			List<JSONObject> orderItems = orderService.getOrderItems(userId);
			
			return new ResponseEntity<Object>(orderItems, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	

	
}
