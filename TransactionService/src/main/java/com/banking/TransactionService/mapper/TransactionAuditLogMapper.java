package com.banking.TransactionService.mapper;

import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionAuditLog;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class}
)
public interface TransactionAuditLogMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    TransactionAuditLog toAuditLog(Transaction transaction, String action);
}