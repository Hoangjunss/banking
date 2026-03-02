package com.banking.BalanceService.dto;


import com.banking.BalanceService.entity.TransactionType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BalanceHistoryResponseDTO {
    private UUID id;
    private UUID transactionId;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private TransactionType type;
    private LocalDateTime createdAt;
}