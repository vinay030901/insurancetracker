package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.TeamSubscription;
import com.sundefined.insurancetracker.repository.TeamSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamSubscriptionService {

    private final TeamSubscriptionRepository repository;

    public TeamSubscription addSubscription(TeamSubscription sub) {
        return repository.save(sub);
    }

    public Optional<TeamSubscription> getSubscription(Long id) {
        return repository.findById(id);
    }

    public List<TeamSubscription> getAllSubscriptions() {
        return repository.findAll();
    }

    public void deleteSubscription(Long id) {
        repository.deleteById(id);
    }
}

