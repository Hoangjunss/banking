package com.banking.HistoryService.dto.response;

import com.banking.HistoryService.enums.BalanceChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistoryResponseDTO {

    private UUID accountId;
    private UUID transactionId;
    private BalanceChangeType type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Instant createdAt;
}