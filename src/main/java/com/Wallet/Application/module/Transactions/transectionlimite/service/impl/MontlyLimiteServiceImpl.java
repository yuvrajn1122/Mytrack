package com.Wallet.Application.module.Transactions.transectionlimite.service.impl;

import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.Transactions.transectionlimite.dto.CreateAndUpdateProductRequest;
import com.Wallet.Application.module.Transactions.transectionlimite.entity.MontlyLimite;
import com.Wallet.Application.module.Transactions.transectionlimite.repository.MontlyLimiteRepository;
import com.Wallet.Application.module.Transactions.transectionlimite.service.MontlyLimiteService;
import com.Wallet.Application.module.security.authentication.TokenUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class MontlyLimiteServiceImpl implements MontlyLimiteService {

    private static final Logger logger = LoggerFactory.getLogger(MontlyLimiteServiceImpl.class);

    @Autowired
    private MontlyLimiteRepository montlyLimiteRepository;
    
    @Autowired
	private UserRepository userRepository;

    @Override
    public MontlyLimite setDailyLimit(BigDecimal dailyLimit, HttpServletRequest request) {
        Long userId = extractUserId(request);
        logger.info("Setting daily limit for userId: {}, dailyLimit: {}", userId, dailyLimit);

        LocalDate today = LocalDate.now();

        MontlyLimite limite = montlyLimiteRepository.findByUserId(userId)
            .map(existingLimite -> {
                // Check if the daily limit is being updated today
                if (existingLimite.getUpdatedon().isEqual(today)) {
                    existingLimite.setDailyLimit(dailyLimit);
                    existingLimite.setUpdatedon(today);
                    logger.info("Updated existing daily limit for userId: {} on the same day", userId);
                    return existingLimite;
                } else {
                    // Create a new entry if it's a different day
                    MontlyLimite newLimite = MontlyLimite.builder()
                        .user_id(userId)
                        .dailyLimit(dailyLimit)
                        .updatedon(today)
                        .monthlyLimite(existingLimite.getMonthlyLimite())
                        .build();
                    logger.info("Created new daily limit entry for userId: {} on a different day", userId);
                    return newLimite;
                }
            })
            .orElseGet(() -> {
                // No existing limit found, create a new one
                MontlyLimite newLimite = MontlyLimite.builder()
                    .user_id(userId)
                    .dailyLimit(dailyLimit)
                    .updatedon(today)
                    .monthlyLimite(BigDecimal.ZERO) // Initialize with zero if monthly limit isn't set yet
                    .build();
                logger.info("Created new daily limit for userId: {}", userId);
                return newLimite;
            });

        MontlyLimite savedLimite = montlyLimiteRepository.save(limite);
        logger.info("Successfully saved daily limit for userId: {}", userId);
        return savedLimite;
    }

    @Override
    public MontlyLimite setMonthlyLimit(BigDecimal monthlyLimit, HttpServletRequest request) {
        Long userId = extractUserId(request);
        logger.info("Setting monthly limit for userId: {}, monthlyLimit: {}", userId, monthlyLimit);

        Optional<MontlyLimite> byUserId = montlyLimiteRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();

        MontlyLimite limite = byUserId
            .map(existingLimite -> {
                // Check if the limit is being updated today
                if (existingLimite.getUpdatedon().isEqual(today)) {
                    existingLimite.setMonthlyLimite(monthlyLimit);
                    existingLimite.setUpdatedon(today);
                    logger.info("Updated existing monthly limit for userId: {} on the same day", userId);
                    return existingLimite;
                } else {
                    // Create a new entry if it's a different day
                    MontlyLimite newLimite = MontlyLimite.builder()
                        .user_id(userId)
                        .monthlyLimite(monthlyLimit)
                        .updatedon(today)
                        .dailyLimit(BigDecimal.ZERO)
                        .build();
                    logger.info("Created new monthly limit entry for userId: {} on a different day", userId);
                    return newLimite;
                }
            })
            .orElseGet(() -> {
                // No existing limit found, create a new one
                MontlyLimite newLimite = MontlyLimite.builder()
                    .user_id(userId)
                    .monthlyLimite(monthlyLimit)
                    .updatedon(today)
                    .dailyLimit(BigDecimal.ZERO)
                    .build();
                logger.info("Created new monthly limit for userId: {}", userId);
                return newLimite;
            });

        MontlyLimite savedLimite = montlyLimiteRepository.save(limite);
        logger.info("Successfully saved monthly limit for userId: {}", userId);
        return savedLimite;
    }

    

    @Override
    public MontlyLimite getLimiteByUserId(HttpServletRequest request) {
    	Long userId = extractUserId(request);
        logger.info("Retrieving limits for userId: {}", userId);

        MontlyLimite limite = montlyLimiteRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Limite not found for userId: {}", userId);
                    return new RuntimeException("Limite not found for userId: " + userId);
                });

        logger.info("Successfully retrieved limits for userId: {}", userId);
        return limite;
    }

    @Override
    public boolean canUserSpend(Long userId, BigDecimal amount) {
        logger.info("Checking if userId: {} can spend amount: {}", userId, amount);

        MontlyLimite limite = montlyLimiteRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Limite not found for userId: {}", userId);
                    return new RuntimeException("Limite not found for userId: " + userId);
                });

        BigDecimal dailyLimit = limite.getDailyLimit();
        boolean canSpend = amount.compareTo(dailyLimit) <= 0;

        logger.info("UserId: {} can spend: {} within daily limit: {}", userId, canSpend, dailyLimit);
        return canSpend;
    }
    
    public Long extractUserId(HttpServletRequest request) {
    	 String userEmail = TokenUtil.extractEmail(request);
   	 Optional<User> optionalUser = userRepository.findByEmail(userEmail);
       if (optionalUser.isPresent()) {
           User user = optionalUser.get();
           return user.getUserId();
       } else {
           return null; // Or throw an exception, or return an optional based on your design choice
       }
   }
}
