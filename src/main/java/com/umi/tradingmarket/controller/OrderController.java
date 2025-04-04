package com.umi.tradingmarket.controller;

import com.umi.tradingmarket.dto.OrderRequest;
import com.umi.tradingmarket.dto.OrderResponse;
import com.umi.tradingmarket.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling order-related endpoints
 * 
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('TRADER')")
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/book/{symbol}")
    @Operation(summary = "Get order book for a symbol")
    public ResponseEntity<List<OrderResponse>> getOrderBook(@PathVariable String symbol) {
        return ResponseEntity.ok(orderService.getOrderBook(symbol));
    }

    @GetMapping("/user")
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        return ResponseEntity.ok(orderService.getUserOrders());
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('TRADER')")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}