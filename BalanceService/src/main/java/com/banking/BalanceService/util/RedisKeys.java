package com.banking.BalanceService.util;

import java.util.UUID;

public class RedisKeys {
    private RedisKeys(){}

    public static String balance(UUID accountId){
        return "balance:" + accountId;
    }

    public static String tx(UUID txId){
        return "tx:" + txId;
    }

    public static String lock(UUID accountId){
        return "lock:account:" + accountId;
    }
}
