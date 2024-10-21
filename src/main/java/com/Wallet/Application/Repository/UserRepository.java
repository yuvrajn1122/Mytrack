package com.Wallet.Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wallet.Application.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  
	
	 Boolean existsByEmail(String email);
	
	 Boolean existsByAccountNumber(String accountNumber);
	
	 User findByAccountNumber(String accountNumber);
	 Optional<User> findByEmail(String email);
	 
	 Optional<User>  findById(Long id);
	 
	 

	
	
}