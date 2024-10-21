package com.Wallet.Application.module.AccountSettings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.module.AccountSettings.Dto.AccountSettingsRequest;
import com.Wallet.Application.module.AccountSettings.Dto.ChangePasswordRequest;
import com.Wallet.Application.module.AccountSettings.service.UpdateAccountDetailsservice;
import com.Wallet.Application.module.AccountSettings.Dto.CheckPreviousPasswordRequest;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/accountsettings")
public class AccountsettingsController {

    // Logger to track the behavior and log necessary information
    private static final Logger logger = LoggerFactory.getLogger(AccountsettingsController.class);
    
    // Autowiring the service responsible for updating user account details
    @Autowired
    private UpdateAccountDetailsservice accountService;

    /**
     * Endpoint to update user account settings.
     * @param userDetails - the user account details being updated
     * @param token - the token or request info (usually for authentication)
     * @return ResponseEntity<User> - the updated user object wrapped in the ResponseEntity
     */
    @PutMapping("/updateusers")
    public ResponseEntity<User> updateUser(@RequestBody AccountSettingsRequest userDetails, HttpServletRequest token) {
        logger.info("Received request to update account details.");
        
        // Service call to update account details
        User updatedUser = accountService.updateAccountDetails(userDetails, token);
        
        logger.info("Successfully updated account details for user.");
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint to check if the provided password matches the previous password.
     * @param checkPreviousPasswordRequest - request object containing the password details to be checked
     * @param request - HttpServletRequest containing the user token
     * @return ResponseEntity<BankResponse> - a response indicating whether the password check was successful
     */
    @PostMapping("/check-previous-password")
    public BankResponse checkPreviousPassword(
            @RequestBody CheckPreviousPasswordRequest checkPreviousPasswordRequest,
            HttpServletRequest request) {

        logger.info("Received request to check previous password for user.");
        
        try {
            // Call the service to check previous password and process the request
            BankResponse response = accountService.checkPreviousPassword(checkPreviousPasswordRequest, request);
            
            logger.info("Password check successful for user.");
            return response;
        } catch (RuntimeException ex) {
            // Log the error and return an appropriate response when a password mismatch or error occurs
            logger.error("Error occurred while checking previous password: {}", ex.getMessage());
            
            return 
                BankResponse.builder()
                            .responseCode("004")
                            .responseMessage("Password mismatch or user not found.")
                            .build();
        }
    }
    
    
    /**
     * Endpoint to update the user's password.
     *
     * @param changePasswordRequest Contains the new password and confirmation password.
     * @param request The HTTP request containing user authentication details.
     * @return ResponseEntity with a success message or error.
     */
    @PutMapping("/change-password")
    public ResponseEntity<BankResponse> updatePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) {

        // Call the service method to update the password and return the response
        BankResponse response = accountService.updateNewPassword(changePasswordRequest, request);
        
        return ResponseEntity.ok(response);
    }
}
