package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionAuditLog;
import com.banking.TransactionService.repository.TransactionAuditLogRepository;
import com.banking.TransactionService.service.TransactionAuditService;

public class TransactionAuditServiceImpl implements TransactionAuditService {
    private  final TransactionAuditLogRepository auditLogRepository;

    public  TransactionAuditServiceImpl ( TransactionAuditLogRepository auditLogRepository){
        this.auditLogRepository=auditLogRepository;
    }
    @Override
    public void logCompleted(Transaction transaction) {
       auditLogRepository.save(
               TransactionAuditLog.completed(transaction)
       );
    }
}
