package com.banking.TransactionService.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Builder
@Data
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

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

    private Instant createdAt;
    /**
     * Factory method for Transaction Completed event.
     *
     * This method guarantees:
     * - correct aggregate type
     * - correct event type
     * - consistent payload structure
     */
    public static OutboxEvent transactionCompleted(Transaction tx) {

        return OutboxEvent.builder()
                .aggregateType("TRANSACTION")
                .aggregateId(tx.getId())
                .eventType("TRANSACTION_COMPLETED")
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
