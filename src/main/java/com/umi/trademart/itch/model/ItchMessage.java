package com.umi.trademart.itch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Base class for ITCH messages
 * @author VrushankPatel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ItchMessage {
    private long sequenceNumber;
} 