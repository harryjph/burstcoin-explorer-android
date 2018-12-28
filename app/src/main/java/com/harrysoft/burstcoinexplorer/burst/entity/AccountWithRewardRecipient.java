package com.harrysoft.burstcoinexplorer.burst.entity;

import burst.kit.entity.BurstAddress;
import burst.kit.entity.response.AccountResponse;

public class AccountWithRewardRecipient {
    private final AccountResponse account;
    private final BurstAddress rewardRecipient;
    private final String rewardRecipientName;

    public AccountWithRewardRecipient(AccountResponse account, BurstAddress rewardRecipient, String rewardRecipientName) {
        this.account = account;
        this.rewardRecipient = rewardRecipient;
        this.rewardRecipientName = rewardRecipientName;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public BurstAddress getRewardRecipient() {
        return rewardRecipient;
    }

    public String getRewardRecipientName() {
        return rewardRecipientName;
    }
}
