package com.banking.TransactionService.dto.internal;

import java.time.Instant;

public class AuditLogResponseDTO {

    private String action;
    private String performedBy;
    private Instant performedAt;
    private String details;
}
