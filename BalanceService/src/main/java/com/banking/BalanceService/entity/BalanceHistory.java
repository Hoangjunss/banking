package com.banking.BalanceService.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "balance_histories")
@Data
@NoArgsConstructor

public class BalanceHistory {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false, unique = true)
    private UUID transactionId; // Dùng để đối soát với Transaction Service

    private BigDecimal amount;        // Số tiền (+ hoặc -)
    private BigDecimal balanceBefore; // Số dư trước khi đổi
    private BigDecimal balanceAfter;  // Số dư sau khi đổi

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEBIT hoặc CREDIT

    private LocalDateTime createdAt;
}