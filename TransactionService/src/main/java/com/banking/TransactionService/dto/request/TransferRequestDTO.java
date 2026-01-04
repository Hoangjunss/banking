package com.banking.TransactionService.dto.request;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequestDTO {

    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private String description;

    // chống double submit
    private String idempotencyKey;
}