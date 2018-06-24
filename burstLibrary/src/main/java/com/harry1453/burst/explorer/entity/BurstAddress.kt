package com.harry1453.burst.explorer.entity

import com.harry1453.burst.BurstUtils
import java.math.BigInteger

class BurstAddress {

    private var address: String
    private var numericID: BigInteger

    constructor(numericID: BigInteger?) {
        this.numericID = numericID ?: BigInteger.ZERO

        this.address = if (this.numericID == BigInteger.ZERO) {
            ""
        } else {
            BurstUtils.toBurstAddress(this.numericID)
        }
    }

    @Throws(BurstUtils.ReedSolomon.DecodeException::class)
    constructor(reedSolomonAddressIn: String?) {
        var reedSolomonAddress = reedSolomonAddressIn ?: ""
        if (reedSolomonAddress.startsWith("BURST-")) {
            reedSolomonAddress = reedSolomonAddress.substring(6)
        }
        numericID = BurstUtils.toNumericID(reedSolomonAddress)
        address = reedSolomonAddress
    }

    fun getNumericID(): BigInteger {
        return numericID
    }

    fun getRawAddress(): String {
        return address
    }

    fun getFullAddress(): String {
        return if (address.isEmpty()) {
            ""
        } else {
            "BURST-$address"
        }
    }

    override fun toString(): String {
        return getFullAddress()
    }

    override fun equals(other: Any?): Boolean {
        return other is BurstAddress && numericID == other.numericID
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + numericID.hashCode()
        return result
    }
}
