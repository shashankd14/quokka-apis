package com.quokka.application.service;

import com.quokka.application.entity.Cart;
import org.springframework.http.ResponseEntity;

public interface CartService {
  ResponseEntity<Object> cartItems(int paramInt);
  
  Cart addToCart(int paramInt1, int paramInt2, int paramInt3, String paramString);
}
