package com.banking.BalanceService.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BalanceResponseDTO {
    private UUID accountId;
    private BigDecimal availableBalance;
    private BigDecimal blockedBalance;
    private LocalDateTime updatedAt;
}