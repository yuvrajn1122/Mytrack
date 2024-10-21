package com.Wallet.Application.module.Transactions.productTransectionLimite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor @Builder
public class ProductDetailsRequest {

	
	@Column(nullable = false)
	private String productName;
	
	
	@Column(nullable = false)
	private BigDecimal productLimite;
	

	@Column(nullable = false)
	private Long user_id;

	@Column(nullable = false)
	private LocalDate updatedon;
}
