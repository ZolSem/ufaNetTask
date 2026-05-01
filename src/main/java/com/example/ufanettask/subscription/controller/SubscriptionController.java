package com.example.ufanettask.subscription.controller;

import com.example.ufanettask.subscription.dto.SubscriptionActivationRequest;
import com.example.ufanettask.subscription.dto.SubscriptionDeactivationRequest;
import com.example.ufanettask.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestBody SubscriptionActivationRequest request) {
        subscriptionService.activate(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @RequestBody SubscriptionDeactivationRequest request) {
        subscriptionService.deactivate(request);
        return ResponseEntity.ok().build();
    }
}
