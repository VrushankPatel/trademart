package com.umi.tradingmarket.dto;

import com.umi.tradingmarket.model.OrderType;
import com.umi.tradingmarket.model.Side;
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
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String symbol;
    private BigDecimal price;
    private Long quantity;
    private Side side;
    private OrderType type;
    private String clientOrderId;
}