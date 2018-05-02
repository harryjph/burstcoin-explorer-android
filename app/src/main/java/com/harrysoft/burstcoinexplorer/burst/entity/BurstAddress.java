package com.harrysoft.burstcoinexplorer.burst.entity;

import com.harrysoft.burstcoinexplorer.burst.BurstUtils;

import java.math.BigInteger;

public class BurstAddress {

    private String address;
    private BigInteger numericID;

    public BurstAddress(BigInteger numericID) {
        this.numericID = numericID;
        if (numericID.equals(BigInteger.ZERO)) {
            this.address = "";
        } else {
            this.address = BurstUtils.toBurstAddress(numericID);
        }
    }

    public BurstAddress(String numericID) {
        this(new BigInteger(numericID));
    }

    public BigInteger getNumericID() {
        return numericID;
    }

    public String getRawAddress() {
        return address;
    }

    public String getFullAddress() {
        return "BURST-" + address;
    }
}
