package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BurstValue extends BigDecimal {

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    private BurstValue(String val) {
        super(new BigDecimal(val).divide(BigDecimal.TEN.pow(8)).toString());
    }

    public static BurstValue fromNQT(String value) {
        try {
            return new BurstValue(value);
        } catch (NumberFormatException e) {
            return new BurstValue("0");
        }
    }

    public static BurstValue fromBurst(String value) {
        try {
            return new BurstValue(new BigDecimal(value).multiply(BigDecimal.TEN.pow(8)).toString());
        } catch (NumberFormatException e) {
            return new BurstValue("0");
        }
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
        return super.stripTrailingZeros().toPlainString();
    }
}
