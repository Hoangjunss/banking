package com.banking.TransactionService.mapper;



import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {UUID.class, Instant.class, TransactionStatus.class}
)
public interface TransactionMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "status", expression = "java(TransactionStatus.PENDING)")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    Transaction toEntity(TransferRequestDTO request);

    TransactionResponseDTO toResponse(Transaction transaction);
}