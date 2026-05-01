package com.example.ufanettask.cache.service;

import com.example.ufanettask.cache.entity.Invoice;
import com.example.ufanettask.cache.entity.UserInvoice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInvoiceCacheService {

    private final RedisTemplate<String, UserInvoice> redisTemplate;

    private static final String KEY_PREFIX = "userId:";

    public void saveInvoiceToUser(Invoice invoice) {
        try {
            Long userId = invoice.getUserId();
            String key = KEY_PREFIX + userId;

            UserInvoice userInvoice = getUser(key)
                    .orElse(new UserInvoice(userId, invoice.getActivationDate()));

            userInvoice.addInvoice(invoice);
            redisTemplate.opsForValue().set(key, userInvoice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<UserInvoice> getUser(String key) {
        UserInvoice userInvoice = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(userInvoice);
    }

    public Optional<UserInvoice> getUser(Long userId) {
        String key = KEY_PREFIX + userId;
        UserInvoice userInvoice = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(userInvoice);
    }

}
