package com.Wallet.Application.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wallet.Application.Dto.BankResponse;
import com.Wallet.Application.Dto.CreditDebitRequest;
import com.Wallet.Application.Dto.EnquiryRequest;
import com.Wallet.Application.Dto.UserRequest;
import com.Wallet.Application.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    
    
    

    @PostMapping("/register/user")
    public BankResponse  createUser(@RequestBody @Valid UserRequest userDTO) {
    	
    	return userService.createUser(userDTO);
    }

    @PutMapping("/creditAmount")
    public BankResponse creditAmount( @RequestBody CreditDebitRequest creditDebitRequest,HttpServletRequest token ) {
    	
             
        return userService.creditAccount(creditDebitRequest,token);
    }



    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
    	return userService.balanceEnquiry(enquiryRequest);
       
    }
    
    @PutMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest,HttpServletRequest request) {
    	return userService.debitAccount(creditDebitRequest,request);
    }
    
    @GetMapping("/details")
    public BankResponse getAccountDetails(HttpServletRequest request) {
    	
    	return userService.getAccountDetails(request);
    	
    }
}
