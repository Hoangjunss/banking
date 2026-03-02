package com.banking.BalanceService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestDTO {
    private UUID transactionId; // ID để chống trùng lặp (Idempotency)
    private UUID accountId;
    private BigDecimal amount;   // Số tiền cộng vào
    private String description;  // Nội dung nạp tiền
}