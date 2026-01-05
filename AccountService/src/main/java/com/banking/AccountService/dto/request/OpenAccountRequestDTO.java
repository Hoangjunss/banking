package com.banking.AccountService.dto.request;

import com.banking.AccountService.entity.AccountStatus;
import com.banking.AccountService.entity.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Version;

import java.time.LocalDateTime;
import java.util.UUID;

public class OpenAccountRequestDTO {
    private UUID ownerId;
    private String type;
}

