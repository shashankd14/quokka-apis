package com.quokka.application.service;

import com.quokka.application.dao.CartRespository;
import com.quokka.application.entity.Cart;
import com.quokka.application.service.CartService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
  @Autowired
  private CartRespository cartRepository;
  
  public ResponseEntity<Object> cartItems(int userId) {
    List<JSONObject> entities = new ArrayList<>();
    entities = this.cartRepository.cartItems(Integer.valueOf(userId));
    return new ResponseEntity(entities, HttpStatus.OK);
  }
  
  public Cart addToCart(int userId, int productId, int quantity, String variant) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Cart cart = new Cart();
    cart.setProductId(productId);
    cart.setQuantity(quantity);
    cart.setUserId(userId);
    cart.setVariant(variant);
    cart.setCreatedOn(timestamp);
    cart.setUpdatedOn(timestamp);
    cart.setUpdatedBy(userId);
    cart.setIsDeleted(Boolean.valueOf(false));
    this.cartRepository.save(cart);
    return cart;
  }
}
