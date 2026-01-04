package com.banking.TransactionService.dto.request;

import java.math.BigDecimal;

public class DepositRequestDTO {

    private String toAccountId;
    private BigDecimal amount;
    private String description;
    private String idempotencyKey;
}
