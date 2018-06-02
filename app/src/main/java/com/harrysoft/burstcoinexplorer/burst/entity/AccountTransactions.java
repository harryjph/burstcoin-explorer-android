package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountTransactions {
    public final ArrayList<BigInteger> transactions;

    public AccountTransactions(ArrayList<BigInteger> transactions) {
        this.transactions = transactions;
    }

    public AccountTransactions setAddress(BurstAddress address) {
        BurstAddress address1 = address;
        return this;
    }
}
