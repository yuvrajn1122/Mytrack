package com.Wallet.Application.module.Refferel.refferalDto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReferralCodeResponse {
	 private Long ReferralId;
    private String code;
    private boolean isActive;
    private LocalDateTime createdOn;
    private String responseCode;
	
	private String responseMessage;
}
