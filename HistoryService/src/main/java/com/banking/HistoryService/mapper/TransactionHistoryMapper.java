package com.banking.HistoryService.mapper;

import com.banking.HistoryService.dto.request.TransactionHistoryCreateDTO;
import com.banking.HistoryService.dto.response.TransactionHistoryResponseDTO;
import com.banking.HistoryService.entity.TransactionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class}
)
public interface TransactionHistoryMapper {

    TransactionHistoryResponseDTO toResponseDTO(TransactionHistory entity);

    List<TransactionHistoryResponseDTO> toResponseDTOs(List<TransactionHistory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TransactionHistory toEntity(TransactionHistoryCreateDTO dto);
}