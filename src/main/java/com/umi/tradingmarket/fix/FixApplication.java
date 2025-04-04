package com.umi.tradingmarket.fix;

import com.umi.tradingmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.NewOrderSingle;

/**
 * QuickFIX/J application implementation for handling FIX messages
 * 
 * @author VrushankPatel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FixApplication implements Application {
    private final OrderService orderService;
    private final FixMessageHandler messageHandler;

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("FIX session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("FIX client logged on: {}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("FIX client logged out: {}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {}

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {}

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Received FIX message: {}", message);
        
        try {
            if (message instanceof NewOrderSingle) {
                messageHandler.handleNewOrderSingle((NewOrderSingle) message, sessionId);
            }
        } catch (Exception e) {
            log.error("Error processing FIX message: {}", e.getMessage(), e);
            sendReject(message, sessionId, e.getMessage());
        }
    }

    private void sendReject(Message message, SessionID sessionId, String reason) throws FieldNotFound {
        Message reject = createReject(message, reason);
        try {
            Session.sendToTarget(reject, sessionId);
        } catch (SessionNotFound e) {
            log.error("Failed to send reject message: {}", e.getMessage(), e);
        }
    }

    private Message createReject(Message message, String reason) throws FieldNotFound {
        ExecutionReport reject = new ExecutionReport(
            new OrderID(message.getString(ClOrdID.FIELD)),
            new ExecID(generateExecId()),
            new ExecTransType(ExecTransType.NEW),
            new ExecType(ExecType.REJECTED),
            new OrdStatus(OrdStatus.REJECTED),
            new Symbol(message.getString(Symbol.FIELD)),
            new Side(message.getChar(Side.FIELD)),
            new LeavesQty(0),
            new CumQty(0),
            new AvgPx(0)
        );
        reject.set(new Text(reason));
        return reject;
    }

    private String generateExecId() {
        return "EXE" + System.currentTimeMillis();
    }
}