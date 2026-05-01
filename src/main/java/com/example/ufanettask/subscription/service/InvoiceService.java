package com.example.ufanettask.subscription.service;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.entity.Invoice;
import com.example.ufanettask.subscription.entity.Subscription;
import com.example.ufanettask.subscription.enums.EventType;
import com.example.ufanettask.subscription.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2
public class InvoiceService {

    @Value("${queue.name.invoice}")
    private String invoiceQueue;

    private final InvoiceRepository invoiceRepository;
    private final EventService eventService;

    @Transactional
    public Invoice createAndSaveInvoice(Subscription sub) {
        Invoice invoice = new Invoice();
        invoice.setUserId(sub.getUserId());
        invoice.setSubscriptionId(sub.getId());
        invoice.setIssueDate(LocalDate.now());
        invoice.setSubscriptionType(sub.getType());
        invoice.setActivationDate(sub.getActivationDate());
        invoice.setPrice(sub.getType().getPrice());
        return invoiceRepository.save(invoice);
    }

    @Transactional
    @Retryable(retryFor = {Exception.class})
    public void processInvoice(Subscription sub) {
        Invoice invoice = createAndSaveInvoice(sub);
        Event invoiceEvent = eventService.createEvent(invoice, EventType.INVOICE_CREATED, invoiceQueue);
        eventService.saveEventOutbox(invoiceEvent);
    }

    @Recover
    public void recoverProcessInvoice(Exception e, Subscription sub) {
        log.error("All retries failed for subscription: {}", sub.getId(), e);
    }
}