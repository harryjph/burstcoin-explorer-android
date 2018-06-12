package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Block implements Parcelable {
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

    protected Block(Parcel in) {
        transactionCount = new BigInteger(in.readString());
        timestamp = new BigInteger(in.readString());
        blockID = new BigInteger(in.readString());
        total = BurstValue.fromBurst(in.readString());
        size = new BigInteger(in.readString());
        fee = BurstValue.fromBurst(in.readString());
        blockReward = BurstValue.fromBurst(in.readString());
        transactionIDs = new ArrayList<>();
        in.readList(transactionIDs, BigInteger.class.getClassLoader());
        blockNumber = new BigInteger(in.readString());
        generatorID = new BigInteger(in.readString());
        generator = in.readParcelable(Account.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transactionCount.toString());
        dest.writeString(timestamp.toString());
        dest.writeString(blockID.toString());
        dest.writeString(total.toUnformattedString());
        dest.writeString(size.toString());
        dest.writeString(fee.toUnformattedString());
        dest.writeString(blockReward.toUnformattedString());
        dest.writeList(transactionIDs);
        dest.writeString(blockNumber.toString());
        dest.writeString(generatorID.toString());
        dest.writeParcelable(generator, flags);
    }

    public static final Creator<Block> CREATOR = new Creator<Block>() {
        @Override
        public Block createFromParcel(Parcel in) {
            return new Block(in);
        }

        @Override
        public Block[] newArray(int size) {
            return new Block[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
