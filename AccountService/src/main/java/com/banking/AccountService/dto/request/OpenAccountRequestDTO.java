package com.banking.AccountService.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class OpenAccountRequestDTO {
    private UUID ownerId;
    private String type;
}

