package com.Wallet.Application.module.Transactions.transectionhoistoty;

public interface TransactionHistoryService {

    TransactionHistory createTransaction(TransactionHistory transaction);
    
    TransactionHistory updateTransaction(long id, TransactionHistory transaction);
    
    TransactionHistory getTransactionById(long id);
    
    
    
}
