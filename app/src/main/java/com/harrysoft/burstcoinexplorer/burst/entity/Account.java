package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class Account implements Parcelable {
    public final BurstAddress address;
    public final String publicKey;
    public final String name;
    public final BurstValue balance;
    public final BigInteger totalSentN;
    public final BurstValue totalSent;
    public final BigInteger totalReceivedN;
    public final BurstValue totalReceived;
    public final BurstValue totalFees;
    public final BigInteger soloMinedBlocks;
    public final BurstValue soloMinedBalance;
    public final BigInteger poolMinedBlocks;
    public final BurstValue poolMinedBalance;
    // todo issued assets
    public final BurstAddress rewardRecipient;
    public final String rewardRecipientName;

    public Account(BurstAddress address, String publicKey, BurstValue totalFees, BurstValue totalReceived, BigInteger totalSentN, BigInteger totalReceivedN, BurstValue balance, String name, BigInteger poolMinedBlocks, BigInteger soloMinedBlocks, BurstValue poolMinedBalance, BurstValue soloMinedBalance, BurstValue totalSent, BurstAddress rewardRecipientAddress, String rewardRecipientName) {
        this.address = address;
        this.publicKey = publicKey;
        this.totalFees = totalFees;
        this.totalReceived = totalReceived;
        this.totalSentN = totalSentN;
        this.totalReceivedN = totalReceivedN;
        this.balance = balance;
        this.name = name;
        this.poolMinedBlocks = poolMinedBlocks;
        this.soloMinedBlocks = soloMinedBlocks;
        this.poolMinedBalance = poolMinedBalance;
        this.soloMinedBalance = soloMinedBalance;
        this.totalSent = totalSent;
        this.rewardRecipient = rewardRecipientAddress;
        this.rewardRecipientName = rewardRecipientName;
    }

    private Account(Parcel in) {
        address = new BurstAddress(in.readString());
        publicKey = in.readString();
        name = in.readString();
        balance = BurstValue.createWithoutDividing(in.readString());
        totalSentN = new BigInteger(in.readString());
        totalSent = BurstValue.createWithoutDividing(in.readString());
        totalReceivedN = new BigInteger(in.readString());
        totalReceived = BurstValue.createWithoutDividing(in.readString());
        totalFees = BurstValue.createWithoutDividing(in.readString());
        soloMinedBlocks = new BigInteger(in.readString());
        soloMinedBalance = BurstValue.createWithoutDividing(in.readString());
        poolMinedBlocks = new BigInteger(in.readString());
        poolMinedBalance = BurstValue.createWithoutDividing(in.readString());
        rewardRecipient = new BurstAddress(in.readString());
        rewardRecipientName = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address.getNumericID().toString());
        dest.writeString(publicKey);
        dest.writeString(name);
        dest.writeString(balance.toUnformattedString());
        dest.writeString(totalSentN.toString());
        dest.writeString(totalSent.toUnformattedString());
        dest.writeString(totalReceivedN.toString());
        dest.writeString(totalReceived.toUnformattedString());
        dest.writeString(totalFees.toUnformattedString());
        dest.writeString(soloMinedBlocks.toString());
        dest.writeString(soloMinedBalance.toUnformattedString());
        dest.writeString(poolMinedBlocks.toString());
        dest.writeString(poolMinedBalance.toUnformattedString());
        dest.writeString(rewardRecipient.getNumericID().toString());
        dest.writeString(rewardRecipientName);
    }
}
