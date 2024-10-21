package com.Wallet.Application.module.AccountSettings.service;

import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.module.AccountSettings.Dto.AccountSettingsRequest;
import com.Wallet.Application.module.AccountSettings.Dto.ChangePasswordRequest;
import com.Wallet.Application.module.AccountSettings.Dto.CheckPreviousPasswordRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UpdateAccountDetailsservice {
	
	User updateAccountDetails( AccountSettingsRequest userDetails,HttpServletRequest token);
	
	BankResponse checkPreviousPassword(CheckPreviousPasswordRequest checkPreviousPasswordRequest,HttpServletRequest request);
	
	BankResponse updateNewPassword(ChangePasswordRequest currentPassword,HttpServletRequest request);

}
