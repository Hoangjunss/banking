package com.banking.HistoryService.entity;

import com.banking.HistoryService.enums.BalanceChangeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "balance_histories", indexes = {
        @Index(name = "idx_bh_account_created", columnList = "accountId, createdAt"),
        @Index(name = "idx_bh_transaction",     columnList = "transactionId")
})
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID accountId;
    private UUID transactionId;      // liên kết với TransactionHistory

    @Enumerated(EnumType.STRING)
    private BalanceChangeType type;  // CREDIT | DEBIT

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;       // số tiền thay đổi

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceAfter;

    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}