package com.example.ufanettask.subscription.dto;

import com.example.ufanettask.subscription.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDeactivationRequest {

    private Long userId;
    private SubscriptionType type;
}