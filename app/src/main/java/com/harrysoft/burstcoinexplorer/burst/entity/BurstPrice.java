package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigDecimal;

public class BurstPrice {
    public final String currencyCode;
    public final BigDecimal price;
    public final BigDecimal marketCapital;

    public BurstPrice(String currencyCode, BigDecimal price, BigDecimal marketCapital) {
        this.currencyCode = currencyCode;
        this.price = price;
        this.marketCapital = marketCapital;
    }
}
