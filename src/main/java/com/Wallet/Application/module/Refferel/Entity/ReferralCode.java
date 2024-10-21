package com.Wallet.Application.module.Refferel.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
	@Table(name = "apna_referral_codes")
	public class ReferralCode {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long ReferralId;

	    @Column(nullable = false, unique = true)
	    private String referralIdentyCode;

	    @Column(nullable = false, unique = true)
	    private Long  userId;

	    @Column(nullable = false)
	    private LocalDateTime createdOn;

	    @Column(nullable = false)
	    private boolean isActive;
	    
	   

}
