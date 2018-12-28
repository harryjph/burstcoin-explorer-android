package com.harrysoft.burstcoinexplorer.util

import burst.kit.entity.response.BlockResponse
import com.harry1453.burst.BurstConstants
import java.math.BigDecimal
import java.text.DecimalFormat

object FileSizeUtils {
    @JvmStatic
    fun formatBlockSize(size: Int, percentageUsage: Double) : String {
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
    fun formatBlockSize(block: BlockResponse) : String {
        return formatBlockSize(block.payloadLength, calculateBlockSpaceUsage(block))
    }

    @JvmStatic
    fun calculateBlockSpaceUsage(block: BlockResponse): Double {
        return block.payloadLength.toDouble() / (if (BurstConstants.PRE_DYMAXION_HF_BLOCKHEIGHT > block.height) BurstConstants.BLOCK_MAX_SIZE_PRE_HF else BurstConstants.BLOCK_MAX_SIZE_POST_HF).toDouble()
    }
}
