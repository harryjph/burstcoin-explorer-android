package com.harrysoft.burstcoinexplorer.util

import com.harry1453.burst.BurstUtils
import com.harry1453.burst.explorer.entity.Block
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat

object FileSizeUtils {
    @JvmStatic
    fun formatBlockSize(size: Long, percentageUsage: Double) : String {
        val decimalFormat = DecimalFormat("0.000")
        val blockSize = size.toBigDecimal()
        val formattedSize = StringBuilder()

        val sizeInTB = blockSize.divide(BigDecimal("2").pow(40))
        val sizeInGB = blockSize.divide(BigDecimal("2").pow(30))
        val sizeInMB = blockSize.divide(BigDecimal("2").pow(20))
        val sizeInKB = blockSize.divide(BigDecimal("2").pow(10))

        when {
            sizeInTB.compareTo(BigDecimal.ONE) > -1 -> formattedSize.append(decimalFormat.format(sizeInTB)).append(" TB")
            sizeInGB.compareTo(BigDecimal.ONE) > -1 -> formattedSize.append(decimalFormat.format(sizeInGB)).append(" GB")
            sizeInMB.compareTo(BigDecimal.ONE) > -1 -> formattedSize.append(decimalFormat.format(sizeInMB)).append(" MB")
            sizeInKB.compareTo(BigDecimal.ONE) > -1 -> formattedSize.append(decimalFormat.format(sizeInKB)).append(" KB")
            else -> formattedSize.append(blockSize.toString()).append(" Bytes")
        }

        return formattedSize.append(" (" + DecimalFormat("###.###").format(percentageUsage * 100) + "%)").toString()
    }

    @JvmStatic
    fun formatBlockSize(block: Block) : String {
        return formatBlockSize(block.size, BurstUtils.calculateBlockSpaceUsage(block))
    }
}
