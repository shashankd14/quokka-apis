package com.quokka.application.service;

import java.util.List;

import com.quokka.application.entity.Cart;

import net.minidev.json.JSONObject;

public interface CartService {
	
	public List<Cart> getCartItems(int userId);

	public Cart addToCart(Cart cart);
	
	public Cart update(Cart cart);
	
	public Cart getById(int cardId);
}
