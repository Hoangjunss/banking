package com.banking.TransactionService.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionHistoryResponseDTO {

    private String transactionId;
    private String referenceCode;
    private String type;
    private BigDecimal amount;
    private String status;
    private Instant createdAt;
}