package com.melon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melon.app.entity.User;

/**
 * Unused atm
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can define custom queries here if needed
    User findByEmail(String email);
}