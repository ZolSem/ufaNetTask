package com.example.ufanettask.subscription.repository;

import com.example.ufanettask.subscription.entity.Subscription;
import com.example.ufanettask.subscription.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.userId = ?1 AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveByUserId(Long userId);

    @Query("SELECT s FROM Subscription s WHERE s.userId = ?1 AND s.type = ?2 AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveByUserIdAndType(Long userId, SubscriptionType ype);

    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND EXTRACT(DAY FROM s.activationDate) = ?1")
    List<Subscription> findAllActiveByDayOfMonth(int dayOfMonth);
}