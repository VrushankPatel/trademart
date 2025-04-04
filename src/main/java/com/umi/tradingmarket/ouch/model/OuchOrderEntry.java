package com.umi.tradingmarket.ouch.model;

import com.umi.tradingmarket.model.OrderType;
import com.umi.tradingmarket.model.Side;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * OUCH order entry message
 * @author VrushankPatel
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OuchOrderEntry extends OuchMessage {
    private String symbol;
    private Side side;
    private long quantity;
    private double price;
    private OrderType orderType;
    private String timeInForce;
    private String firm;
    private String display;
    private String capacity;
    private String intermarketType;
    private String minimumQuantity;
    private String crossType;
    private String orderReference;
} 