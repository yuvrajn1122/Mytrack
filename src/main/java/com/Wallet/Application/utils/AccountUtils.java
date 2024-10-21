package com.Wallet.Application.utils;

import java.time.Year;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class AccountUtils {

	
	
	public static final String ACCOUNT_EXISTS_CODE ="001";
	
	public static final String ACCOUNT_EXISTS_MESSAGE="This user already has an account Created!";
	
	public static final String ACCOUNT_CREATION_CODE="002";
	
	public static final String ACCOUNT_CREATION_MESSAGE="Account has been successfully created !";
	
	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account number does not exist.";

    public static final String ACCOUNT_FOUND_CODE = "004"; 
    public static final String ACCOUNT_FOUND_MESSAGE = "User with the provided account number exists.";

    public static final String ACCOUNT_CREDIT_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Successfully credited an amount of %.2f. Your new balance is %.2f.";
    public static final String INVALID_AMOUNT_CODE  = "006";
    public static final String INVALID_AMOUNT_MESSAGE ="Invalid credit amount. Amount must be greater than zero.";
	
    public static final String OTP_GENERATED_CODE= "007";
    public static final String  OTP_VALIDATED_CODE= "008";
    public static final String OTP_INVALID_CODE= "009";
    public static final String OTP_NOT_FOUND_CODE= "010";
    public static final String EMAIL_NOT_FOUND_CODE= "011";
    public static final String OTP_GENERATED_SUCCESS = "OTP generated and sent to email.";
    public static final String OTP_VALIDATED_SUCCESS = "OTP validated successfully.";
    public static final String OTP_INVALID_MESSAGE = "Invalid OTP.";
    public static final String OTP_NOT_FOUND = "No OTP found for the provided email.";
    public static final String EMAIL_NOT_FOUND = "Email not found.";
    
 // Response Codes
    public static final String INSUFFICIENT_BALANCE_CODE = "012";
    public static final String TRANSACTION_SUCCESS_CODE = "013";

    // Response Messages
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance for this transaction.";
    public static final String TRANSACTION_SUCCESS_MESSAGE = "Transaction completed successfully.";
    
    public static final String ERROR_CODE="404";
    
    public static final String INVALID_REFERRAL_CODE="014";
    
    public static final String INVALID_REFERRAL_MESSAGE="Referral code not found";
    
    public static final String VALID_REFERRAL_CODE="015";
    
    public static final String VALID_REFERRAL_MESSAGE="Referral code found successfully.";
    
    public static final String SUCCESS_CODE = "200"; 
    public static final String SUCCESS_CODE_MESSAGE="Details updated successfully";
    public static final String REFERRAL_CODE_GENERATED_MESSAGE = "Referral code generated successfully: ";
    public static final String REFERRAL_CODE_ERROR_MESSAGE = "Error generating referral code.";
    
 
    public static final String USER_FOUND_MESSAGE = "User Found Sucessfully";
    
    public static final String PASSWORD_CHANGE_FAILED_CODE="016";
    
    public static final String PASSWORD_CHANGE_SUCCESS = "Password changed successfully!";
    public static final String PASSWORD_MARCH_SUCCESS = "Password match successfully!";
    public static final String PASSWORD_CHANGE_FAILED = "Password change failed. Please try again.";
    public static final String ACCOUNT_NOT_FOUND = "Account not found. Please verify your details.";

    
    
    
    
    
    
    
    
    
    
    
    
    
	/**
	 * 2024 + rsndomSixDigits
	 */
	private Year currentYear = Year.now();
    private int min = 100000;
    private int max = 999999;

    public String generateAccountNumber() {
       
        Random random = new Random();
        int randomNumber = random.nextInt(max - min + 1) + min;

       
        String year = String.valueOf(currentYear.getValue()); 
        String number = String.valueOf(randomNumber);
        
        StringBuffer combinedString = new StringBuffer();
        combinedString.append(year);
        combinedString.append(number);

        return combinedString.toString(); 
    }

	 
	 
	 
	
	
	
	
}
