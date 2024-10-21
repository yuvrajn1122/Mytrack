package com.Wallet.Application.module.Transactions.productTransectionLimite.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsRequest;
import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsResponse;
import com.Wallet.Application.module.Transactions.productTransectionLimite.entity.ProductDetails;
import com.Wallet.Application.module.Transactions.productTransectionLimite.repository.ProductDetailsRepository;
import com.Wallet.Application.module.Transactions.productTransectionLimite.service.ProductDetailsService;
import com.Wallet.Application.module.security.authentication.TokenUtil;
import com.Wallet.Application.utils.AccountUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
	
	
   
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailsServiceImpl.class);
    private final ProductDetailsRepository productDetailsRepository;
	private UserRepository userRepository;
	private AccountUtils accountUtils;
	
   
    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository,UserRepository userRepository) {
        this.productDetailsRepository = productDetailsRepository;
        this.userRepository=userRepository;
        this. accountUtils=accountUtils;
    }

    /**
     * Creates a new product and sets the default active status.
     * 
     * @param productDetails The details of the product to be created.
     * @return The created ProductDetails object.
     */
    @Override
    public ProductDetailsResponse createProduct(ProductDetailsRequest  productDetailsRequest,HttpServletRequest request) {
    	Long userId = extractUserId( request);
    	
         ProductDetails  productDetails=ProductDetails.builder()
    	.updatedon(LocalDate.now())
    	.productName(productDetailsRequest.getProductName())
    	.productLimite(productDetailsRequest.getProductLimite())
    	.isActive(true)
    	.userId(userId)
    	.build();
        logger.info("Creating product: {}", productDetails);
       productDetailsRepository.save(productDetails);
       ProductDetailsResponse productDetailsResponse= ProductDetailsResponse.builder()
       .statusCode(accountUtils.SUCCESS_CODE)
       .message(accountUtils.SUCCESS_CODE_MESSAGE)
       .build();
       return productDetailsResponse;
    }

    /**
     * Updates an existing product by ID.
     * 
     * @param productDetailsRequest The new details for the product.
     * @param request The HTTP servlet request containing user information.
     * @return The response containing the status and message.
     * @throws RuntimeException if the product is not found.
     */
    @Override
    public ProductDetailsResponse updateProduct(ProductDetailsRequest productDetailsRequest, HttpServletRequest request) {
        Long userId = extractUserId(request);     
        logger.info("User Id Extracted from token: {}", userId);

        // Fetch products for the user
        List<ProductDetails> byUserId = productDetailsRepository.findByUserId(userId);
        logger.info("Fetched {} products for userId: {}", byUserId.size(), userId);

        // Check if any product matches the requested product name
        Optional<ProductDetails> productToUpdate = byUserId.stream()
                .filter(product -> product.getProductName().equals(productDetailsRequest.getProductName()))
                .findFirst();

        if (productToUpdate.isPresent()) {
            ProductDetails existingProduct = productToUpdate.get();
            existingProduct.setProductLimite(productDetailsRequest.getProductLimite());
            existingProduct.setUpdatedon(LocalDate.now());

            logger.info("Updating product: {}", existingProduct);
            productDetailsRepository.save(existingProduct);

            return ProductDetailsResponse.builder()
                    .statusCode(accountUtils.SUCCESS_CODE)
                    .message("Product updated successfully.")
                    .build();
        } else {
            logger.warn("No product found with the name: {}", productDetailsRequest.getProductName());
            return ProductDetailsResponse.builder()
                    .statusCode(accountUtils.ERROR_CODE)
                    .message("Create product entered product NotFound")
                    .build();
        }
    }


    /**
     * Soft deletes a product by setting its active status to false.
     * 
     * @param id The ID of the product to be soft deleted.
     * @throws RuntimeException if the product is not found.
     */
    @Override
    public void softDeleteProduct(Long id) {
        logger.info("Soft deleting product with ID: {}", id);
        Optional<ProductDetails> existingProduct = productDetailsRepository.findById(id);
        if (existingProduct.isPresent()) {
            ProductDetails productDetails = existingProduct.get();
            productDetails.setIsActive(false); // Soft delete
            productDetailsRepository.save(productDetails);
            logger.info("Product soft deleted: {}", productDetails);
        } else {
            logger.error("Product not found with ID: {}", id);
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    /**
     * Retrieves all active products.
     * 
     * @return A list of all active ProductDetails objects.
     */
    @Override
    public List<ProductDetails> getAllProducts() {
        logger.info("Retrieving all products");
        return productDetailsRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     * 
     * @param id The ID of the product to be retrieved.
     * @return The ProductDetails object.
     * @throws RuntimeException if the product is not found.
     */
    @Override
    public ProductDetails getProductById(Long id) {
        logger.info("Retrieving product with ID: {}", id);
        return productDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product not found with id: " + id);
                });
    }
    
    public Long extractUserId(HttpServletRequest request) {
   	 String userEmail = TokenUtil.extractEmail(request);
  	 Optional<User> optionalUser = userRepository.findByEmail(userEmail);
      if (optionalUser.isPresent()) {
          User user = optionalUser.get();
          return user.getUserId();
      } else {
          return null; // Or throw an exception, or return an optional based on your design choice
      }
  }
}