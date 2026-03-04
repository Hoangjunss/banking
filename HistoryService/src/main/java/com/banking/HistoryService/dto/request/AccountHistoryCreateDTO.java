package com.banking.HistoryService.dto.request;

import com.banking.HistoryService.enums.AccountEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryCreateDTO {

    private UUID accountId;
    private UUID ownerId;
    private AccountEventType eventType;
    private String detail;
    private String performedBy;
    private Instant eventAt;
}