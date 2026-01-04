package com.banking.TransactionService.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class TransactionDetailResponseDTO {

    private String transactionId;
    private String referenceCode;
    private String type;
    private String status;
    private BigDecimal amount;
    private String description;
    private Instant createdAt;
    private Instant completedAt;

    private List<TransactionEntryResponse> entries;
}
