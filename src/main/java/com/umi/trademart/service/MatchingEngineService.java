package com.umi.trademart.service;

import com.umi.trademart.model.Order;
import com.umi.trademart.model.OrderStatus;
import com.umi.trademart.model.Side;
import com.umi.trademart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for matching orders in the order book
 * 
 * @author VrushankPatel
 */
@Service
@RequiredArgsConstructor
public class MatchingEngineService {
    private final OrderRepository orderRepository;

    @Transactional
    public void processOrder(Order newOrder) {
        if (newOrder.getSide() == Side.BUY) {
            matchBuyOrder(newOrder);
        } else {
            matchSellOrder(newOrder);
        }
    }

    private void matchBuyOrder(Order buyOrder) {
        List<Order> sellOrders = orderRepository.findOrderBookBySymbol(buyOrder.getSymbol(), OrderStatus.NEW);
        
        for (Order sellOrder : sellOrders) {
            if (sellOrder.getPrice().compareTo(buyOrder.getPrice()) <= 0) {
                executeMatch(buyOrder, sellOrder);
                if (buyOrder.getStatus() == OrderStatus.FILLED) {
                    break;
                }
            }
        }
    }

    private void matchSellOrder(Order sellOrder) {
        List<Order> buyOrders = orderRepository.findOrderBookBySymbol(sellOrder.getSymbol(), OrderStatus.NEW);
        
        for (Order buyOrder : buyOrders) {
            if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
                executeMatch(buyOrder, sellOrder);
                if (sellOrder.getStatus() == OrderStatus.FILLED) {
                    break;
                }
            }
        }
    }

    private void executeMatch(Order buyOrder, Order sellOrder) {
        long matchQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
        
        buyOrder.setQuantity(buyOrder.getQuantity() - matchQuantity);
        sellOrder.setQuantity(sellOrder.getQuantity() - matchQuantity);
        
        updateOrderStatus(buyOrder);
        updateOrderStatus(sellOrder);
        
        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);
    }

    private void updateOrderStatus(Order order) {
        if (order.getQuantity() == 0) {
            order.setStatus(OrderStatus.FILLED);
        } else {
            order.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
    }
}