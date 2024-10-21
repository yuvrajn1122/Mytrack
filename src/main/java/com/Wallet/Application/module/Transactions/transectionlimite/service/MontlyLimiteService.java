package com.Wallet.Application.module.Transactions.transectionlimite.service;

import com.Wallet.Application.module.Transactions.transectionlimite.dto.CreateAndUpdateProductRequest;
import com.Wallet.Application.module.Transactions.transectionlimite.entity.MontlyLimite;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

public interface MontlyLimiteService {

	 public MontlyLimite setDailyLimit(BigDecimal dailyLimit,HttpServletRequest request);
    
	 public MontlyLimite setMonthlyLimit( BigDecimal monthlyLimit,HttpServletRequest request);

	 public MontlyLimite getLimiteByUserId(HttpServletRequest request);
	 //public MontlyLimite setProductDetails( CreateAndUpdateProductRequest updateProductRequest,   HttpServletRequest request);
    
    boolean canUserSpend(Long userId, BigDecimal amount);
}
