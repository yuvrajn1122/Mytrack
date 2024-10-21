package com.Wallet.Application.Service;

import java.math.BigDecimal;

import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Dto.CreditDebitRequest;
import com.Wallet.Application.Dto.EnquiryRequest;
import com.Wallet.Application.Dto.UserRequest;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.module.security.exception.MasterException;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	
	BankResponse createUser(UserRequest userDTO);
	
	BankResponse balanceEnquiry(EnquiryRequest  enquiryRequest); 
	
	public String nameEnquiry(EnquiryRequest enquiryRequest);
	
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest, HttpServletRequest token );
	
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest,HttpServletRequest request );
	BankResponse getAccountDetails(HttpServletRequest request );

	public BankResponse setDailyLimit(BigDecimal dailyLimit,HttpServletRequest request);
    boolean canUserSpend(Long userId, BigDecimal amount);
    void updateSpentAmount(Long userId, BigDecimal amount);
//    UserRequest updateUser(Long userId, UserRequest userDTO);
//
//    void deleteUser(Long userId);
//
//    Optional<UserRequest> getUserById(Long userId);
//
//    List<UserRequest> getAllUsers();
}
