package com.Wallet.Application.module.Transactions.productTransectionLimite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor @Builder
public class ProductDetailsResponse {
	
	private String statusCode;
	
	private String message;

	

}
