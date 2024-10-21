package com.Wallet.Application.module.Transactions.transectionlimite.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "apna_monthly_limite")
public class MontlyLimite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long montly_limite_id;

	@Column(nullable = false)
	private Long user_id;

	@Column(nullable = false)
	private LocalDate updatedon;

	@Column(nullable = false)
	private BigDecimal dailyLimit;
	
	@Column(nullable = false)
    private BigDecimal monthlyLimite;
	
	
	
    	
}
