package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountTransactions {
    public BurstAddress address;
    public final ArrayList<BigInteger> transactions;

    public AccountTransactions(ArrayList<BigInteger> transactions) {
        this.transactions = transactions;
    }

    public AccountTransactions setAddress(BurstAddress address) {
        this.address = address;
        return this;
    }
}
