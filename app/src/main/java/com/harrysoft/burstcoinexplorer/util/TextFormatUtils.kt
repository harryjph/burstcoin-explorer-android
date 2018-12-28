package com.harrysoft.burstcoinexplorer.util

import android.content.Context
import android.text.TextUtils
import burst.kit.entity.BurstAddress
import burst.kit.entity.response.TransactionResponse
import com.harrysoft.burstcoinexplorer.R
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType

object TextFormatUtils {
    @JvmStatic
    fun checkIfSet(context: Context, burstName: String?): String? {
        return if (TextUtils.isEmpty(burstName)) {
            context.getString(R.string.not_set)
        } else {
            burstName
        }
    }

    @JvmStatic
    fun burstAddress(context: Context, burstAddress: BurstAddress?) : String {
        if (burstAddress == null) return context.getString(R.string.not_set)
        val address = burstAddress.fullAddress
        return if (TextUtils.isEmpty(address)) context.getString(R.string.not_set) else address
    }

    @JvmStatic
    fun transactionSummary(context: Context, transaction: TransactionResponse, displayType: TransactionDisplayType) : String {
        val mDisplayType = if (transaction.recipient == null) TransactionDisplayType.FROM else displayType
        return when (mDisplayType) {
            TransactionDisplayType.FROM -> context.getString(R.string.transaction_view_info_from, transaction.amountNQT.toString(), transaction.sender.fullAddress)
            TransactionDisplayType.TO -> if (TextUtils.isEmpty(transaction.recipient!!.fullAddress)) transaction.amountNQT.toString() else context.getString(R.string.transaction_view_info_to, transaction.amountNQT.toString(), transaction.recipient!!.fullAddress)
        }
    }

}