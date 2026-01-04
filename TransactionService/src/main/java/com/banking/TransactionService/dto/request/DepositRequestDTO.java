package com.banking.TransactionService.dto.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DepositRequestDTO {

    private String toAccountId;
    private BigDecimal amount;
    private String description;
    private String idempotencyKey;
}
