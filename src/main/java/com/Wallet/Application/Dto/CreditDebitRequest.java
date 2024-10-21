package com.Wallet.Application.Dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDebitRequest {
	
	private String accountNumber;
	
	private BigDecimal ammount;

}
