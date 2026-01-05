package com.banking.AccountService.service;

import com.banking.AccountService.dto.request.*;
import com.banking.AccountService.dto.response.AccountResponseDTO;

import java.util.UUID;

public interface AccountService {
    AccountResponseDTO openAccount(OpenAccountRequestDTO request);
    AccountResponseDTO updateInfo(UUID id, UpdateAccountInfoRequestDTO request);
    void freezeAccount(UUID id, FreezeAccountRequestDTO request);
    void unfreezeAccount(UUID id, UnfreezeAccountRequestDTO request);
    void setLimit(UUID id, SetAccountLimitRequestDTO request);
    void closeAccount(UUID id, CloseAccountRequestDTO request);
}