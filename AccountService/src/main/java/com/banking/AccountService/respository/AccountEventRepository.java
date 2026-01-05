package com.banking.AccountService.respository;

import com.banking.AccountService.entity.AccountEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.desktop.AboutEvent;
import java.util.UUID;

public interface AccountEventRepository extends JpaRepository<AccountEvent,UUID> {
}
