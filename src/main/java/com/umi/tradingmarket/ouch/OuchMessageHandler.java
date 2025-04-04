package com.umi.tradingmarket.ouch;

import com.umi.tradingmarket.dto.OrderRequest;
import com.umi.tradingmarket.dto.OrderResponse;
import com.umi.tradingmarket.ouch.model.OuchMessage;
import com.umi.tradingmarket.ouch.model.OuchOrderEntry;
import com.umi.tradingmarket.service.OrderService;
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
        if (message instanceof OuchOrderEntry) {
            return processOrderEntry((OuchOrderEntry) message);
        }
        throw new IllegalArgumentException("Unsupported message type: " + message.getMessageType());
    }

    /**
     * Process an OUCH order entry message
     * @param orderEntry The order entry message
     * @return The processed order response
     */
    private OrderResponse processOrderEntry(OuchOrderEntry orderEntry) {
        log.info("Processing OUCH order entry: {}", orderEntry);
        
        OrderRequest orderRequest = OrderRequest.builder()
                .symbol(orderEntry.getSymbol())
                .side(orderEntry.getSide())
                .quantity(orderEntry.getQuantity())
                .price(BigDecimal.valueOf(orderEntry.getPrice()))
                .type(orderEntry.getOrderType())
                .clientOrderId(orderEntry.getOrderReference())
                .build();

        return orderService.createOrder(orderRequest);
    }
} 