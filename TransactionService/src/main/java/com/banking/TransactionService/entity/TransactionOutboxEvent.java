package com.banking.TransactionService.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;
@Builder
@Data
@Entity
@Table(name = "outbox_events")
public class TransactionOutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String aggregateType; // TRANSACTION

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String eventType; // TransactionCompleted

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    @Column(nullable = false)
    private int retryCount = 0;

    private Instant nextRetryAt;



    private Instant createdAt;
    /**
     * Factory method for Transaction Completed event.
     *
     * This method guarantees:
     * - correct aggregate type
     * - correct event type
     * - consistent payload structure
     */
    public static TransactionOutboxEvent debitRequested(Transaction tx) {
        return build(tx, "DEBIT_REQUESTED");
    }

    public static TransactionOutboxEvent creditRequested(Transaction tx) {
        return build(tx, "CREDIT_REQUESTED");
    }

    public static TransactionOutboxEvent completed(Transaction tx) {
        return build(tx, "TRANSACTION_COMPLETED");
    }

    public static TransactionOutboxEvent failed(Transaction tx) {
        return build(tx, "TRANSACTION_FAILED");
    }

    private static TransactionOutboxEvent build(Transaction tx, String type) {
        return TransactionOutboxEvent.builder()
                .aggregateType("TRANSACTION")
                .aggregateId(tx.getId())
                .eventType(type)
                .payload(buildPayload(tx))
                .status(OutboxStatus.PENDING)
                .createdAt(Instant.now())
                .build();
    }
    private static String buildPayload(Transaction tx) {
        // Có thể thay bằng ObjectMapper sau
        return String.format(
                "{ \"transactionId\": \"%s\", \"type\": \"%s\", \"amount\": %s }",
                tx.getId(),
                tx.getType(),
                tx.getAmount()
        );
    }

}
