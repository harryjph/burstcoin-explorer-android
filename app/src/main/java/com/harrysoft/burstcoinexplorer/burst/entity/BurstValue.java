package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BurstValue extends BigDecimal {

    public BurstValue(String val) {
        super(new BigDecimal(val).divide(BigDecimal.TEN.pow(8), RoundingMode.HALF_UP).toString());
    }

    public static BurstValue createWithoutDividing(String value) {
        return new BurstValue(new BigDecimal(value).multiply(BigDecimal.TEN.pow(8)).toString());
    }

    @Override
    public String toString() {
        String value = setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString(); // round to 3 d.p.
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
