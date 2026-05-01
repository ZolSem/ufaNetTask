package com.example.ufanettask.subscription.service;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;

    @Transactional
    public void save(Event event) {
        outboxRepository.save(event);
    }
}
