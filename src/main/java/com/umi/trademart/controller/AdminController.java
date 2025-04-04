package com.umi.trademart.controller;

import com.umi.trademart.model.MarketState;
import com.umi.trademart.model.Order;
import com.umi.trademart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for admin operations
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Operations", description = "Admin endpoints for market management")
public class AdminController {
    private final OrderService orderService;

    /**
     * Get all orders in the order book
     * @return List of all orders
     */
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders", description = "Get all orders in the order book")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Get orders for a specific symbol
     * @param symbol The symbol to get orders for
     * @return List of orders for the symbol
     */
    @GetMapping("/orders/{symbol}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get symbol orders", description = "Get all orders for a specific symbol")
    public ResponseEntity<List<Order>> getSymbolOrders(@PathVariable String symbol) {
        return ResponseEntity.ok(orderService.getOrdersBySymbol(symbol));
    }

    /**
     * Set the market state
     * @param state The new market state
     * @return A success response
     */
    @PostMapping("/market-state")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set market state", description = "Set the market state (OPEN, HALT, CLOSE)")
    public ResponseEntity<Void> setMarketState(@RequestBody MarketState state) {
        orderService.setMarketState(state);
        return ResponseEntity.ok().build();
    }

    /**
     * Get the current market state
     * @return The current market state
     */
    @GetMapping("/market-state")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get market state", description = "Get the current market state")
    public ResponseEntity<MarketState> getMarketState() {
        return ResponseEntity.ok(orderService.getMarketState());
    }
} 