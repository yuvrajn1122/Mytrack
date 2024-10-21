package com.Wallet.Application.module.Refferel.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.Refferel.Entity.ReferralCode;
import com.Wallet.Application.module.Refferel.Repository.ReferralCodeRepository;
import com.Wallet.Application.module.Refferel.refferalDto.GenerateReferralCodeRequest;
import com.Wallet.Application.module.Refferel.refferalDto.ReferralCodeResponse;
import com.Wallet.Application.module.Refferel.service.ReferralCodeService;
import com.Wallet.Application.module.security.exception.ResourceNotFoundException;
import com.Wallet.Application.utils.AccountUtils;

@Service
public class ReferralCodeServiceImpl implements ReferralCodeService {

	private static final Logger logger = LoggerFactory.getLogger(ReferralCodeServiceImpl.class);
	@Autowired
	private AccountUtils accountUtils;

	private final ReferralCodeRepository referralCodeRepository;
	private final UserRepository userRepository;

	@Autowired
	public ReferralCodeServiceImpl(ReferralCodeRepository referralCodeRepository, UserRepository userRepository) {
		this.referralCodeRepository = referralCodeRepository;
		this.userRepository = userRepository;
	}

	@Override
	public ReferralCodeResponse generateReferralCode(GenerateReferralCodeRequest request) {
		logger.info("Generating referral code for user with ID: {}", request.getUserId());

		try {
			Optional<User> userOptional = userRepository.findById(request.getUserId());
			if (!userOptional.isPresent()) {
				throw new ResourceNotFoundException("User not found with ID: " + request.getUserId());
			}
			request.getUserId();

			// Generate referral code
			String code = generateCode();

			ReferralCode referralCode = ReferralCode.builder().isActive(true).createdOn(LocalDateTime.now())
					.userId(request.getUserId()).referralIdentyCode(code).build();
			referralCodeRepository.save(referralCode);
			logger.info("Referral code generated: {}", code);

			return ReferralCodeResponse.builder().ReferralId(referralCode.getReferralId()).code(code)
					.isActive(referralCode.isActive()).createdOn(referralCode.getCreatedOn())
					.responseCode(accountUtils.SUCCESS_CODE)
					.responseMessage(accountUtils.REFERRAL_CODE_GENERATED_MESSAGE).build();

		} catch (ResourceNotFoundException e) {
			logger.error("User not found with ID: {}", request.getUserId(), e);
			throw e;
		} catch (Exception e) {
			logger.error("An error occurred while generating referral code for user with ID: {}", request.getUserId(),
					e);
			throw new ResourceNotFoundException("Failed to generate referral code");
		}
	}

	public ReferralCodeResponse findByCode(String code) {
		logger.info("Finding referral code with code: {}", code);

		try {
			ReferralCode referralCode = referralCodeRepository.findByReferralIdentyCode(code);

			if (referralCode == null) {
				logger.error("Referral code not found: {}", code);
				return ReferralCodeResponse.builder().responseCode(accountUtils.INVALID_REFERRAL_CODE)
						.responseMessage(accountUtils.INVALID_REFERRAL_MESSAGE).build();
			}

			logger.info("Referral code found: {}", referralCode.getReferralIdentyCode());

			return ReferralCodeResponse.builder().ReferralId(referralCode.getReferralId())
					.responseCode(accountUtils.VALID_REFERRAL_CODE)
					.responseMessage(accountUtils.INVALID_REFERRAL_MESSAGE).build();
		} catch (ResourceNotFoundException e) {
			logger.error("Exception occurred: {}", e.getMessage(), e);
			return ReferralCodeResponse.builder().responseMessage(e.getMessage()).build();
		}
	}

	// Helper method to generate referral code
	private String generateCode() {
		Random random = new Random();
		char letter = (char) (random.nextInt(26) + 'A');
		int digits = random.nextInt(10000);
		return String.format("D00%s%04d", letter, digits);
	}

	@Override
	public void deactivateReferralCode(String code) {
		// TODO Auto-generated method stub

	}

}
