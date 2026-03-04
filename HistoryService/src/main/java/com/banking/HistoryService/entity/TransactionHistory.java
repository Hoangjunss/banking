package com.banking.HistoryService.entity;

import com.banking.HistoryService.enums.TransactionDirection;
import com.banking.HistoryService.enums.TransactionStatus;
import com.banking.HistoryService.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction_histories", indexes = {
        @Index(name = "idx_txh_account_created", columnList = "accountId, createdAt"),
        @Index(name = "idx_txh_transaction",     columnList = "transactionId"),
        @Index(name = "idx_txh_reference",       columnList = "referenceCode")
})
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID transactionId;
    private String referenceCode;
    private UUID accountId;              // account này trong lịch sử

    @Enumerated(EnumType.STRING)
    private TransactionType type;        // DEPOSIT | WITHDRAW | TRANSFER

    @Enumerated(EnumType.STRING)
    private TransactionDirection direction; // IN | OUT

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    private UUID counterpartAccountId;   // account phía còn lại (TRANSFER)

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;    // SUCCESS | FAILED

    @Column(length = 255)
    private String description;
    private String initiatedBy;

    private Instant transactionAt;
    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}