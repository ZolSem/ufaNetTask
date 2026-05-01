package com.example.ufanettask.subscription.service;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.enums.EventStatus;
import com.example.ufanettask.subscription.enums.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventService {

    private final ObjectMapper objectMapper;
    private final OutboxService outboxService;

    @Transactional
    public void saveEventOutbox(Event event) {
        outboxService.save(event);
    }

    public Event createEvent(Object object, EventType eventType, String queue) {
        try {
            String data = objectMapper.writeValueAsString(object);
            Event event = new Event();
            event.setEventType(eventType);
            event.setData(data);
            event.setStatus(EventStatus.NEW);
            event.setQueue(queue);
            return event;
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
