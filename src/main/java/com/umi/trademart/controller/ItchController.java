package com.umi.trademart.controller;

import com.umi.trademart.itch.model.ItchMessage;
import com.umi.trademart.service.ItchMarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for ITCH market data endpoints
 * @author VrushankPatel
 */
@RestController
@RequestMapping("/api/v1/itch")
@RequiredArgsConstructor
@Tag(name = "ITCH Market Data", description = "ITCH market data endpoints")
public class ItchController {
    private final ItchMarketDataService itchMarketDataService;

    /**
     * Subscribe to market data for a symbol
     * @param symbol The symbol to subscribe to
     * @return A success response
     */
    @PostMapping("/subscribe/{symbol}")
    @PreAuthorize("hasAnyRole('TRADER', 'OBSERVER')")
    @Operation(summary = "Subscribe to market data", description = "Subscribe to ITCH market data for a symbol")
    public ResponseEntity<Void> subscribeToMarketData(@PathVariable String symbol) {
        // TODO: Implement subscription logic
        return ResponseEntity.ok().build();
    }

    /**
     * Unsubscribe from market data for a symbol
     * @param symbol The symbol to unsubscribe from
     * @return A success response
     */
    @PostMapping("/unsubscribe/{symbol}")
    @PreAuthorize("hasAnyRole('TRADER', 'OBSERVER')")
    @Operation(summary = "Unsubscribe from market data", description = "Unsubscribe from ITCH market data for a symbol")
    public ResponseEntity<Void> unsubscribeFromMarketData(@PathVariable String symbol) {
        // TODO: Implement unsubscription logic
        return ResponseEntity.ok().build();
    }

    /**
     * Get market snapshot for a symbol
     * @param symbol The symbol to get a snapshot for
     * @return The market snapshot
     */
    @GetMapping("/snapshot/{symbol}")
    @PreAuthorize("hasAnyRole('TRADER', 'OBSERVER')")
    @Operation(summary = "Get market snapshot", description = "Get a market snapshot for a symbol")
    public ResponseEntity<ItchMessage> getMarketSnapshot(@PathVariable String symbol) {
        // TODO: Implement market snapshot logic
        return ResponseEntity.ok().build();
    }
} 