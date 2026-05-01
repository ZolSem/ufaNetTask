package com.example.ufanettask.cache.entity;


import com.example.ufanettask.cache.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;

@RedisHash("invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private LocalDate issueDate;
    private Integer price;
    private SubscriptionType subscriptionType;
    private LocalDate activationDate;
}