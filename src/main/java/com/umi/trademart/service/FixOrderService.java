package com.umi.trademart.service;

import com.umi.trademart.dto.OrderRequest;
import com.umi.trademart.dto.OrderResponse;
import com.umi.trademart.model.Order;
import com.umi.trademart.model.OrderType;
import com.umi.trademart.model.Side;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import com.umi.trademart.fix.FixMessageHandler;
import com.umi.trademart.fix.FixApplication;

import java.math.BigDecimal;

/**
 * Service for handling FIX orders
 * @author VrushankPatel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FixOrderService {
    private final OrderService orderService;
    private final FixMessageHandler fixMessageHandler;
    private final FixApplication fixApplication;

    /**
     * Process a FIX order
     * @param message The FIX message
     * @param sessionID The FIX session ID
     */
    public void processOrder(Message message, SessionID sessionID) {
        if (message instanceof NewOrderSingle) {
            processNewOrderSingle((NewOrderSingle) message, sessionID);
        }
    }

    /**
     * Process a NewOrderSingle message
     * @param message The NewOrderSingle message
     * @param sessionID The FIX session ID
     */
    private void processNewOrderSingle(NewOrderSingle message, SessionID sessionID) {
        try {
            String symbol = message.get(new Symbol()).getValue();
            BigDecimal quantity = BigDecimal.valueOf(message.get(new OrderQty()).getValue());
            BigDecimal price = BigDecimal.valueOf(message.get(new Price()).getValue());
            quickfix.field.Side fixSide = new quickfix.field.Side();
            message.get(fixSide);
            quickfix.field.OrdType fixOrdType = new quickfix.field.OrdType();
            message.get(fixOrdType);

            Side side = fixSide.getValue() == quickfix.field.Side.BUY ? Side.BUY : Side.SELL;
            OrderType orderType = fixOrdType.getValue() == OrdType.MARKET ? OrderType.MARKET : OrderType.LIMIT;

            String clientOrderId = message.getHeader().getField(new ClOrdID()).getValue();

            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(symbol)
                    .quantity(quantity.longValue())
                    .price(price.doubleValue())
                    .side(side)
                    .type(orderType)
                    .clientOrderId(clientOrderId)
                    .build();

            var response = orderService.createOrder(orderRequest);
            sendExecutionReport(response, sessionID);
        } catch (Exception e) {
            log.error("Error processing FIX order: {}", e.getMessage());
        }
    }

    private void sendExecutionReport(OrderResponse order, SessionID sessionID) {
        try {
            quickfix.fix44.ExecutionReport executionReport = createExecutionReport(order);
            fixApplication.sendMessage(executionReport, sessionID);
            log.info("Sent execution report for order {}", order.getId());
        } catch (Exception e) {
            log.error("Error sending execution report: {}", e.getMessage());
        }
    }

    private quickfix.fix44.ExecutionReport createExecutionReport(OrderResponse order) throws quickfix.FieldNotFound {
        quickfix.fix44.ExecutionReport executionReport = new quickfix.fix44.ExecutionReport();
        executionReport.set(new OrderID(order.getId().toString()));
        executionReport.set(new ExecID(generateExecID()));
        executionReport.set(new quickfix.field.ExecType(mapOrderStatusToExecType(order.getStatus())));
        executionReport.set(new quickfix.field.OrdStatus(mapOrderStatusToOrdStatus(order.getStatus())));
        executionReport.set(new Symbol(order.getSymbol()));
        executionReport.set(new quickfix.field.Side(mapSideToFixSide(order.getSide())));
        executionReport.set(new LeavesQty(order.getQuantity()));
        executionReport.set(new CumQty(0L));
        executionReport.set(new AvgPx(0.0));
        return executionReport;
    }

    private String generateExecID() {
        return String.valueOf(System.currentTimeMillis());
    }

    private char mapOrderStatusToExecType(com.umi.trademart.model.OrderStatus status) {
        return switch (status) {
            case NEW -> quickfix.field.ExecType.NEW;
            case PARTIALLY_FILLED -> quickfix.field.ExecType.PARTIAL_FILL;
            case FILLED -> quickfix.field.ExecType.FILL;
            case CANCELLED -> quickfix.field.ExecType.CANCELED;
            case REJECTED -> quickfix.field.ExecType.REJECTED;
            default -> quickfix.field.ExecType.NEW;
        };
    }

    private char mapOrderStatusToOrdStatus(com.umi.trademart.model.OrderStatus status) {
        return switch (status) {
            case NEW -> quickfix.field.OrdStatus.NEW;
            case PARTIALLY_FILLED -> quickfix.field.OrdStatus.PARTIALLY_FILLED;
            case FILLED -> quickfix.field.OrdStatus.FILLED;
            case CANCELLED -> quickfix.field.OrdStatus.CANCELED;
            case REJECTED -> quickfix.field.OrdStatus.REJECTED;
            default -> quickfix.field.OrdStatus.NEW;
        };
    }

    private char mapSideToFixSide(Side side) {
        return side == Side.BUY ? quickfix.field.Side.BUY : quickfix.field.Side.SELL;
    }
}