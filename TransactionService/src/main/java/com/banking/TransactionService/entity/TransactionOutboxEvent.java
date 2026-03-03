package com.banking.TransactionService.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.json.JsonParseException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
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

    public static TransactionOutboxEvent refundRequested(Transaction tx) {
        return build(tx, "REFUND_REQUESTED");
    }

    private static TransactionOutboxEvent build(Transaction tx, String type) {
        return TransactionOutboxEvent.builder()
                .aggregateType("TRANSACTION")
                .aggregateId(tx.getId())
                .eventType(type)
                .payload(buildPayload(tx, type))
                .status(OutboxStatus.PENDING)
                .createdAt(Instant.now())
                .build();
    }
    private static String buildPayload(Transaction tx, String eventType) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = Map.of(
                "transactionId", tx.getId().toString(),
                "aggregateType", "TRANSACTION",
                "aggregateId", tx.getId(),
                "eventType", eventType,
                "initiatedBy", tx.getInitiatedBy(),
                "fromAccountId", tx.getFromAccountId(),
                "toAccountId", tx.getToAccountId(),
                "amount", tx.getAmount(),
                "type", tx.getType(),
                "referenceCode", tx.getReferenceCode()
        );

        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonParseException e) {
            throw new RuntimeException("Failed to build payload", e);
        }
    }

}
