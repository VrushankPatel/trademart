package com.umi.tradingmarket.itch.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Base class for ITCH messages
 * @author VrushankPatel
 */
@Data
@SuperBuilder
public abstract class ItchMessage {
    private String messageType;
    private long timestamp;
    private String symbol;
    private long sequenceNumber;
} 