package com.Wallet.Application.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Wallet.Application.Dto.OtpRequest;
import com.Wallet.Application.Dto.OtpResponse;
import com.Wallet.Application.Service.OtpService;
import com.Wallet.Application.utils.AccountUtils;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@Autowired
	private AccountUtils accountUtils;

	@PostMapping("/generate")
	public OtpResponse generateOtp(@RequestBody OtpRequest otpRequest) {
		if (otpRequest.getEmail() == null) {
			return OtpResponse.builder().responseCode(accountUtils.EMAIL_NOT_FOUND_CODE)
					.responseMessage(accountUtils.EMAIL_NOT_FOUND).build();
		}
		String otp = otpService.generateOtp(otpRequest.getEmail());
		return OtpResponse.builder()
				.responseCode(accountUtils.OTP_GENERATED_CODE)
				.responseMessage(accountUtils.OTP_GENERATED_SUCCESS)
				.build();
	}

	@PostMapping("/validate")
	public OtpResponse validateOtp(@RequestBody OtpRequest otpRequest) {
		boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
		if (isValid) {
			return OtpResponse.builder()
					.responseCode(accountUtils.OTP_VALIDATED_CODE)
					.responseMessage(accountUtils.OTP_VALIDATED_SUCCESS)
					.build();
		} else {
			return OtpResponse.builder()
					.responseCode(accountUtils.OTP_INVALID_CODE)
					.responseMessage(accountUtils.OTP_INVALID_MESSAGE)
					.build();
		}
	}
}
