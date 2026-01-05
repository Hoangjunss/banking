package com.banking.AccountService.entity;


import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    @Version
    private Integer version;
}
