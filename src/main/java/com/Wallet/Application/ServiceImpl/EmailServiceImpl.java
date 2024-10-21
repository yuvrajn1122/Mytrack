package com.Wallet.Application.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Dto.EmailDetails;
import com.Wallet.Application.Service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	
	@Autowired
    JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sendEmail;
	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		// Create a SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Set the email properties
        message.setFrom(sendEmail); 
        message.setTo(emailDetails.getRecipient()); 
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getMessageBody()); 
        
        // Send the email
        javaMailSender.send(message);
        
        System.out.println("Mail sent SuccessFully!!!");
	}

}
