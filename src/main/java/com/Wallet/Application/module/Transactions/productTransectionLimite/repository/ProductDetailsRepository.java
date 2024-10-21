package com.Wallet.Application.module.Transactions.productTransectionLimite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wallet.Application.module.Transactions.productTransectionLimite.entity.ProductDetails;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
	 List<ProductDetails> findByUserId(Long userId);

}
