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
@Table(name = "balance_outbox_events")
@Data
@NoArgsConstructor

public class BalanceOutboxEvent {

    @Id
    private UUID id;

    private UUID aggregateId; // Thường là TransactionId

    private String type; // Ví dụ: TRANSACTION_SUCCESS, TRANSACTION_FAILED

    @Lob
    private byte[] payload; // Data chi tiết gửi đi

    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, COMPLETED, FAILED

    private LocalDateTime createdAt;
}
