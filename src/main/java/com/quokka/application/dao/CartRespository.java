package com.quokka.application.dao;

import com.quokka.application.entity.Cart;
import java.util.List;

import javax.transaction.Transactional;

import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRespository extends JpaRepository<Cart, Integer> {
  @Query(nativeQuery = true, value = "SELECT * FROM cart where userId= :userId AND orderId is NULL")
  List<Cart> cartItems(@Param("userId") Integer userId);
  
}
