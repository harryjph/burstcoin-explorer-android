package com.harrysoft.burstcoinexplorer.burst.entity;

import com.harrysoft.burstcoinexplorer.burst.util.BurstUtils;

import java.math.BigInteger;
import java.util.Objects;

public class BurstAddress {

    private final String address;
    private final BigInteger numericID;

    public BurstAddress(BigInteger numericID) {
        this.numericID = numericID;
        if (numericID.equals(BigInteger.ZERO)) {
            this.address = "";
        } else {
            this.address = BurstUtils.toBurstAddress(numericID);
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
