package com.harrysoft.burstcoinexplorer.burst.entity;

import java.math.BigInteger;
import java.util.ArrayList;

public class BlockExtra {
    public final BigInteger blockNumber;
    public final BurstValue blockReward;
    public final ArrayList<BigInteger> transactionIDs;

    public BlockExtra(BigInteger blockNumber, BigInteger blockID, BurstValue blockReward, ArrayList<BigInteger> transactionIDs) {
        this.blockNumber = blockNumber;
        BigInteger blockID1 = blockID;
        this.blockReward = blockReward;
        this.transactionIDs = transactionIDs;
    }
}
