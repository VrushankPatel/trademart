package com.umi.trademart.controller;

import com.umi.trademart.dto.OrderRequest;
import com.umi.trademart.dto.OrderResponse;
import com.umi.trademart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling FIX protocol related endpoints
 * 
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/fix")
@RequiredArgsConstructor
@Tag(name = "FIX Protocol", description = "FIX protocol management APIs")
@SecurityRequirement(name = "bearerAuth")
public class FixController {
    private final OrderService orderService;

    @PostMapping("/order")
    @PreAuthorize("hasRole('TRADER')")
    @Operation(summary = "Create a new order via FIX")
    public ResponseEntity<OrderResponse> createFixOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}