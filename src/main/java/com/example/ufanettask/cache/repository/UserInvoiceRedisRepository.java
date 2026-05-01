package com.example.ufanettask.cache.repository;

import com.example.ufanettask.cache.entity.UserInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInvoiceRedisRepository extends CrudRepository<UserInvoice, Long> {
    Optional<UserInvoice> findByUserId(Long userId);
}
