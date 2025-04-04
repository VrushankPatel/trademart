package com.umi.trademart.itch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.umi.trademart.model.OrderType;
import com.umi.trademart.model.Side;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderEntry extends ItchMessage {
    private String symbol;
    private Side side;
    private long quantity;
    private double price;
    private OrderType orderType;
    private String orderReference;
} 