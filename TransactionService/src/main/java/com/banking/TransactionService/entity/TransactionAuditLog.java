package com.banking.TransactionService.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Data
@Builder
@Entity
@Table(name = "transaction_audit_logs")
public class TransactionAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID transactionId;

    @Column(nullable = false)
    private String action; // CREATED, VALIDATED, COMPLETED, FAILED

    @Column(nullable = false)
    private String performedBy; // userId / system

    @Column(nullable = false)
    private Instant performedAt;

    @Column(length = 500)
    private String details;
    /**
     * Factory method for completed transaction audit log.
     */
    public static TransactionAuditLog completed(Transaction tx) {

        return TransactionAuditLog.builder()
                .transactionId(tx.getId())
                .action("COMPLETED")
                .performedBy(tx.getInitiatedBy())
                .performedAt(Instant.now())
                .details("Transaction completed successfully")
                .build();
    }
}
