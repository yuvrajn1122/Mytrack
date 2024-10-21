package com.Wallet.Application.module.Refferel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wallet.Application.module.Refferel.Entity.ReferralCode;

import java.util.List;

@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Long> {
	ReferralCode findByReferralIdentyCode(String referralIdentyCode);
    List<ReferralCode> findByIsActive(boolean isActive);
}
