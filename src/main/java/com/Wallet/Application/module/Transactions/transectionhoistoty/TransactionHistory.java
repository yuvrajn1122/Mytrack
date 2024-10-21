package com.Wallet.Application.module.Transactions.transectionhoistoty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "apna_transaction_history")
public class TransactionHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;  

    private String transactionNumber; 

    private LocalDateTime createdOn;  

    private BigDecimal amount;  

    private String status;  // Transaction status (e.g., "SUCCESS", "FAILED")

    private String transactionType;  // Type of transaction (e.g., "CREDIT", "DEBIT")

    private long userId;  // Account related to the transaction
}
