package com.banking.TransactionService.mapper;

import com.banking.TransactionService.entity.EntryType;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionEntry;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {UUID.class, Instant.class}
)
public interface TransactionEntryMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "transaction", source = "transaction")
    @Mapping(target = "type", source = "entryType")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    TransactionEntry toEntry(Transaction transaction, EntryType entryType);
}
