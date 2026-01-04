package com.banking.TransactionService.dto.request;

import java.math.BigDecimal;

public class DepositRequest {

    private String toAccountId;
    private BigDecimal amount;
    private String description;
    private String idempotencyKey;
}
