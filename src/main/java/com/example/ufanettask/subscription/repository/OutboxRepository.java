package com.example.ufanettask.subscription.repository;

import com.example.ufanettask.subscription.entity.Event;
import com.example.ufanettask.subscription.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = ?1 WHERE e.id IN ?2")
    void updateStatus(List<Long> sentIds, String eventStatus);
}
