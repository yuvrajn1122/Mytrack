package com.Wallet.Application.ServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Dto.AccountInfo;
import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Dto.CreditDebitRequest;
import com.Wallet.Application.Dto.EmailDetails;
import com.Wallet.Application.Dto.EnquiryRequest;
import com.Wallet.Application.Dto.UserDetailsResponse;
import com.Wallet.Application.Dto.UserRequest;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.Service.EmailService;
import com.Wallet.Application.Service.UserService;
import com.Wallet.Application.module.Refferel.Entity.ReferralCode;
import com.Wallet.Application.module.Refferel.Repository.ReferralCodeRepository;
import com.Wallet.Application.module.Refferel.refferalDto.GenerateReferralCodeRequest;
import com.Wallet.Application.module.Refferel.refferalDto.ReferralCodeResponse;
import com.Wallet.Application.module.Refferel.service.ReferralCodeService;
import com.Wallet.Application.module.Transactions.transectionhoistoty.TransactionHistory;
import com.Wallet.Application.module.Transactions.transectionhoistoty.TransactionHistoryRepository;
import com.Wallet.Application.module.Transactions.transectionhoistoty.TransectionHistoryRequest;
import com.Wallet.Application.module.security.authentication.TokenUtil;
import com.Wallet.Application.module.security.role.Role;
import com.Wallet.Application.utils.AccountUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	
	
	 private final PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountUtils accountUtils;

	@Autowired
	private EmailService emailService;
	
	@Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
	@Autowired
	private ReferralCodeService referralCodeService;
	@Autowired
	private ReferralCodeRepository referralCodeRepository;
	
	 private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	
	/**
	 * Creating the user and saving in tha db check if the user have already have an
	 * account
	 * 
	 */

	  @Override
	  public BankResponse createUser(UserRequest userRequest) {

	      // Check if the email already exists
	      if (userRepository.existsByEmail(userRequest.getEmail())) {
	          return BankResponse.builder()
	                  .responseCode(accountUtils.ACCOUNT_EXISTS_CODE)
	                  .responseMessage(accountUtils.ACCOUNT_EXISTS_MESSAGE)
	                  .accountInfo(null)
	                  .build();
	      }	

	      // Retrieve the referral code from the repository
	      String referralCodeValue = userRequest.getReferralIdentyCode();
	      ReferralCode referralCode = null;

	      // Check if the referral code is provided and valid
	      if (referralCodeValue != null && !referralCodeValue.isEmpty()) {
	          referralCode = referralCodeRepository.findByReferralIdentyCode(referralCodeValue);
	      }

	      // Build the new User object
	      String email = userRequest.getEmail().trim().toLowerCase();
	      User newUser = User.builder()
	              .firstName(userRequest.getFirstName())
	              .lastName(userRequest.getLastName())
	              .password(passwordEncoder.encode(userRequest.getPassword()))
	              .role(Role.USER)
	              .accountNumber(accountUtils.generateAccountNumber())
	              .accountBalance(BigDecimal.ZERO)
	              .email(email)
	              .phoneNumber(userRequest.getPhoneNumber())
	              .referralId(referralCode != null ? referralCode.getReferralId() : null) // Optional handling
	              .status("ACTIVE")
	              .isActive(true)
	              .emailVerified(false)
	              .phoneNumberVerified(false)
	              .build();

	      // Save the new user
	      User savedUser = userRepository.save(newUser);

	      // Generate referral code for the new user
	      GenerateReferralCodeRequest codeRequest = GenerateReferralCodeRequest.builder()
	              .userId(savedUser.getUserId())
	              .build();
	      referralCodeService.generateReferralCode(codeRequest);

	      // Prepare email details
	      EmailDetails emailDetails = EmailDetails.builder()
	              .recipient(savedUser.getEmail())
	              .subject("ACCOUNT CREATION")
	              .messageBody("Dear " + savedUser.getFirstName() + "\n\n" +
	                           "Congratulations! Your account has been created successfully.\n\n" +
	                           "Thank you for joining us. We are excited to have you on board!\n\n" +
	                           "Account Number: " + savedUser.getAccountNumber() + "\nBest regards,\n" +
	                           "The Wallet Application Team")
	              .build();
	      emailService.sendEmailAlert(emailDetails);

	      // Return the bank response
	      return BankResponse.builder()
	              .responseCode(accountUtils.ACCOUNT_CREATION_CODE)
	              .responseMessage(accountUtils.ACCOUNT_CREATION_MESSAGE)
	              .accountInfo(AccountInfo.builder()
	                      .accoutBalance(savedUser.getAccountBalance())
	                      .accountNumber(savedUser.getAccountNumber())
	                      .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
	                      .build())
	              .build();
	  }

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		Boolean existsByAaccountNumber = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if (!existsByAaccountNumber) {
			BankResponse bankResponse = BankResponse.builder().responseCode(accountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(accountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
			return bankResponse;

		}

		User userFoundByAccountNumber = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		BankResponse userFound = BankResponse.builder()

				.responseCode(accountUtils.ACCOUNT_FOUND_CODE).responseMessage(accountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userFoundByAccountNumber.getFirstName() + userFoundByAccountNumber.getLastName())
						.accountNumber(userFoundByAccountNumber.getAccountNumber())
						.accoutBalance(userFoundByAccountNumber.getAccountBalance()).build())
				.build();

		return userFound;
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest, HttpServletRequest request) {
	    // Find the user by account number in one query
	    User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
	    
	    
	    // Extract email from the token
	    String userEmail = TokenUtil.extractEmail(request);
	    if (userToCredit == null) {
	        return BankResponse.builder()
	                .responseCode(accountUtils.ACCOUNT_NOT_EXIST_CODE)
	                .responseMessage(accountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    // Validate credit amount (must be positive)
	    if (creditDebitRequest.getAmmount() == null || creditDebitRequest.getAmmount().compareTo(BigDecimal.ZERO) <= 0) {
	        return BankResponse.builder()
	                .responseCode(accountUtils.INVALID_AMOUNT_CODE)
	                .responseMessage(accountUtils.INVALID_AMOUNT_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    // Update account balance
	    userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmmount()));
	    userRepository.save(userToCredit);

	    
	    
	    TransectionHistoryRequest transectionHistoryRequest=TransectionHistoryRequest.builder()
	    		.amount(creditDebitRequest.getAmmount())
	    		.createdOn(LocalDateTime.now())
	    		.status("COMPLITED")
	    		.transactionNumber(generateTransactionNumber())
	    		.transactionType("CREDIT")
	    		.userId(extractUserId(userEmail))
	    		.build();
	    TransactionHistory transactionHistory=   TransactionHistory.builder()
	    		.amount(transectionHistoryRequest.getAmount())
	    		.createdOn(transectionHistoryRequest.getCreatedOn())
	    		.status(transectionHistoryRequest.getStatus())
	    		.transactionNumber(transectionHistoryRequest.getTransactionNumber())
	    		.transactionType(transectionHistoryRequest.getTransactionType())
	    		.userId(transectionHistoryRequest.getUserId())
	    		.build();
	    transactionHistoryRepository.save(transactionHistory);
	    // Send email asynchronously to reduce blocking time
	    emailService.sendEmailAlert(EmailDetails.builder()
	            .recipient(userToCredit.getEmail())
	            .subject("Account Crediting Notification")
	            .messageBody("Dear " + userToCredit.getFirstName() + ",\n\n"
	                + "We are pleased to inform you that your account has been successfully credited.\n\n"
	                + "New Account Balance: " + userToCredit.getAccountBalance() + "\n\n"
	                + "Thank you for being a valued customer.\n\nBest regards,\nThe Wallet Application Team")
	            .build());

	    // Return success response
	    return BankResponse.builder()
	            .responseCode(accountUtils.ACCOUNT_CREATION_CODE)
	            .responseMessage(accountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
	            .accountInfo(AccountInfo.builder()
	                    .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
	                    .accountNumber(userToCredit.getAccountNumber())
	                    .accoutBalance(userToCredit.getAccountBalance())
	                    .build())
	            .build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest,HttpServletRequest request) {
		
 
	    
	    // Extract email from the token
	    String userEmail = TokenUtil.extractEmail(request);
		// Find the user by account number in one query
	    User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
	    
	    if (userToDebit == null) {
	        return BankResponse.builder()
	                .responseCode(accountUtils.ACCOUNT_NOT_EXIST_CODE)
	                .responseMessage(accountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }
	    
	    if(userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmmount()) < 0) {
	    	
	    	 return BankResponse.builder()
		                .responseCode(accountUtils.INSUFFICIENT_BALANCE_CODE )
		                .responseMessage(accountUtils.INSUFFICIENT_BALANCE_MESSAGE)
		                .accountInfo(AccountInfo.builder()
		                		.accountName(userToDebit.getFirstName())
		                		.accountNumber(userToDebit.getAccountNumber())
								.accoutBalance(userToDebit.getAccountBalance()).build())
		                .build();
	    	
	    }else {
	    	
	    	// Proceed with the credit or debit
	        BigDecimal newBalance = userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmmount());
	        userToDebit.setAccountBalance(newBalance);
	        userRepository.save(userToDebit);
	        
	
		    
		    
		    TransectionHistoryRequest transectionHistoryRequest=TransectionHistoryRequest.builder()
		    		.amount(creditDebitRequest.getAmmount())
		    		.createdOn(LocalDateTime.now())
		    		.status("COMPLITED")
		    		.transactionNumber(generateTransactionNumber())
		    		.transactionType("DEBIT")
		    		.userId(extractUserId(userEmail))
		    		.build();
		    TransactionHistory transactionHistory=   TransactionHistory.builder()
		    		.amount(transectionHistoryRequest.getAmount())
		    		.createdOn(transectionHistoryRequest.getCreatedOn())
		    		.status(transectionHistoryRequest.getStatus())
		    		.transactionNumber(transectionHistoryRequest.getTransactionNumber())
		    		.transactionType(transectionHistoryRequest.getTransactionType())
		    		.userId(transectionHistoryRequest.getUserId())
		    		.build();
		    transactionHistoryRepository.save(transactionHistory);
	        EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("Account Debiting Notification")
                    .messageBody("Dear"+userToDebit.getFirstName() +",\n\n"
                            + "We are writing to inform you that an amount of " + creditDebitRequest.getAmmount() + " has been debited from your account.\n\n"
                            + "New Account Balance: " + newBalance + "\n\n"
                            + "Thank you for being a valued customer.\n\nBest regards,\nThe Wallet Application Team")
                    .build();

            emailService.sendEmailAlert(emailDetails);


	        
	        return BankResponse.builder()
	                .responseCode(accountUtils.TRANSACTION_SUCCESS_CODE  )
	                .responseMessage(accountUtils.TRANSACTION_SUCCESS_MESSAGE)
	                .accountInfo(AccountInfo.builder()
	                		.accountName(userToDebit.getFirstName())
	                		.accountNumber(userToDebit.getAccountNumber())
							.accoutBalance(newBalance).build())
	                .build();
	    }
		
	    
	    
	}
	
	
	
	@Override
    public BankResponse setDailyLimit(BigDecimal dailyLimit,HttpServletRequest request) {
		
		 // Extract email from the token
	    String userEmail = TokenUtil.extractEmail(request);
	    Long userId = extractUserId(userEmail);
	    
        logger.info("Setting daily limit for user with ID: {} to {}", userId, dailyLimit);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        user.setDailyLimit(dailyLimit);
        User updatedUser = userRepository.save(user);

        BankResponse bankResponse= BankResponse.builder()
        		.responseCode(userEmail)
        		.responseMessage(userEmail)
        		.accountInfo(AccountInfo.builder()
        				.accoutBalance(updatedUser.getAccountBalance())
        				.build())
        		.build();
        logger.info("Daily limit set successfully for user with ID: {}", userId);
        return bankResponse;
    }

    @Override
    public boolean canUserSpend(Long userId,BigDecimal amount) {
        logger.info("Checking if user with ID: {} can spend an amount of {}", userId, amount);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        // Reset spentToday if the last transaction date is not today
        if (user.getLastTransactionDate() == null || !user.getLastTransactionDate().isEqual(LocalDate.now())) {
            logger.info("Resetting spentToday for user with ID: {} since it's a new day", userId);
            user.setSpentToday(BigDecimal.ZERO);
            user.setLastTransactionDate(LocalDate.now());
        }

        BigDecimal remainingAmount = user.getDailyLimit().subtract(user.getSpentToday());
        logger.info("User with ID: {} has {} remaining for the day", userId, remainingAmount);

        boolean canSpend = remainingAmount.compareTo(amount) >= 0;
        if (canSpend) {
            logger.info("User with ID: {} is allowed to spend the amount of {}", userId, amount);
        } else {
            logger.warn("User with ID: {} cannot spend the amount of {}. Exceeds daily limit.", userId, amount);
        }
        return canSpend;
    }

    @Override
    public void updateSpentAmount(Long userId, BigDecimal amount) {
        logger.info("Updating spent amount for user with ID: {} by {}", userId, amount);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", userId);
                    return new RuntimeException("User not found");
                });

        // Reset spentToday if the last transaction date is not today
        if (user.getLastTransactionDate() == null || !user.getLastTransactionDate().isEqual(LocalDate.now())) {
            logger.info("Resetting spentToday for user with ID: {} since it's a new day", userId);
            user.setSpentToday(BigDecimal.ZERO);
            user.setLastTransactionDate(LocalDate.now());
        }

        // Update the amount spent today
        user.setSpentToday(user.getSpentToday().add(amount));
        userRepository.save(user);

        logger.info("Updated spent amount for user with ID: {}. New spent today: {}", userId, user.getSpentToday());
    }
    
      

    public BankResponse getAccountDetails(HttpServletRequest request) {
        String userEmail = TokenUtil.extractEmail(request);
        Long userId = extractUserId(userEmail);
        
        // Now the logger is properly initialized and can be used
        logger.info("User id extracted from Token: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User with ID: {} not found", userId);
                return new RuntimeException("User not found");
            });
        
        BankResponse bankResponse = BankResponse.builder()
            .responseCode(accountUtils.SUCCESS_CODE)
            .responseMessage(accountUtils.USER_FOUND_MESSAGE)
            .userDetailsResponsel(UserDetailsResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build())
            .build();
        
        return bankResponse;
    }

    
    
	private static final Random random = new Random();

    public static String generateTransactionNumber() {    
        long timestamp = Instant.now().toEpochMilli();        
        int randomNumber = 1000 + random.nextInt(9000);       
        return "TXN" + timestamp + randomNumber;
    }
    public Long extractUserId(String userEmail) {
    	
    	 Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUserId();
        } else {
            return null; // Or throw an exception, or return an optional based on your design choice
        }
    }

    
}
