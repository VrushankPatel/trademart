package com.umi.trademart.fix;

import com.umi.trademart.dto.OrderRequest;
import com.umi.trademart.model.OrderType;
import com.umi.trademart.model.Side;
import com.umi.trademart.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;

import java.math.BigDecimal;

/**
 * Handler for FIX messages
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FixMessageHandler {
    private final OrderService orderService;

    /**
     * Handle incoming FIX message
     * @param message The FIX message
     * @param sessionID The FIX session ID
     */
    public void handleMessage(Message message, SessionID sessionID) {
        if (message instanceof NewOrderSingle) {
            handleNewOrderSingle((NewOrderSingle) message, sessionID);
        }
    }

    /**
     * Handle NewOrderSingle message
     * @param message The NewOrderSingle message
     * @param sessionID The FIX session ID
     */
    public void handleNewOrderSingle(NewOrderSingle message, SessionID sessionID) {
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
                    .price(price.doubleValue())
                    .side(side)
                    .type(orderType)
                    .clientOrderId(clientOrderId)
                    .build();

            var response = orderService.createOrder(orderRequest);

            // Send execution report
            ExecutionReport executionReport = new ExecutionReport();
            executionReport.set(new OrderID(response.getId().toString()));
            executionReport.set(new ExecID(generateExecId()));
            executionReport.set(new ExecTransType(ExecTransType.NEW));
            executionReport.set(new ExecType(ExecType.NEW));
            executionReport.set(new OrdStatus(OrdStatus.NEW));
            executionReport.set(new Symbol(symbol));
            executionReport.set(new quickfix.field.Side(fixSide.getValue()));
            executionReport.set(new LeavesQty(quantity.longValue()));
            executionReport.set(new CumQty(0));
            executionReport.set(new AvgPx(price.doubleValue()));

            Session.sendToTarget(executionReport, sessionID);
        } catch (Exception e) {
            log.error("Error processing FIX message", e);
        }
    }

    private String generateExecId() {
        return "EXE" + System.currentTimeMillis();
    }
}