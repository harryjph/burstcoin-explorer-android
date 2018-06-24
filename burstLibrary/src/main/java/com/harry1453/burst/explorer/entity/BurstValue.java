package com.harry1453.burst.explorer.entity;

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
        return roundToThreeDP(this).toPlainString() + " BURST";
    }

    private static BigDecimal roundToThreeDP(BigDecimal in) {
        if (in.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return in.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    }

    public String toUnformattedString() {
        return super.stripTrailingZeros().toPlainString();
    }
}
