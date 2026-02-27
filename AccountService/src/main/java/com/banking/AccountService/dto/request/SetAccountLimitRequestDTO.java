package com.banking.AccountService.dto.request;

import com.banking.AccountService.entity.LimitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetAccountLimitRequestDTO {
    private String type;
    private BigDecimal amount;
    private String limitType;
}
