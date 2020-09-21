package com.quokka.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quokka.application.dao.OrderRepository;
import com.quokka.application.entity.Order;

import net.minidev.json.JSONObject;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Override
	public Order add(Order order) {
		return orderRepo.save(order);
	}

	@Override
	public Order updateStatus(int orderId, int statusId) {
		return orderRepo.updateStatus(orderId, statusId);
	}

	@Override
	public List<Order> getAll() {
		return orderRepo.findAll();
	}

	@Override
	public Order getById(int id) {
		
		Optional<Order> result = orderRepo.findById(id);

		Order theOrder = null;

		if (result.isPresent()) {
			theOrder = result.get();
		} else {
			throw new RuntimeException("Did not find order id - " + id);
		}

		return theOrder;
	}

	@Override
	public List<JSONObject> getOrderItems(int userId) {
		
		return orderRepo.getOrderItems(userId);
	}


}
