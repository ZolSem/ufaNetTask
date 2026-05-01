package com.example.ufanettask.subscription.scheduler;

import com.example.ufanettask.subscription.entity.Subscription;
import com.example.ufanettask.subscription.repository.SubscriptionRepository;
import com.example.ufanettask.subscription.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class InvoiceScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceService invoiceService;

    @Scheduled(cron = "0 0 1 * * *")
    public void generateInvoices() {

        List<Subscription> activeSubsToday = subscriptionRepository.findAllActiveByDayOfMonth(LocalDate.now().getDayOfMonth());

        for (Subscription sub : activeSubsToday) {
            try {
                invoiceService.processInvoice(sub);
            } catch (Exception e) {
                log.error("Failed for scheduled Invoice: {}", sub.getId(), e);
            }
        }
    }
}

