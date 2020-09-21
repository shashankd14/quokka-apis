package com.quokka.application.dao;

import com.quokka.application.entity.Cart;
import com.quokka.application.entity.Order;

import net.minidev.json.JSONObject;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update orders set statusId= :statusId where orderId= :orderId")
	Order updateStatus(@Param("orderId") Integer orderId, @Param("statusId") Integer statusId);

	@Query(nativeQuery = true, value = "select o.orderId, o.statusId, p.* from orders as o" + 
			"	left join cart as c on c.orderId = o.orderId" + 
			"    left join products as p on p.productId = c.productId" + 
			"    where o.createdby = :userId")
	List<JSONObject> getOrderItems(int userId);
	
}
