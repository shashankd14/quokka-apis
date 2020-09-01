package com.quokka.application.dao;

import com.quokka.application.entity.Cart;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRespository extends JpaRepository<Cart, Integer> {
  @Query(nativeQuery = true, value = "SELECT productId, quantity, variant  FROM Cart where userId= :userId ")
  List<JSONObject> cartItems(@Param("userId") Integer userId);
}
