package com.quokka.application.dao;

import com.quokka.application.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	@Query(nativeQuery = true, value = "SELECT * from users where email= :email")
	public User verifyIfEmailIdExists(@Param("email") String email);
	
	
}
