package com.quokka.application.controller;

import com.quokka.application.entity.Cart;
import com.quokka.application.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/cart"})
public class CartController {
  @Autowired
  private CartService cartService;
  
  @PostMapping({"/add"})
	public Cart addToCart(@RequestParam("userId") int userId, 
			@RequestParam("productId") int productId,
			@RequestParam("variant") String variant, 
			@RequestParam("quantity") int quantity) {
		return this.cartService.addToCart(userId, productId, quantity, variant);
	}
  
  @GetMapping({"/list"})
  public ResponseEntity<Object> cartItems(@RequestParam("userId") int userId) {
    return this.cartService.cartItems(userId);
  }
}
