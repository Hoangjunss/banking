package com.banking.HistoryService.entity;

import com.banking.HistoryService.enums.AccountEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_histories", indexes = {
        @Index(name = "idx_ach_account_created", columnList = "accountId, createdAt")
})
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID accountId;
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    private AccountEventType eventType;
    // ACCOUNT_CREATED | ACCOUNT_FROZEN | ACCOUNT_UNFROZEN
    // ACCOUNT_CLOSED  | LIMIT_CHANGED

    @Column(columnDefinition = "TEXT")
    private String detail;              // thông tin thêm về sự kiện

    private String performedBy;         // ai thực hiện (userId hoặc "System")

    private Instant eventAt;
    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}