package com.banking.HistoryService.mapper;

import com.banking.HistoryService.dto.request.AccountHistoryCreateDTO;
import com.banking.HistoryService.dto.response.AccountHistoryResponseDTO;
import com.banking.HistoryService.entity.AccountHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class}
)
public interface AccountHistoryMapper {

    AccountHistoryResponseDTO toResponseDTO(AccountHistory entity);

    List<AccountHistoryResponseDTO> toResponseDTOs(List<AccountHistory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AccountHistory toEntity(AccountHistoryCreateDTO dto);
}