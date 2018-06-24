package com.harry1453.burst.explorer.entity;

import java.math.BigInteger;

public class Transaction {
    public final BurstValue amount;
    public final BigInteger blockID;
    public final String fullHash;
    public final BigInteger confirmations;
    public final BurstValue fee;
    public final BigInteger type;
    public final String signatureHash;
    public final String signature;
    public final BurstAddress sender;
    public final BurstAddress recipient;
    public final BigInteger timestamp;
    public final BigInteger transactionID;
    // todo attachment
    public final BigInteger subType;

    public Transaction(BurstValue amount, BigInteger blockID, String fullHash, BigInteger confirmations, BurstValue fee, BigInteger type, String signatureHash, String signature, BurstAddress sender, BurstAddress recipient, BigInteger timestamp, BigInteger transactionID, BigInteger subType) {
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
}
