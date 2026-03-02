package com.banking.BalanceService.mapper;

import com.banking.BalanceService.dto.BalanceHistoryResponseDTO;
import com.banking.BalanceService.dto.BalanceResponseDTO;
import com.banking.BalanceService.dto.CreditRequestDTO;
import com.banking.BalanceService.dto.DebitRequestDTO;
import com.banking.BalanceService.entity.Balance;
import com.banking.BalanceService.entity.BalanceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {

    // Chuyển đổi từ Entity Balance sang Response để trả về cho Client
    BalanceResponseDTO toResponseDTO(Balance balance);

    // Chuyển đổi từ Entity History sang Response để xem sao kê
    BalanceHistoryResponseDTO toHistoryResponseDTO(BalanceHistory history);

    // Chuyển đổi từ CreditRequestDTO sang BalanceHistory (để lưu vết nạp tiền)
    @Mapping(target = "type", constant = "CREDIT")
    @Mapping(target = "amount", source = "amount")
    BalanceHistory toEntityFromCredit(CreditRequestDTO request);

    // Chuyển đổi từ DebitRequestDTO sang BalanceHistory (để lưu vết trừ tiền)
    @Mapping(target = "type", constant = "DEBIT")
    @Mapping(target = "amount", source = "amount")
    BalanceHistory toEntityFromDebit(DebitRequestDTO request);
}