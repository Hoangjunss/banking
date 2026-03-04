package com.banking.HistoryService.dto.response;

import com.banking.HistoryService.enums.TransactionDirection;
import com.banking.HistoryService.enums.TransactionStatus;
import com.banking.HistoryService.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponseDTO {
    private UUID transactionId;
    private String referenceCode;
    private UUID accountId;
    private TransactionType type;
    private TransactionDirection direction;
    private BigDecimal amount;
    private UUID counterpartAccountId;
    private TransactionStatus status;
    private String description;
    private String initiatedBy;
    private Instant transactionAt;
    private Instant createdAt;
}
