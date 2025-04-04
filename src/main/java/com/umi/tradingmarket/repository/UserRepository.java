package com.umi.tradingmarket.repository;

import com.umi.tradingmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User entity operations
 * 
 * @author VrushankPatel
 */
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}