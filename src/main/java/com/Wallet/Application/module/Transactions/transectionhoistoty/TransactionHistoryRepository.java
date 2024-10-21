package com.Wallet.Application.module.Transactions.transectionhoistoty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
	
	 List<TransactionHistory> findByTransactionNumber(String transactionNumber);
	 
}

