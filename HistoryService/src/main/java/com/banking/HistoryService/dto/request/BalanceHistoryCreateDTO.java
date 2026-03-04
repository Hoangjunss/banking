package com.banking.HistoryService.dto.request;

import com.banking.HistoryService.enums.BalanceChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistoryCreateDTO {

    private UUID accountId;
    private UUID transactionId;
    private BalanceChangeType type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
}