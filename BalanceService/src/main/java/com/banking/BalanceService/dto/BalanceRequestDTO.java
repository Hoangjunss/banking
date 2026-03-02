package com.banking.BalanceService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BalanceRequestDTO {
    private UUID accountId;
    private BigDecimal amount;
}