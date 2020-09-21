package com.quokka.application.service;

import com.quokka.application.dao.CartRespository;
import com.quokka.application.entity.Cart;
import com.quokka.application.entity.Product;
import com.quokka.application.service.CartService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRespository cartRepository;

	public List<Cart> getCartItems(int userId) {

		List<Cart> cartItems = this.cartRepository.cartItems(Integer.valueOf(userId));
		return cartItems;
	}

	public Cart addToCart(Cart cart) {

		return cartRepository.save(cart);
	}

	@Override
	public Cart update(Cart cart) {

		return cartRepository.save(cart);
	}

	@Override
	public Cart getById(int cardId) {

		Optional<Cart> result = cartRepository.findById(Integer.valueOf(cardId));
		Cart theItem = null;
		if (result.isPresent()) {
			theItem = result.get();
		} else {
			throw new RuntimeException("Did not find theProduct id - " + theItem);
		}
		return theItem;

	}
}
