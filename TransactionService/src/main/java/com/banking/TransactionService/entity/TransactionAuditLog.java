package com.banking.TransactionService.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "transaction_audit_logs")
@Data
@Builder
public class TransactionAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(nullable = false)
    private String performedBy; // userId / system

    @Column(nullable = false)
    private Instant performedAt;

    @Column(length = 500)
    private String details;

    /* ======================================================
       Factory helpers
       ====================================================== */

    public static TransactionAuditLog of(
            Transaction tx,
            AuditAction action,
            String details
    ) {
        return TransactionAuditLog.builder()
                .transactionId(tx.getId())
                .action(action)
                .performedBy(tx.getInitiatedBy())
                .performedAt(Instant.now())
                .details(details)
                .build();
    }
}