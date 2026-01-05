package com.banking.AccountService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountResponseDTO {
    private UUID id;
    private String accountNumber;
    private UUID ownerId;
    private String type;
    private String status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
}
