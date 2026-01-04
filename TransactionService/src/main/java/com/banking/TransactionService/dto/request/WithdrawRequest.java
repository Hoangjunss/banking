package com.banking.TransactionService.dto.request;

import java.math.BigDecimal;

public class WithdrawRequest {

    private String fromAccountId;
    private BigDecimal amount;
    private String description;
    private String idempotencyKey;
}
