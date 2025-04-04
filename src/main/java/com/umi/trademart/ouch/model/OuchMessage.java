package com.umi.trademart.ouch.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Base class for OUCH messages
 * @author VrushankPatel
 */
@Data
@SuperBuilder
public abstract class OuchMessage {
    private String messageType;
    private long timestamp;
    private String orderToken;
    private String username;
} 