package com.harry1453.burst.explorer.entity;

import com.harry1453.burst.BurstUtils;

import java.math.BigInteger;
import java.util.Objects;

public class BurstAddress {

    private final String address;
    private final BigInteger numericID;

    public BurstAddress(BigInteger numericID) {
        if (numericID == null) {
            this.numericID = BigInteger.ZERO;
        } else {
            this.numericID = numericID;
        }

        if (this.numericID.equals(BigInteger.ZERO)) {
            this.address = "";
        } else {
            this.address = BurstUtils.toBurstAddress(this.numericID);
        }
    }

    public BurstAddress(String RS) throws BurstUtils.ReedSolomon.DecodeException {
        if (RS.startsWith("BURST-")) {
            RS = RS.substring(6);
        }
        numericID = BurstUtils.toNumericID(RS);
        address = RS;
    }

    public BigInteger getNumericID() {
        return numericID;
    }

    public String getRawAddress() {
        return address;
    }

    public String getFullAddress() {
        if (address == null || address.length() == 0) {
            return "";
        } else {
            return "BURST-" + address;
        }
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BurstAddress && Objects.equals(numericID, ((BurstAddress) obj).numericID);
    }
}
