package com.banking.HistoryService.mapper;


import com.banking.HistoryService.dto.response.BalanceHistoryResponseDTO;
import com.banking.HistoryService.entity.BalanceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class}
)
public interface BalanceHistoryMapper {

    BalanceHistoryResponseDTO toResponseDTO(BalanceHistory entity);

    List<BalanceHistoryResponseDTO> toResponseDTOs(List<BalanceHistory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    BalanceHistory toEntity(BalanceHistoryCreateDTO dto);
}