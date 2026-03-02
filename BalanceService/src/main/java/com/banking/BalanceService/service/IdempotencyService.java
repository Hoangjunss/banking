package com.banking.BalanceService.service;

import com.banking.BalanceService.util.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean isProcessed(UUID txId){
        return Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeys.tx(txId)));
    }

    public void markProcessed(UUID txId){
        redisTemplate.opsForValue()
                .set(RedisKeys.tx(txId), "1", Duration.ofHours(24));
    }
}
