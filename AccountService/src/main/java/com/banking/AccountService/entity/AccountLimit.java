package com.banking.AccountService.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account_limits")
@Data
public class AccountLimit {

    @Id
    private UUID id;

    private UUID accountId;

    @Enumerated(EnumType.STRING)
    private LimitType type;

    private BigDecimal amount;
}
