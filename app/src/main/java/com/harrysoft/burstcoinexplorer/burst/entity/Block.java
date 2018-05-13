package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class Block implements Parcelable {
    public final BigInteger transactionCount;
    public final String timestamp;
    public final BigInteger blockID;
    public final BurstValue total;
    public final BurstAddress rewardRecipient;
    public final BigInteger size;
    public final String generatorName;
    public final BurstValue fee;
    public final BigInteger blockNumber;
    public final BurstAddress generator;
    public final String rewardRecipientName;

    public Block(BigInteger transactionCount, String timestamp, BigInteger blockID, BurstValue total, BurstAddress rewardRecipient, BigInteger size, String generatorName, BurstValue fee, BigInteger blockNumber, BurstAddress generator, String rewardRecipientName) {
        this.transactionCount = transactionCount;
        this.timestamp = timestamp;
        this.blockID = blockID;
        this.total = total;
        this.rewardRecipient = rewardRecipient;
        this.size = size;
        this.generatorName = generatorName;
        this.fee = fee;
        this.blockNumber = blockNumber;
        this.generator = generator;
        this.rewardRecipientName = rewardRecipientName;
    }

    protected Block(Parcel in) {
        transactionCount = new BigInteger(in.readString());
        timestamp = in.readString();
        blockID = new BigInteger(in.readString());
        total = BurstValue.fromBurst(in.readString());
        rewardRecipient = new BurstAddress(new BigInteger(in.readString()));
        size = new BigInteger(in.readString());
        generatorName = in.readString();
        fee = BurstValue.fromBurst(in.readString());
        blockNumber = new BigInteger(in.readString());
        generator = new BurstAddress(new BigInteger(in.readString()));
        rewardRecipientName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transactionCount.toString());
        dest.writeString(timestamp);
        dest.writeString(blockID.toString());
        dest.writeString(total.toUnformattedString());
        dest.writeString(rewardRecipient.getNumericID().toString());
        dest.writeString(size.toString());
        dest.writeString(generatorName);
        dest.writeString(fee.toUnformattedString());
        dest.writeString(blockNumber.toString());
        dest.writeString(generator.getNumericID().toString());
        dest.writeString(rewardRecipientName);
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
