
package com.example.ufanettask.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String activeSubscriptionType;
    private List<InvoiceDto> invoices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceDto {
        private Long id;
        private Long userId;
        private Long subscriptionId;
        private String issueDate;
        private Integer price;
        private String subscriptionType;
        private String activationDate;
    }
}
