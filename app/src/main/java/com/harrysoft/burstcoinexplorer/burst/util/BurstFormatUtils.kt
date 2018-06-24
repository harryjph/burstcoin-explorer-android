package com.harrysoft.burstcoinexplorer.burst.util

import android.content.Context
import android.text.TextUtils
import com.harry1453.burst.explorer.entity.BurstAddress
import com.harry1453.burst.explorer.entity.Transaction
import com.harrysoft.burstcoinexplorer.R
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType

object BurstFormatUtils {
    @JvmStatic
    fun checkIfSet(context: Context, burstName: String?): String? {
        return if (TextUtils.isEmpty(burstName)) {
            context.getString(R.string.not_set)
        } else {
            burstName
        }
    }

    @JvmStatic
    fun burstAddress(context: Context, burstAddress: BurstAddress) : String {
        val address = burstAddress.fullAddress
        return if (TextUtils.isEmpty(address)) context.getString(R.string.not_set) else address
    }

    @JvmStatic
    fun transactionSummary(context: Context, transaction: Transaction, displayType: TransactionDisplayType) : String {
        return when (displayType) {
            TransactionDisplayType.FROM -> context.getString(R.string.transaction_view_info_from, transaction.amount.toString(), transaction.sender.fullAddress)
            TransactionDisplayType.TO -> if (TextUtils.isEmpty(transaction.recipient.fullAddress)) transaction.amount.toString() else context.getString(R.string.transaction_view_info_to, transaction.amount.toString(), transaction.recipient.fullAddress)
        }
    }

}