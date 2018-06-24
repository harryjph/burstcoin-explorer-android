package com.harry1453.burst.explorer.entity;

import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

public class Block {
    public final BigInteger transactionCount;
    public final BigInteger timestamp;
    public final BigInteger blockID;
    public final BurstValue total;
    public final BigInteger size;
    public final BurstValue fee;
    public final BurstValue blockReward;
    public final List<BigInteger> transactionIDs;
    public final BigInteger blockNumber;
    public final BigInteger generatorID;
    @Nullable
    public Account generator;

    public Block(BigInteger transactionCount, BigInteger timestamp, BigInteger blockID, BurstValue total, BigInteger size, BurstValue fee, BurstValue blockReward, List<BigInteger> transactionIDs, BigInteger blockNumber, BigInteger generatorID, @Nullable Account generator) {
        this.transactionCount = transactionCount;
        this.timestamp = timestamp;
        this.blockID = blockID;
        this.total = total;
        this.size = size;
        this.fee = fee;
        this.blockReward = blockReward;
        this.transactionIDs = transactionIDs;
        this.blockNumber = blockNumber;
        this.generatorID = generatorID;
        this.generator = generator;
    }

    public void setGenerator(Account generator) {
        if (this.generator == null) {
            this.generator = generator;
        }
    }
}
