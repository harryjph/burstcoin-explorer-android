package com.harrysoft.burstcoinexplorer.events.util

import android.content.Context
import com.harrysoft.burstcoinexplorer.R
import java.math.BigInteger

object EventUtils {

    @JvmStatic
    fun formatEventInfo(context: Context, currentBlockHeight: Long, forkName: String, forkHeight: Long): String {
        val blocksRemaining = forkHeight - currentBlockHeight
        if (blocksRemaining <= 0) {
            return context.getString(R.string.event_complete, forkName)
        }

        val minutes: Long = blocksRemaining * 4 % 60
        val hours: Long = blocksRemaining * 4 / 60
        val days: Long = blocksRemaining * 4 / 60 / 24

        val daysSuffix = context.getString(if (days == 1L) R.string.day else R.string.days)
        val hoursSuffix = context.getString(if (hours == 1L) R.string.hour else R.string.hours)
        val minutesSuffix = context.getString(if (minutes == 1L) R.string.minute else R.string.minutes)

        val timeRemaining = context.getString(R.string.countdown_format, days.toString(), daysSuffix, hours.toString(), hoursSuffix, minutes.toString(), minutesSuffix)

        return context.getString(R.string.event_description_format, forkName, forkHeight.toString(), blocksRemaining.toString(), timeRemaining)
    }

}