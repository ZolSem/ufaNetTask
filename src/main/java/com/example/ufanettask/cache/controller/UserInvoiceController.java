package com.example.ufanettask.cache.controller;

import com.example.ufanettask.cache.entity.UserInvoice;
import com.example.ufanettask.cache.service.UserInvoiceCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserInvoiceController {

    private final UserInvoiceCacheService userInvoiceCacheService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
        UserInvoice user = userInvoiceCacheService
                .getUser(userId)
                .orElseThrow(() -> new IllegalStateException("User does not exist with id:" + userId));
        return ResponseEntity.ok(objectMapper.valueToTree(user));
    }
}
