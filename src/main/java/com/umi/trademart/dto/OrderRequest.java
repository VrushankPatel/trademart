package com.umi.trademart.dto;

import com.umi.trademart.model.OrderType;
import com.umi.trademart.model.Side;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for order creation requests
 * 
 * @author VrushankPatel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String symbol;
    private Side side;
    private Long quantity;
    private Double price;
    private OrderType type;
    private String clientOrderId;
}