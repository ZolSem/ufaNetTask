package com.example.ufanettask.subscription.dto;

import com.example.ufanettask.subscription.enums.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionActivationRequest {

    private Long userId;

    private SubscriptionType type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;
}