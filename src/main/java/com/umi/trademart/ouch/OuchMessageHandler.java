package com.umi.trademart.ouch;

import com.umi.trademart.dto.OrderRequest;
import com.umi.trademart.dto.OrderResponse;
import com.umi.trademart.ouch.model.OuchMessage;
import com.umi.trademart.ouch.model.OuchOrderEntry;
import com.umi.trademart.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Handler for OUCH protocol messages
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OuchMessageHandler {
    private final OrderService orderService;

    /**
     * Process an OUCH message
     * @param message The OUCH message to process
     * @return The processed order response
     */
    public OrderResponse processMessage(OuchMessage message) {
        log.info("Processing OUCH message: {}", message);
        if (message instanceof OuchOrderEntry) {
            return processOrderEntry((OuchOrderEntry) message);
        }
        log.warn("Unsupported OUCH message type: {}", message.getClass().getSimpleName());
        return null;
    }

    /**
     * Process an OUCH order entry message
     * @param orderEntry The order entry message
     * @return The processed order response
     */
    private OrderResponse processOrderEntry(OuchOrderEntry orderEntry) {
        try {
            log.info("Processing OUCH order entry: {}", orderEntry);
            
            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(orderEntry.getSymbol())
                    .side(orderEntry.getSide())
                    .quantity(orderEntry.getQuantity())
                    .price(orderEntry.getPrice())
                    .type(orderEntry.getOrderType())
                    .clientOrderId(orderEntry.getOrderReference())
                    .build();

            OrderResponse response = orderService.createOrder(orderRequest);
            log.info("Created order from OUCH message: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error processing OUCH order entry: {}", e.getMessage());
            return null;
        }
    }
} 