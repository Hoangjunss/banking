package com.banking.BalanceService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequestDTO {
    private UUID transactionId; // ID để chống trùng lặp
    private UUID accountId;
    private BigDecimal amount;   // Số tiền trừ đi
    private String reason;      // Lý do trừ tiền (thanh toán hóa đơn, rút ATM...)
}