package com.Wallet.Application.module.Transactions.transectionlimite.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class CreateAndUpdateProductRequest {

	

    private String productName;

   
    private BigDecimal productLimite;
}
