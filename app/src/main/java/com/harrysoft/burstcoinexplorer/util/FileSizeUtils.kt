package com.harrysoft.burstcoinexplorer.util

import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat

object FileSizeUtils {
    @JvmStatic
    fun formatFileSize(byteSize : BigInteger) : String {
        val size = BigDecimal(byteSize)

        val decimalFormat = DecimalFormat("0.000")

        val t = size.divide(BigDecimal("2").pow(40))
        if (t.compareTo(BigDecimal.ONE) == 1) {
            return StringBuilder().append(decimalFormat.format(t)).append(" TB").toString()
        }

        val g = size.divide(BigDecimal("2").pow(30))
        if (g.compareTo(BigDecimal.ONE) == 1) {
            return StringBuilder().append(decimalFormat.format(g)).append(" GB").toString()
        }

        val m = size.divide(BigDecimal("2").pow(20))
        if (m.compareTo(BigDecimal.ONE) == 1) {
            return StringBuilder().append(decimalFormat.format(m)).append(" MB").toString()
        }

        val k = size.divide(BigDecimal("2").pow(10))
        if (k.compareTo(BigDecimal.ONE) == 1) {
            return StringBuilder().append(decimalFormat.format(k)).append(" KB").toString()
        }

        return StringBuilder().append(size.toString()).append(" Bytes").toString()
    }
}