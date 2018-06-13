package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class Account implements Parcelable {
    public final BurstAddress address;
    public final String publicKey;
    public final String name;
    public final String description;
    public final BurstValue balance;
    public final BurstValue forgedBalance;
    // todo issued assets
    // todo don't display reward recipient if it is set to the same address
    public final BurstAddress rewardRecipient;
    public final String rewardRecipientName;

    public Account(BurstAddress address, String publicKey, String name, String description, BurstValue balance, BurstValue forgedBalance, BurstAddress rewardRecipientAddress, String rewardRecipientName) {
        this.address = address;
        this.publicKey = publicKey;
        this.balance = balance;
        this.name = name;
        this.description = description;
        this.forgedBalance = forgedBalance;
        this.rewardRecipient = rewardRecipientAddress;
        this.rewardRecipientName = rewardRecipientName;
    }

    private Account(Parcel in) {
        address = new BurstAddress(new BigInteger(in.readString()));
        publicKey = in.readString();
        name = in.readString();
        description = in.readString();
        balance = BurstValue.fromBurst(in.readString());
        forgedBalance = BurstValue.fromBurst(in.readString());
        rewardRecipient = new BurstAddress(new BigInteger(in.readString()));
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
        dest.writeString(description);
        dest.writeString(balance.toUnformattedString());
        dest.writeString(forgedBalance.toString());
        dest.writeString(rewardRecipient.getNumericID().toString());
        dest.writeString(rewardRecipientName);
    }
}
