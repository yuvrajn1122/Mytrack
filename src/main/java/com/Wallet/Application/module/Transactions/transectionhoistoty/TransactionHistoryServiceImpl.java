package com.Wallet.Application.module.Transactions.transectionhoistoty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.security.exception.TransactionNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
	
	@Autowired
	private TransactionHistoryRepository transactionHistoryRepository;
	  @Autowired
	    private UserRepository userRepository;
	
	 
	  Logger logger;
	
	@Override
    public TransactionHistory createTransaction(TransactionHistory transaction) {
        return transactionHistoryRepository.save(transaction);
        
        
    }
	
	@Override
    public TransactionHistory updateTransaction(long id, TransactionHistory transaction) {
        TransactionHistory existingTransaction = getTransactionById(id);
        if (existingTransaction != null) {
            existingTransaction.setAmount(transaction.getAmount());
            existingTransaction.setStatus(transaction.getStatus());
            existingTransaction.setTransactionType(transaction.getTransactionType());
            return transactionHistoryRepository.save(existingTransaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found with id: " + id);
        }
    }

    @Override
    public TransactionHistory getTransactionById(long id) {
        Optional<TransactionHistory> transaction = transactionHistoryRepository.findById(id);
        return transaction.orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }
	
   

  
/*
    @Override
    public User setDailyLimit(Long userId, BigDecimal dailyLimit) {
        logger.info("Setting daily limit for user with ID: {} to {}", userId, dailyLimit);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        user.setDailyLimit(dailyLimit);
        User updatedUser = userRepository.save(user);

        logger.info("Daily limit set successfully for user with ID: {}", userId);
        return updatedUser;
    }

    @Override
    public boolean canUserSpend(Long userId, BigDecimal amount) {
        logger.info("Checking if user with ID: {} can spend an amount of {}", userId, amount);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        // Reset spentToday if the last transaction date is not today
        if (user.getLastTransactionDate() == null || !user.getLastTransactionDate().isEqual(LocalDate.now())) {
            logger.info("Resetting spentToday for user with ID: {} since it's a new day", userId);
            user.setSpentToday(BigDecimal.ZERO);
            user.setLastTransactionDate(LocalDate.now());
        }

        BigDecimal remainingAmount = user.getDailyLimit().subtract(user.getSpentToday());
        logger.info("User with ID: {} has {} remaining for the day", userId, remainingAmount);

        boolean canSpend = remainingAmount.compareTo(amount) >= 0;
        if (canSpend) {
            logger.info("User with ID: {} is allowed to spend the amount of {}", userId, amount);
        } else {
            logger.warn("User with ID: {} cannot spend the amount of {}. Exceeds daily limit.", userId, amount);
        }
        return canSpend;
    }

    @Override
    public void updateSpentAmount(Long userId, BigDecimal amount) {
        logger.info("Updating spent amount for user with ID: {} by {}", userId, amount);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        // Reset spentToday if the last transaction date is not today
        if (user.getLastTransactionDate() == null || !user.getLastTransactionDate().isEqual(LocalDate.now())) {
            logger.info("Resetting spentToday for user with ID: {} since it's a new day", userId);
            user.setSpentToday(BigDecimal.ZERO);
            user.setLastTransactionDate(LocalDate.now());
        }

        // Update the amount spent today
        user.setSpentToday(user.getSpentToday().add(amount));
        userRepository.save(user);

        logger.info("Updated spent amount for user with ID: {}. New spent today: {}", userId, user.getSpentToday());
    }
    */
}
