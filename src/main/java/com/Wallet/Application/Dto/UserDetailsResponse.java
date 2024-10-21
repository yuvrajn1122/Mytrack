package com.Wallet.Application.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor@Builder

public class UserDetailsResponse {
	
	private String firstName;

	private String lastName;
	
	private String email;
	
	private String phoneNumber;

	private Boolean phoneNumberVerified;
	private Boolean emailVerified;
	

}
