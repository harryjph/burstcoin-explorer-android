package com.harry1453.burst.explorer.entity

data class Account (
        @JvmField val address: BurstAddress,
        @JvmField val publicKey: String,
        @JvmField val name: String,
        @JvmField val description: String,
        @JvmField val balance: BurstValue,
        @JvmField val forgedBalance: BurstValue,
        // TODO issued assets
        @JvmField val rewardRecipient: BurstAddress,
        @JvmField val rewardRecipientName: String
)