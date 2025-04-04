package com.umi.tradingmarket.service;

import com.umi.tradingmarket.dto.OrderRequest;
import com.umi.tradingmarket.dto.OrderResponse;
import com.umi.tradingmarket.mapper.OrderMapper;
import com.umi.tradingmarket.model.Order;
import com.umi.tradingmarket.model.OrderStatus;
import com.umi.tradingmarket.model.User;
import com.umi.tradingmarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.umi.tradingmarket.model.MarketState;

/**
 * Service for handling order operations
 * 
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MatchingEngineService matchingEngineService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Order order = orderMapper.toEntity(request);
        order.setUser(currentUser);
        
        Order savedOrder = orderRepository.save(order);
        matchingEngineService.processOrder(savedOrder);
        
        return orderMapper.toDto(savedOrder);
    }

    public List<OrderResponse> getOrderBook(String symbol) {
        return orderRepository.findOrderBookBySymbol(symbol, OrderStatus.NEW)
            .stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<OrderResponse> getUserOrders() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserId(currentUser.getId())
            .stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
            
        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    /**
     * Get all orders
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Get orders by symbol
     * @param symbol The symbol to get orders for
     * @return List of orders for the symbol
     */
    public List<Order> getOrdersBySymbol(String symbol) {
        return orderRepository.findBySymbol(symbol);
    }

    /**
     * Set the market state
     * @param state The new market state
     */
    public void setMarketState(MarketState state) {
        // Implementation needed
    }

    /**
     * Get the current market state
     * @return The current market state
     */
    public MarketState getMarketState() {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }
}