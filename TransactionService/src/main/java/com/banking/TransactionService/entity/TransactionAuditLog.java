package com.banking.TransactionService.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

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
}
