package com.quokka.application.dao;

import com.quokka.application.entity.User;

import net.minidev.json.JSONObject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	@Query(nativeQuery = true, value = "SELECT * from users where email= :email")
	public User verifyIfEmailIdExists(@Param("email") String email);
	
	@Query(nativeQuery = true, value = "SELECT userId from users where email = :userString")
	public JSONObject getUserDetailsByEmailId(@Param("userString") String userString);
	
	@Query(nativeQuery = true, value = "SELECT userId from users where phone_number= :userString")
	public JSONObject getUserDetailsByPhoneNumber(@Param("userString") String userString);
	
	@Query(nativeQuery = true, value = "SELECT * from users where createdBy= :createdBy")
	public List<User>  getCustomersByManufacturer(@Param("createdBy") int createdBy);
	
}
