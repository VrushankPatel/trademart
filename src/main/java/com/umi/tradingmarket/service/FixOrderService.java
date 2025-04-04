package com.umi.tradingmarket.service;

import com.umi.tradingmarket.dto.OrderRequest;
import com.umi.tradingmarket.dto.OrderResponse;
import com.umi.tradingmarket.model.Order;
import com.umi.tradingmarket.model.OrderType;
import com.umi.tradingmarket.model.Side;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;

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

            Side side = fixSide.getValue() == '1' ? Side.BUY : Side.SELL;
            OrderType orderType = fixOrdType.getValue() == OrdType.MARKET ? OrderType.MARKET : OrderType.LIMIT;

            String clientOrderId = message.getHeader().getField(new ClOrdID()).getValue();

            OrderRequest orderRequest = OrderRequest.builder()
                    .symbol(symbol)
                    .quantity(quantity.longValue())
                    .price(price)
                    .side(side)
                    .type(orderType)
                    .clientOrderId(clientOrderId)
                    .build();

            var response = orderService.createOrder(orderRequest);
            sendExecutionReport(response, sessionID);
        } catch (Exception e) {
            log.error("Error processing FIX order", e);
        }
    }

    public void sendExecutionReport(OrderResponse order, SessionID sessionId) {
        try {
            ExecutionReport execReport = createExecutionReport(order);
            Session.sendToTarget(execReport, sessionId);
        } catch (SessionNotFound e) {
            log.error("Failed to send execution report for order {}: {}", order.getId(), e.getMessage(), e);
        }
    }

    private ExecutionReport createExecutionReport(OrderResponse order) {
        ExecutionReport execReport = new ExecutionReport();
        execReport.set(new OrderID(order.getId()));
        execReport.set(new ExecID(generateExecId()));
        execReport.set(new ExecTransType(ExecTransType.NEW));
        execReport.set(new ExecType(mapOrderStatusToExecType(order)));
        execReport.set(new OrdStatus(mapOrderStatusToOrdStatus(order)));
        execReport.set(new Symbol(order.getSymbol()));
        execReport.set(new quickfix.field.Side(mapSideToFixSide(order)));
        execReport.set(new LeavesQty(order.getQuantity()));
        execReport.set(new CumQty(0));
        execReport.set(new AvgPx(order.getPrice().doubleValue()));
        return execReport;
    }

    private char mapOrderStatusToExecType(OrderResponse order) {
        return switch (order.getStatus()) {
            case NEW -> ExecType.NEW;
            case PARTIALLY_FILLED -> ExecType.PARTIAL_FILL;
            case FILLED -> ExecType.FILL;
            case CANCELLED -> ExecType.CANCELED;
            case REJECTED -> ExecType.REJECTED;
            case EXPIRED -> ExecType.EXPIRED;
            default -> ExecType.NEW;
        };
    }

    private char mapOrderStatusToOrdStatus(OrderResponse order) {
        return switch (order.getStatus()) {
            case NEW -> OrdStatus.NEW;
            case PARTIALLY_FILLED -> OrdStatus.PARTIALLY_FILLED;
            case FILLED -> OrdStatus.FILLED;
            case CANCELLED -> OrdStatus.CANCELED;
            case REJECTED -> OrdStatus.REJECTED;
            case EXPIRED -> OrdStatus.EXPIRED;
            default -> OrdStatus.NEW;
        };
    }

    private char mapSideToFixSide(OrderResponse order) {
        return order.getSide() == Side.BUY ? '1' : '2';
    }

    private String generateExecId() {
        return "EXE" + System.currentTimeMillis();
    }
}