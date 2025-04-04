package com.umi.trademart.repository;

import com.umi.trademart.model.Order;
import com.umi.trademart.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity operations
 * 
 * @author VrushankPatel
 */
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findBySymbol(String symbol);
    List<Order> findByUserId(String userId);
    Optional<Order> findByClientOrderId(String clientOrderId);
    Optional<Order> findByFixOrderId(String fixOrderId);
    Optional<Order> findByOuchOrderId(String ouchOrderId);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.symbol = :symbol ORDER BY o.price ASC, o.createdAt ASC")
    List<Order> findOrderBookBySymbol(String symbol, OrderStatus status);
}