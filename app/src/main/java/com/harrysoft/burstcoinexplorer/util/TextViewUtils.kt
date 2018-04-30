package com.harrysoft.burstcoinexplorer.util

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView

object TextViewUtils {
    @JvmStatic
    fun makeTextViewHyperlink(tv: TextView) {
        val ssb = SpannableStringBuilder()
        ssb.append(tv.text)
        ssb.setSpan(URLSpan("#"), 0, ssb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.setText(ssb, TextView.BufferType.SPANNABLE)
    }
}