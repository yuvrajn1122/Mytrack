package com.Wallet.Application.module.AccountSettings.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Dto.AccountInfo;
import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.AccountSettings.Dto.AccountSettingsRequest;
import com.Wallet.Application.module.AccountSettings.Dto.ChangePasswordRequest;
import com.Wallet.Application.module.AccountSettings.Dto.CheckPreviousPasswordRequest;
import com.Wallet.Application.module.AccountSettings.service.UpdateAccountDetailsservice;
import com.Wallet.Application.module.security.authentication.TokenUtil;
import com.Wallet.Application.utils.AccountUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UpdateAccountdetailsServiceImpl implements UpdateAccountDetailsservice {
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final AccountUtils accountUtils;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public UpdateAccountdetailsServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,
			AccountUtils accountUtils) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.accountUtils = accountUtils;
	}

	@Override
	public User updateAccountDetails(AccountSettingsRequest userDetails, HttpServletRequest request) {

		String userEmail = TokenUtil.extractEmail(request);
		Optional<User> optionalUser = userRepository.findByEmail(userEmail);

		User existingUser = userRepository.findById(extractUserId(optionalUser))
				.orElseThrow(() -> new RuntimeException("User not found"));

		existingUser.setOtherName(userDetails.getOtherName());
		existingUser.setGender(userDetails.getGender());
		existingUser.setAddress(userDetails.getAddress());
		existingUser.setStateOfOrigin(userDetails.getStateOfOrigin());
		existingUser.setAlternativePhoneNumber(userDetails.getAlternativePhoneNumber());

		return userRepository.save(existingUser);
	}

	public Long extractUserId(Optional<User> optionalUser) {
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getUserId();
		} else {
			return null; // Or throw an exception, or return an optional based on your design choice
		}
	}

	/**
	 * Checks if the provided current password matches the user's existing password.
	 * 
	 * This method retrieves the user's email from the token, locates the user in
	 * the database, and compares the provided password against the stored password.
	 * If the passwords match, a successful response is returned; otherwise, an
	 * exception is thrown.
	 */
	@Override
	public BankResponse checkPreviousPassword(CheckPreviousPasswordRequest checkPreviousPasswordRequest,
			HttpServletRequest request) {
		String userEmail = TokenUtil.extractEmail(request);
		logger.info("Extracted email from token: {}", userEmail);

		Optional<User> optionalUser = userRepository.findByEmail(userEmail);

		if (!optionalUser.isPresent()) {
			logger.error("User not found with email: {}", userEmail);
			throw new RuntimeException("User not found with email: " + userEmail);
		}

		User existingUser = optionalUser.get();
		logger.info("User found with email: {} and ID: {}", userEmail, existingUser.getUserId());

		String rawCurrentPassword = checkPreviousPasswordRequest.getCurrentPassword();
		logger.debug("Raw current password received for user ID: {}", existingUser.getUserId());
		System.out.println("Raw current password: " + rawCurrentPassword); // Caution: Log sensitive information only in
																			// development

		String existingPassword = existingUser.getPassword();
		logger.debug("Existing password retrieved from database for user ID: {}", existingUser.getUserId());

		boolean isPasswordMatch = passwordEncoder.matches(rawCurrentPassword, existingPassword);

		if (isPasswordMatch) {
			logger.info("Current password matches the previous password for user ID: {}", existingUser.getUserId());
		} else {
			logger.warn("Current password does not match the previous password for user ID: {}",
					existingUser.getUserId());
			throw new RuntimeException("Password mismatch");
		}

		logger.info("Password check successful for user ID: {}", existingUser.getUserId());
		return BankResponse.builder().responseCode(accountUtils.SUCCESS_CODE)
				.responseMessage(accountUtils.PASSWORD_MARCH_SUCCESS).build();
	}

	/**
	 * Updates the user's password.
	 * 
	 * This method retrieves the user's email from the request token, checks if the
	 * user exists, and updates the password if the user is found. It logs the
	 * process and checks whether the password change was successful. If the user is
	 * not found or the update fails, an exception is thrown.
	 */
	@Override
	public BankResponse updateNewPassword(ChangePasswordRequest currentPassword, HttpServletRequest request) {

		String userEmail = TokenUtil.extractEmail(request);
		logger.info("Extracted email from token: {}", userEmail);

		Optional<User> optionalUser = userRepository.findByEmail(userEmail);

		if (!optionalUser.isPresent()) {
			logger.error("User not found with email: {}", userEmail);
			throw new RuntimeException("User not found with email: " + userEmail);
		}

		User existingUser = optionalUser.get();
		logger.info("User found with email: {} and ID: {}", userEmail, existingUser.getUserId());

		String newPassword = currentPassword.getConformPassword();
		logger.debug("New password received for user ID: {}", existingUser.getUserId());

		existingUser.setPassword(passwordEncoder.encode(newPassword));
		logger.info("Attempting to update password for user ID: {}", existingUser.getUserId());

		User updatedUser = userRepository.save(existingUser);

		if (updatedUser.getPassword().equals(existingUser.getPassword())) {
			logger.info("Password updated successfully for user ID: {}", existingUser.getUserId());
		} else {
			return BankResponse.builder().responseCode(accountUtils.PASSWORD_CHANGE_FAILED_CODE)
					.responseMessage(accountUtils.PASSWORD_CHANGE_FAILED).build();
		}

		return BankResponse.builder().responseCode(accountUtils.SUCCESS_CODE)
				.responseMessage(accountUtils.PASSWORD_CHANGE_SUCCESS).build();
	}

}
