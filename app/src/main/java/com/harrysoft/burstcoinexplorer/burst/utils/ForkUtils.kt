package com.harrysoft.burstcoinexplorer.burst.utils

import android.content.Context
import com.harrysoft.burstcoinexplorer.R
import java.math.BigInteger

object ForkUtils {

    @JvmStatic
    fun formatNextFork(context: Context, currentBlockHeight: BigInteger): String {
        val nextForkHeight = BigInteger(context.getString(R.string.fork_poc2_height))
        val nextForkName = context.getString(R.string.fork_poc2_name)

        val blocksRemaining = nextForkHeight.subtract(currentBlockHeight)
        if (blocksRemaining.compareTo(BigInteger.ZERO) < 1) {
            return context.getString(R.string.time_until_fork_forked, nextForkName)
        }

        val timeLeftInMinutes = blocksRemaining.multiply(BigInteger("4"))

        val hoursAndMinutes = timeLeftInMinutes.divideAndRemainder(BigInteger("60"))
        val daysAndHours = hoursAndMinutes[0].divideAndRemainder(BigInteger("24"))
        val days = daysAndHours[0].toLong()
        val hours = daysAndHours[1].toLong()
        val minutes = hoursAndMinutes[1].toLong()

        val hoursSuffix = context.getString(if (days == 1L) R.string.day else R.string.days)
        val daysSuffix = context.getString(if (hours == 1L) R.string.hour else R.string.hours)
        val minutesSuffix = context.getString(if (minutes == 1L) R.string.minute else R.string.minutes)

        val timeRemaining = context.getString(R.string.fork_countdown_format, days.toString(), daysSuffix, hours.toString(), hoursSuffix, minutes.toString(), minutesSuffix)

        return context.getString(R.string.time_until_fork_format, nextForkName, nextForkHeight, blocksRemaining, timeRemaining)
    }

}