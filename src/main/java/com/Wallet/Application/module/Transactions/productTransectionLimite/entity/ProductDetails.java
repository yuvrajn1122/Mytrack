package com.Wallet.Application.module.Transactions.productTransectionLimite.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.Wallet.Application.module.Transactions.transectionlimite.entity.MontlyLimite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apna_product_details")
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_product_id;


	@Column(nullable = false)
	private String productName;
	
	
	@Column(nullable = false)
	private BigDecimal productLimite;
	

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private LocalDate updatedon;

	@Column(nullable = false)
	private Boolean isActive = true;

}
