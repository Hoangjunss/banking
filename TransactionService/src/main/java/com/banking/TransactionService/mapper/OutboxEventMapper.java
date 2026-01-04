package com.banking.TransactionService.mapper;

import com.banking.TransactionService.entity.OutboxEvent;
import com.banking.TransactionService.entity.OutboxStatus;
import com.banking.TransactionService.entity.Transaction;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class, OutboxStatus.class}
)
public interface OutboxEventMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "aggregateId", source = "transaction.id")
    @Mapping(target = "aggregateType", constant = "TRANSACTION")
    @Mapping(target = "eventType", source = "eventType")
    @Mapping(target = "payload", source = "payload")
    @Mapping(target = "status", expression = "java(OutboxStatus.PENDING)")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    OutboxEvent toOutboxEvent(Transaction transaction, String eventType, String payload);
}