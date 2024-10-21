package com.Wallet.Application.module.Transactions.productTransectionLimite.service;

import java.util.List;

import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsRequest;
import com.Wallet.Application.module.Transactions.productTransectionLimite.dto.ProductDetailsResponse;
import com.Wallet.Application.module.Transactions.productTransectionLimite.entity.ProductDetails;

import jakarta.servlet.http.HttpServletRequest;

public interface ProductDetailsService {
	public ProductDetailsResponse createProduct(ProductDetailsRequest  productDetailsRequest,HttpServletRequest request);
	 public ProductDetailsResponse updateProduct(ProductDetailsRequest  productDetailsRequest,HttpServletRequest request);
    void softDeleteProduct(Long id);
    List<ProductDetails> getAllProducts();
    ProductDetails getProductById(Long id);
}