package com.example.ufanettask.cache.entity;


import com.example.ufanettask.cache.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RedisHash("user_invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInvoice {
    @Id
    private Long userId;
    private List<Invoice> invoices =  new ArrayList<>();
    private SubscriptionType subscriptionType;
    private LocalDate activationDate;
    private LocalDate activeUntil;

    public UserInvoice(Long userId, LocalDate activationDate) {
        this.userId = userId;
        this.activationDate = activationDate;
        this.invoices = new ArrayList<>();
    }

    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        this.subscriptionType = invoice.getSubscriptionType();
        this.activeUntil= invoice.getIssueDate().plusMonths(1);
    }
}