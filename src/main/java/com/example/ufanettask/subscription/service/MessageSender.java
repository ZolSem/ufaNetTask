package com.example.ufanettask.subscription.service;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.enums.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageSender {

    private final EventService eventService;
    private final RabbitTemplate rabbitTemplate;

    public void sendEvent(Event event) {
        try {
            rabbitTemplate.convertAndSend(event.getQueue(), event.getData());
            event.setStatus(EventStatus.SENT);
        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED);
            log.error("Failed to send event: {}", e.getMessage());
        } finally {
            eventService.saveEventOutbox(event);
        }
    }
}