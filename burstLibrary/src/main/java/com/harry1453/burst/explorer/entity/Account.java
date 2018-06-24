package com.harry1453.burst.explorer.entity;

public class Account {
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
}
