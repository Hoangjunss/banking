package com.banking.TransactionService.dto.response;


import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponseDTO {

    private String transactionId;
    private String referenceCode;
    private BigDecimal amount;
    private String status;
    private String type;
    private Instant createdAt;
}