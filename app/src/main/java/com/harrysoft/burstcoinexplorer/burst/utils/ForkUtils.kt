package com.harrysoft.burstcoinexplorer.burst.utils

import android.content.Context
import com.harrysoft.burstcoinexplorer.R
import java.math.BigInteger

object ForkUtils {

    @JvmStatic
    fun formatForkInfo(context: Context, currentBlockHeight: BigInteger, forkName: String, forkHeight: BigInteger): String {
        val blocksRemaining = forkHeight.subtract(currentBlockHeight)
        if (blocksRemaining.compareTo(BigInteger.ZERO) < 1) {
            return context.getString(R.string.time_until_fork_forked, forkName)
        }

        val timeLeftInMinutes = blocksRemaining.multiply(BigInteger("4"))

        val hoursAndMinutes = timeLeftInMinutes.divideAndRemainder(BigInteger("60"))
        val daysAndHours = hoursAndMinutes[0].divideAndRemainder(BigInteger("24"))
        val days = daysAndHours[0].toLong()
        val hours = daysAndHours[1].toLong()
        val minutes = hoursAndMinutes[1].toLong()

        val daysSuffix = context.getString(if (days == 1L) R.string.day else R.string.days)
        val hoursSuffix = context.getString(if (hours == 1L) R.string.hour else R.string.hours)
        val minutesSuffix = context.getString(if (minutes == 1L) R.string.minute else R.string.minutes)

        val timeRemaining = context.getString(R.string.fork_countdown_format, days.toString(), daysSuffix, hours.toString(), hoursSuffix, minutes.toString(), minutesSuffix)

        return context.getString(R.string.time_until_fork_format, forkName, forkHeight, blocksRemaining, timeRemaining)
    }

}