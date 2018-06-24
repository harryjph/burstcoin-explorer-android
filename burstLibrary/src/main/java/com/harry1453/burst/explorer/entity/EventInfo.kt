package com.harry1453.burst.explorer.entity

import org.jetbrains.annotations.Nullable
import java.math.BigInteger

class EventInfo(@JvmField val name: String, @Nullable infoPage: String?, @Nullable blockHeight: BigInteger?) {
    @JvmField val infoPage: String
    @JvmField val infoPageSet: Boolean
    @JvmField val blockHeight: BigInteger
    @JvmField val blockHeightSet: Boolean

    init {
        if (infoPage == null || infoPage == "") {
            this.infoPage = ""
            infoPageSet = false
        } else {
            this.infoPage = infoPage
            infoPageSet = true
        }

        if (blockHeight == null) {
            this.blockHeight = BigInteger.ZERO
            blockHeightSet = false
        } else {
            this.blockHeight = blockHeight
            blockHeightSet = true
        }
    }
}
