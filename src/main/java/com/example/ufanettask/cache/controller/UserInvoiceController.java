package com.example.ufanettask.cache.controller;

import com.example.ufanettask.cache.dto.UserInfoResponse;
import com.example.ufanettask.cache.entity.Invoice;
import com.example.ufanettask.cache.entity.UserInvoice;
import com.example.ufanettask.cache.service.UserInvoiceCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
@ResponseBody
public class UserInvoiceController {

    private final UserInvoiceCacheService userInvoiceCacheService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{userId}/info")
    public ResponseEntity<?> getUserInfo(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserInvoice user = userInvoiceCacheService
                .findUserInvoiceById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist with id '" + userId + "'"));
        UserInfoResponse response = new UserInfoResponse();
        response.setActiveSubscriptionType(user.getSubscriptionType());
        List<Invoice> invoices = user.getInvoices().reversed();
        List<UserInfoResponse.InvoiceDto> invoiceDtos = user.getInvoices().reversed().stream()
                .map(invoice -> objectMapper.convertValue(invoice, UserInfoResponse.InvoiceDto.class))
                .toList();
        int start = page * size;
        int end = Math.min(start + size, invoices.size());
        response.setInvoices(invoiceDtos.subList(start, end));

        return ResponseEntity.ok(response);
    }
}
