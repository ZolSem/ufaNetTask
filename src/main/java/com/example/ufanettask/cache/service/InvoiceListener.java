package com.example.ufanettask.cache.service;

import com.example.ufanettask.cache.entity.Invoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceListener {

    private final ObjectMapper objectMapper;
    private final UserInvoiceCacheService userInvoiceCacheService;

    @RabbitListener(queues = "${queue.name.invoice}")
    public void handleInvoice(String message) {
        log.info("Received invoice: {}", message);
        try {
            Invoice invoice = objectMapper.readValue(message, Invoice.class);
            userInvoiceCacheService.saveInvoiceToUser(invoice);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
