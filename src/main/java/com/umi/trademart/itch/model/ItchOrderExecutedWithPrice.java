package com.umi.trademart.itch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * ITCH message for order executions with price
 * @author VrushankPatel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderExecutedWithPrice extends ItchMessage {
    private String orderReference;
    private long executedQuantity;
    private double executionPrice;
} 