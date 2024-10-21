package com.Wallet.Application.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

	private String accountName;
	private String accountNumber;
	private BigDecimal accoutBalance;

}
