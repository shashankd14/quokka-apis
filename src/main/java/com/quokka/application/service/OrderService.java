package com.quokka.application.service;

import java.util.List;

import com.quokka.application.entity.Order;

public interface OrderService {

	Order addNew(Order order);
	
	Order update(Order order);
	
	List<Order> getAll ();
	
	Order getById (int id);
	
	void deleteById(int id);
	
	
}
