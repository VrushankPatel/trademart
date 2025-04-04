package com.umi.tradingmarket.controller;

import com.umi.tradingmarket.dto.OrderResponse;
import com.umi.tradingmarket.ouch.OuchMessageHandler;
import com.umi.tradingmarket.ouch.model.OuchMessage;
import com.umi.tradingmarket.ouch.model.OuchOrderEntry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for OUCH protocol endpoints
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/ouch")
@RequiredArgsConstructor
@Tag(name = "OUCH Protocol", description = "OUCH protocol endpoints")
public class OuchController {
    private final OuchMessageHandler ouchMessageHandler;

    /**
     * Submit an OUCH order entry
     * @param orderEntry The order entry to submit
     * @return The processed order response
     */
    @PostMapping("/order")
    @PreAuthorize("hasRole('TRADER')")
    @Operation(summary = "Submit OUCH order entry", description = "Submit a new order using OUCH protocol")
    public ResponseEntity<OrderResponse> submitOrder(@RequestBody OuchOrderEntry orderEntry) {
        OrderResponse orderResponse = ouchMessageHandler.processMessage(orderEntry);
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Submit a raw OUCH message
     * @param message The OUCH message to submit
     * @return The processed order response
     */
    @PostMapping("/message")
    @PreAuthorize("hasRole('TRADER')")
    @Operation(summary = "Submit OUCH message", description = "Submit a raw OUCH protocol message")
    public ResponseEntity<OrderResponse> submitMessage(@RequestBody OuchMessage message) {
        OrderResponse orderResponse = ouchMessageHandler.processMessage(message);
        return ResponseEntity.ok(orderResponse);
    }
} 