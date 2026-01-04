package com.banking.TransactionService.dto.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WithdrawRequestDTO {

    private String fromAccountId;
    private BigDecimal amount;
    private String description;
    private String idempotencyKey;
}
