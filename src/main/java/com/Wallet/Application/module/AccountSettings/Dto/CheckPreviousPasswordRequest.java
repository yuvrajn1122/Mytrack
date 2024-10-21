package com.Wallet.Application.module.AccountSettings.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckPreviousPasswordRequest {
	
	String currentPassword;

}
