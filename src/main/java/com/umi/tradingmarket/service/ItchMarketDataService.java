package com.umi.tradingmarket.service;

import com.umi.tradingmarket.itch.model.ItchMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for ITCH market data simulation
 * @author VrushankPatel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItchMarketDataService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, AtomicLong> sequenceNumbers = new ConcurrentHashMap<>();

    /**
     * Broadcast market data for a symbol
     * @param symbol The symbol to broadcast data for
     * @param message The market data message
     */
    public void broadcastMarketData(String symbol, ItchMessage message) {
        message.setSequenceNumber(getNextSequenceNumber(symbol));
        messagingTemplate.convertAndSend("/topic/market-data/" + symbol, message);
    }

    /**
     * Get the next sequence number for a symbol
     * @param symbol The symbol
     * @return The next sequence number
     */
    private long getNextSequenceNumber(String symbol) {
        return sequenceNumbers.computeIfAbsent(symbol, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * Schedule market data updates
     */
    @Scheduled(fixedRate = 1000)
    public void scheduleMarketDataUpdates() {
        // TODO: Implement market data simulation logic
        log.info("Scheduling market data updates");
    }
} 