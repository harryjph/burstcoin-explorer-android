package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class Transaction implements Parcelable {
    public final BurstValue amount;
    public final BigInteger blockID;
    public final String fullHash;
    public final BigInteger confirmations;
    public final BurstValue fee;
    public final BigInteger type; // todo find out all possible types
    public final String signatureHash;
    public final String signature;
    public final BurstAddress sender;
    public final BurstAddress recipient;
    public final String timestamp;
    public final BigInteger transactionID;
    // todo attachment
    public final BigInteger subType;

    public Transaction(BurstValue amount, BigInteger blockID, String fullHash, BigInteger confirmations, BurstValue fee, BigInteger type, String signatureHash, String signature, BurstAddress sender, BurstAddress recipient, String timestamp, BigInteger transactionID, BigInteger subType) {
        this.amount = amount;
        this.blockID = blockID;
        this.fullHash = fullHash;
        this.confirmations = confirmations;
        this.fee = fee;
        this.type = type;
        this.signatureHash = signatureHash;
        this.signature = signature;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.transactionID = transactionID;
        this.subType = subType;
    }

    protected Transaction(Parcel in) {
        amount = BurstValue.createWithoutDividing(in.readString());
        blockID = new BigInteger(in.readString());
        fullHash = in.readString();
        confirmations = new BigInteger(in.readString());
        fee = BurstValue.createWithoutDividing(in.readString());
        type = new BigInteger(in.readString());
        signatureHash = in.readString();
        signature = in.readString();
        sender = new BurstAddress(new BigInteger(in.readString()));
        recipient = new BurstAddress(new BigInteger(in.readString()));
        timestamp = in.readString();
        transactionID = new BigInteger(in.readString());
        subType = new BigInteger(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount.toUnformattedString());
        dest.writeString(blockID.toString());
        dest.writeString(fullHash);
        dest.writeString(confirmations.toString());
        dest.writeString(fee.toUnformattedString());
        dest.writeString(type.toString());
        dest.writeString(signatureHash);
        dest.writeString(signature);
        dest.writeString(sender.getNumericID().toString());
        dest.writeString(recipient.getNumericID().toString());
        dest.writeString(timestamp);
        dest.writeString(transactionID.toString());
        dest.writeString(subType.toString());
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
