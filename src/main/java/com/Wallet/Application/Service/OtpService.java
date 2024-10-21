package com.Wallet.Application.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Dto.EmailDetails;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;

@Service
public class OtpService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;
	
	private static final int EXPIRATION_TIME = 5; // minutes
	private static final int OTP_LENGTH = 6;
	private final Random random = new Random();

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public String generateOtp(String Email) {

		String otp = String.format("%06d", random.nextInt(999999));

		redisTemplate.opsForValue().set(Email, otp, EXPIRATION_TIME, TimeUnit.MINUTES);

	     if( userRepository.existsByEmail(Email)) {
	    	
	    	 
	    	 EmailDetails emailDetails = EmailDetails.builder()
	    			    .recipient(Email) 
	    			    .subject("Your OTP for Secure Access")
	    			    .messageBody( "Dear "+"user"+",\n\n"
	    			        + "We have received a request for authentication using One-Time Password (OTP).\n\n"
	    			        + "Your OTP is: " + otp + "\n\n" 
	    			        + "This OTP is valid for 5 minutes. Please do not share this OTP with anyone.\n\n"
	    			        + "If you did not request this, please contact our support team immediately.\n\n"
	    			        + "Best regards,\n"
	    			        + "The Wallet Application Team")
	    			    .build();

	    			emailService.sendEmailAlert(emailDetails);
	    	 
	     }
		return otp;
	}

	public boolean validateOtp(String Email, String otp) {

		String storedOtp = redisTemplate.opsForValue().get(Email);

		if (storedOtp != null && storedOtp.equals(otp)) {

			redisTemplate.delete(Email);
			return true;
		}

		return false;
	}
}