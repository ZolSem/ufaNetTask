package com.example.ufanettask.subscription.service;


import com.example.ufanettask.subscription.dto.SubscriptionActivationRequest;
import com.example.ufanettask.subscription.dto.SubscriptionDeactivationRequest;
import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.entity.Invoice;
import com.example.ufanettask.subscription.entity.Subscription;
import com.example.ufanettask.subscription.enums.EventType;
import com.example.ufanettask.subscription.enums.SubscriptionStatus;
import com.example.ufanettask.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final EventService eventService;
    private final InvoiceService invoiceService;
    private final MessageSender messageSender;

    @Value("${queue.name.invoice}")
    private String invoiceQueue;

    @Transactional
    public void activate(SubscriptionActivationRequest request) {
        validateDateActivation(request);
        subscriptionRepository.findActiveByUserId(request.getUserId())
                .ifPresent(s -> {
                    log.info("User already has an active subscription");
                    throw new IllegalStateException("User already has an active subscription");
                });

        Subscription subscription = new Subscription();
        subscription.setUserId(request.getUserId());
        subscription.setType(request.getType());
        subscription.setActivationDate(request.getActivationDate());
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);

        Invoice invoice = invoiceService.createAndSaveInvoice(subscription);
        Event invoiceEvent = eventService.createEvent(invoice, EventType.INVOICE_CREATED, invoiceQueue);
        messageSender.sendEvent(invoiceEvent);
    }

    @Transactional
    public void deactivate(SubscriptionDeactivationRequest request) {

        Subscription sub = subscriptionRepository
                .findActiveByUserIdAndType(request.getUserId(), request.getType())
                .orElseThrow(() -> new IllegalStateException("User does not exist with type " + request.getType().toString()));

        sub.setStatus(SubscriptionStatus.INACTIVE);
        sub.setDeactivationDate(LocalDate.now());
        subscriptionRepository.save(sub);
    }

    public static void validateDateActivation(SubscriptionActivationRequest request) {
        if (request.getActivationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Activation date cannot be in the past");
        }
    }
}

