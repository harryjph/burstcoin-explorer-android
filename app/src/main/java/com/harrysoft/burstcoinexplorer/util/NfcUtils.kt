package com.harrysoft.burstcoinexplorer.util

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import burst.kit.entity.BurstID
import com.harrysoft.burstcoinexplorer.main.router.ExplorerRouter
import java.math.BigInteger
import java.util.*

object NfcUtils {
    @JvmStatic
    fun createBeamMessage(type: String, data: String): NdefMessage {
        return NdefMessage(NdefRecord.createMime("application/com.harrysoft.burstcoinexplorer", ("$type:$data").toByteArray()), NdefRecord.createApplicationRecord("com.harrysoft.burstcoinexplorer"))
    }

    @JvmStatic
    fun processNfcIntent(context: Context, intent: Intent) {
        val message = String((intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0] as NdefMessage).records[0].payload)
        val stringTokenizer = StringTokenizer(message, ":")
        if (stringTokenizer.countTokens() != 2) {
            throw IllegalArgumentException()
        }
        val type = stringTokenizer.nextToken()
        val id = BurstID((stringTokenizer.nextToken()))

                when (type) {
            "block_id" -> ExplorerRouter.viewBlockDetailsByID(context, id)
            "account_id" -> ExplorerRouter.viewAccountDetails(context, id)
            "transaction_id" -> ExplorerRouter.viewTransactionDetailsByID(context, id)
            else -> throw IllegalArgumentException()
        }
    }
}