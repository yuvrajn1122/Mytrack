package com.Wallet.Application.module.AccountSettings.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class AccountSettingsRequest {
	
	private String otherName;
	
	private String gender;
	
	private String address;

	private String stateOfOrigin;
	
	private String alternativePhoneNumber;

}
