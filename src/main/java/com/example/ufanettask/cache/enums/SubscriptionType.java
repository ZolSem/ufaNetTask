package com.example.ufanettask.cache.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
    BASIC(100), PRO(200);
    private int price;

    SubscriptionType(int price) {
        this.price = price;
    }
}
