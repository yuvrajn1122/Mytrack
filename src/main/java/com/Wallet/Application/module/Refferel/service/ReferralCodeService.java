package com.Wallet.Application.module.Refferel.service;




import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.module.Refferel.refferalDto.GenerateReferralCodeRequest;
import com.Wallet.Application.module.Refferel.refferalDto.ReferralCodeResponse;

public interface ReferralCodeService {
	ReferralCodeResponse generateReferralCode(GenerateReferralCodeRequest request); // Generate and return a referral code for a user
    void deactivateReferralCode(String code);  // Deactivate a referral code
    public ReferralCodeResponse findByCode(String code);
}
