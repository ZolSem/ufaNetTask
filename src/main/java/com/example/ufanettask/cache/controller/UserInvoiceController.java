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
        log.info("getUserInfo 1");
        UserInvoice user = userInvoiceCacheService
                .getUser(userId)
                .orElseThrow(() -> new IllegalStateException("User not found with id:" + userId));

        log.info("getUserInfo 2");
        UserInfoResponse response = new UserInfoResponse();
        log.info("getUserInfo 3: {}", response);

        response.setActiveSubscriptionType(user.getSubscriptionType().toString());
        log.info("getUserInfo 4: {}", response);

        List<Invoice> invoices = user.getInvoices().reversed();
        log.info("getUserInfo 5: {}", invoices);

        List<UserInfoResponse.InvoiceDto> invoiceDtos = user.getInvoices().reversed().stream()
                .map(invoice -> objectMapper.convertValue(invoice, UserInfoResponse.InvoiceDto.class))
                .toList();
        log.info("getUserInfo 6: {}", invoiceDtos);

        int start = page * size;
        log.info("getUserInfo 7: {}", start);
        int end = Math.min(start + size, invoices.size());
        log.info("getUserInfo 8: {}", end);
        response.setInvoices(invoiceDtos.subList(start, end));
        log.info("getUserInfo 9: {}", response);

        ResponseEntity<UserInfoResponse> res = ResponseEntity.ok(response);
        log.info("getUserInfo 10: status {} body {}", res.getStatusCode(), res.getBody());
        return ResponseEntity.ok(res);
    }
}
