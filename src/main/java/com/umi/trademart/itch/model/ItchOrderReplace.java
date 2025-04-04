package com.umi.trademart.itch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItchOrderReplace extends ItchOrderEntry {
    private String originalOrderReference;
} 