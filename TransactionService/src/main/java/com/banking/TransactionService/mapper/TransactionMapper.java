package com.banking.TransactionService.mapper;



import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = TransactionStatus.class
)
public interface TransactionMapper {

    // ===== DEPOSIT =====
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", constant = "DEPOSIT")
    @Mapping(target = "status", expression = "java(TransactionStatus.PENDING)")
    @Mapping(target = "referenceCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Transaction toDepositEntity(DepositRequestDTO request);

    // ===== WITHDRAW =====
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", constant = "WITHDRAW")
    @Mapping(target = "status", expression = "java(TransactionStatus.PENDING)")
    @Mapping(target = "referenceCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Transaction toWithdrawEntity(WithdrawRequestDTO request);

    // ===== TRANSFER =====
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", constant = "TRANSFER")
    @Mapping(target = "status", expression = "java(TransactionStatus.PENDING)")
    @Mapping(target = "referenceCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Transaction toTransferEntity(TransferRequestDTO request);

    TransactionResponseDTO toResponse(Transaction transaction);
}
