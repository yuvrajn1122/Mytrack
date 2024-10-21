package com.Wallet.Application.module.Transactions.transectionhoistoty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Wallet.Application.Entity.User;
import com.Wallet.Application.module.security.role.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransectionHistoryRequest {
	
	 private long transactionId;  

	    private String transactionNumber; 

	    private LocalDateTime createdOn;  

	    private BigDecimal amount;  

	    private String status;  // Transaction status (e.g., "SUCCESS", "FAILED")

	    private String transactionType;  // Type of transaction (e.g., "CREDIT", "DEBIT")

	    private long userId; 
	    
	    
}
