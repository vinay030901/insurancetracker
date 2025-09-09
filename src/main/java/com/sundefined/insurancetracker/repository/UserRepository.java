package com.sundefined.insurancetracker.repository;

import com.sundefined.insurancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    Optional<User> findByUserIdAndPassword(String userId, String password);
}
