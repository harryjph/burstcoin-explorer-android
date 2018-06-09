package com.harrysoft.burstcoinexplorer.util

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository

object TextViewUtils {
    @JvmStatic
    fun setupTextViewAsHyperlink(textView: TextView, onClickListener: View.OnClickListener) {
        val ssb = SpannableStringBuilder()
        ssb.append(textView.text)
        ssb.setSpan(URLSpan("#"), 0, ssb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(ssb, TextView.BufferType.SPANNABLE)
        textView.setOnClickListener(onClickListener)
    }

    @JvmStatic
    fun setupTextViewAsCopyable(clipboardRepository: ClipboardRepository, textView: TextView, copiedText: String) {
        textView.setOnLongClickListener({clipboardRepository.copyToClipboard(copiedText, copiedText); true})
    }
}