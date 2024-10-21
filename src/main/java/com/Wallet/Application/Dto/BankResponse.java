package com.Wallet.Application.Dto;

import com.Wallet.Application.module.security.authentication.AuthResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
	
	private String responseCode;
	
	private String responseMessage;
	
	private AccountInfo  accountInfo;
	
	private AuthResponse authResponse;
	
	private UserDetailsResponse userDetailsResponsel;

}
