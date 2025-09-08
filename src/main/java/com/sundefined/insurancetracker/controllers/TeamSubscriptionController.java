package com.sundefined.insurancetracker.controllers;

import com.sundefined.insurancetracker.model.TeamSubscription;
import com.sundefined.insurancetracker.service.TeamSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class TeamSubscriptionController {

    private final TeamSubscriptionService service;

    @PostMapping
    public ResponseEntity<TeamSubscription> add(@RequestBody TeamSubscription sub) {
        return ResponseEntity.ok(service.addSubscription(sub));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamSubscription> get(@PathVariable Long id) {
        return service.getSubscription(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TeamSubscription>> getAll() {
        return ResponseEntity.ok(service.getAllSubscriptions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
