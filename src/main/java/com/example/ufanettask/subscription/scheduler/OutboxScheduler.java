package com.example.ufanettask.subscription.scheduler;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.enums.EventStatus;
import com.example.ufanettask.subscription.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0/2 * * * *")
    public void publish() {
        List<Event> events = outboxRepository.findByStatus(EventStatus.NEW);

        List<Long> sentIds = new ArrayList<>();
        List<Long> faildIds = new ArrayList<>();

        for (Event event : events) {
            try {
                rabbitTemplate.convertAndSend(event.getQueue(), event.getData());
                sentIds.add(event.getId());
            } catch (Exception e) {
                faildIds.add(event.getId());
            }
        }
        if (!sentIds.isEmpty()) {
            outboxRepository.updateStatus(sentIds, EventStatus.SENT.toString());
        }
        if (!faildIds.isEmpty()) {
            outboxRepository.updateStatus(sentIds, EventStatus.FAILED.toString());
        }
    }
}
