package com.umi.trademart.model;

/**
 * Enumeration representing the status of an order
 * 
 * @author VrushankPatel
 */
public enum OrderStatus {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELLED,
    REJECTED,
    EXPIRED
}