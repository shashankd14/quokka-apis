package com.quokka.application.service;

import java.util.List;

import com.quokka.application.entity.Order;

import net.minidev.json.JSONObject;

public interface OrderService {

	public Order add(Order order);
	
	public void updateStatus(int orderId, int statusId);
	
	public List<Order> getAll ();
	
	public Order getById (int id);

	public List<JSONObject> getOrderItems(int userId);
	
}
