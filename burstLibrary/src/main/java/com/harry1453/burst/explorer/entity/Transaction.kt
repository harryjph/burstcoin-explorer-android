package com.harry1453.burst.explorer.entity

import java.math.BigInteger

data class Transaction (
        @JvmField val amount: BurstValue,
        @JvmField val blockID: BigInteger,
        @JvmField val fullHash: String,
        @JvmField val confirmations: BigInteger,
        @JvmField val fee: BurstValue,
        @JvmField val type: BigInteger,
        @JvmField val signatureHash: String,
        @JvmField val signature: String,
        @JvmField val sender: BurstAddress,
        @JvmField val recipient: BurstAddress,
        @JvmField val timestamp: BigInteger,
        @JvmField val transactionID: BigInteger,
        // TODO Attachment
        @JvmField val subType: BigInteger
)