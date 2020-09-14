package com.quokka.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.quokka.application.dao.OrderRepository;
import com.quokka.application.entity.Order;

public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Override
	public Order addNew(Order order) {
		return orderRepo.save(order);
	}

	@Override
	public Order update(Order order) {
		return orderRepo.save(order);
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
			throw new RuntimeException("Did not find employee id - " + id);
		}

		return theOrder;
	}

	@Override
	public void deleteById(int id) {
		orderRepo.deleteById(id);
	}

}
