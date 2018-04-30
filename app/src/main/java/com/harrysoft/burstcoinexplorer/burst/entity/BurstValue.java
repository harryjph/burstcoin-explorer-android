package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigDecimal;

public class BurstValue extends BigDecimal {

    public BurstValue(String val) {
        super(new BigDecimal(val).divide(BigDecimal.TEN.pow(8)).toString());
    }

    public static BurstValue createWithoutDividing(String value) {
        return new BurstValue(new BigDecimal(value).multiply(BigDecimal.TEN.pow(8)).toString());
    }

    @Override
    public String toString() {
        String value = super.toString();
        String burstSuffix = " BURST";
        if (value.endsWith(burstSuffix)) {
            return value;
        } else {
            return value + burstSuffix;
        }
    }

    public String toUnformattedString() {
        return super.toString();
    }
}
