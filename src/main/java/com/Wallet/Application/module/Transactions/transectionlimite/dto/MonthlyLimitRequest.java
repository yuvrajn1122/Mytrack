package com.Wallet.Application.module.Transactions.transectionlimite.dto;

import java.math.BigDecimal;

public class MonthlyLimitRequest {
    private BigDecimal monthlyLimit;

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }
}