package com.Wallet.Application.module.Transactions.transectionlimite.dto;

import java.math.BigDecimal;

public class DailyLimitRequest {
    private BigDecimal dailyLimit;

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}