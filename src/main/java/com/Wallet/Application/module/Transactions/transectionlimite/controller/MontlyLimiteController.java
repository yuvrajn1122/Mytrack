package com.Wallet.Application.module.Transactions.transectionlimite.controller;

import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsRequest;
import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsResponse;
import com.Wallet.Application.module.Transactions.productTransectionLimite.service.ProductDetailsService;
import com.Wallet.Application.module.Transactions.transectionlimite.dto.CreateAndUpdateProductRequest;
import com.Wallet.Application.module.Transactions.transectionlimite.dto.DailyLimitRequest;
import com.Wallet.Application.module.Transactions.transectionlimite.dto.MonthlyLimitRequest;
import com.Wallet.Application.module.Transactions.transectionlimite.entity.MontlyLimite;

import com.Wallet.Application.module.Transactions.transectionlimite.service.MontlyLimiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/limite")
public class MontlyLimiteController {

    private static final Logger logger = LoggerFactory.getLogger(MontlyLimiteController.class);

    @Autowired
    private MontlyLimiteService montlyLimiteService;
    
    @Autowired
    private ProductDetailsService productDetailsService;

    // Set daily limit for the user
    @PutMapping("/daily-limit")
    public ResponseEntity<MontlyLimite> setDailyLimit(@RequestBody DailyLimitRequest request, HttpServletRequest httpRequest) {
        logger.info("Received request to set daily limit");
        MontlyLimite limite = montlyLimiteService.setDailyLimit(request.getDailyLimit(), httpRequest);
        logger.info("Successfully set daily limit");
        return ResponseEntity.ok(limite);
    }

    // Set monthly limit for the user
    @PutMapping("/monthly-limit")
    public ResponseEntity<MontlyLimite> setMonthlyLimit(@RequestBody MonthlyLimitRequest request, HttpServletRequest httpRequest) {
        logger.info("Received request to set monthly limit");
        MontlyLimite limite = montlyLimiteService.setMonthlyLimit(request.getMonthlyLimit(), httpRequest);
        logger.info("Successfully set monthly limit");
        return ResponseEntity.ok(limite);
    }

    // Get limits for the user
    @GetMapping("/limits")
    public ResponseEntity<MontlyLimite> getUserLimits(HttpServletRequest request) {
        logger.info("Received request to get limits");
        MontlyLimite limite = montlyLimiteService.getLimiteByUserId(request);
        logger.info("Successfully retrieved limits");
        return ResponseEntity.ok(limite);
    }

    // Check if the user can spend the given amount
    @GetMapping("/can-spend")
    public ResponseEntity<Boolean> canUserSpend(@RequestParam BigDecimal amount, @RequestParam Long userId) {
        logger.info("Received request to check if userId: {} can spend amount: {}", userId, amount);
        boolean canSpend = montlyLimiteService.canUserSpend(userId, amount);
        logger.info("UserId: {} can spend: {}", userId, canSpend);
        return ResponseEntity.ok(canSpend);
    }
    
    /**
     * Endpoint to create a new product.
     * 
     * @param productDetailsRequest The request body containing product details.
     * @param request               The HTTP servlet request to extract additional information (like userId).
     * @return A response entity containing the product creation status.
     */
    @PostMapping("/set-product-limite")
    public ProductDetailsResponse createProduct(
            @RequestBody ProductDetailsRequest productDetailsRequest,
            HttpServletRequest request) {

        logger.info("Received request to create a product with name: {}", productDetailsRequest.getProductName());

        // Call service to create the product
        ProductDetailsResponse productDetailsResponse = productDetailsService.createProduct(productDetailsRequest, request);

        // Log the result
        logger.info("Product created successfully with status code: {}", productDetailsResponse.getStatusCode());

        // Return response entity
        return productDetailsResponse;
    }
    /**
     * Updates an existing product.
     * 
     * @param productDetailsRequest The details of the product to be updated.
     * @param request The HTTP servlet request.
     * @return ResponseEntity containing the status and message.
     */
    @PutMapping("/update-product-limite")
    public ProductDetailsResponse updateProduct(@RequestBody ProductDetailsRequest productDetailsRequest, HttpServletRequest request) {
        ProductDetailsResponse response = productDetailsService.updateProduct(productDetailsRequest, request);
        return response;
    }
    
//    
// // Set product limit and name for the user
//    @PostMapping("/set-product-limite")
//    public ResponseEntity<MontlyLimite> setProductLimite(
//            @RequestBody CreateAndUpdateProductRequest updateProductRequest, HttpServletRequest request) {
//        logger.info("Received request to set product details");
//        MontlyLimite limite = montlyLimiteService.setProductDetails(
//        		updateProductRequest, request);
//        logger.info("Successfully set product details");
//        return ResponseEntity.ok(limite);
//    }
}
