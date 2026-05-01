package com.example.ufanettask.subscription.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
    BASIC(100), PRO(200);
    private int price;

    SubscriptionType(int price) {
        this.price = price;
    }
}
