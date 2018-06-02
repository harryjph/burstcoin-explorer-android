package com.harrysoft.burstcoinexplorer.util

import android.content.Context
import android.support.v7.preference.ListPreference
import com.harrysoft.burstcoinexplorer.R
import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CurrencyUtils {

    @JvmStatic
    fun setupCurrencyPreferenceData(context: Context, preferences: PreferenceRepository, lp: ListPreference, currentCurrencyCode: String) {

        val currencyCodes = context.resources.getStringArray(R.array.currencies)

        fun getDisplayInfo(currencyCode: String) : String {
            val currency = Currency.getInstance(currencyCode)
            return context.getString(R.string.currency_format, currency.displayName, currency.currencyCode)
        }

        fun getDisplayInfoList() : Array<String> {
            val currencyDisplayStrings = ArrayList<String>()

            for (currencyCode in currencyCodes) {
                currencyDisplayStrings.add(getDisplayInfo(currencyCode))
            }

            return currencyDisplayStrings.toTypedArray()
        }

        val displayValues = getDisplayInfoList()

        preferences.selectedCurrency = currentCurrencyCode

        val currentDisplayValue = getDisplayInfo(currentCurrencyCode)

        lp.entries = displayValues
        lp.setDefaultValue(currentCurrencyCode)
        lp.value = currentCurrencyCode
        lp.summary = currentDisplayValue
        lp.entryValues = currencyCodes
    }

    @JvmStatic
    fun setupCurrencyPreferenceData(context: Context, preferences: PreferenceRepository, lp: ListPreference) {
        setupCurrencyPreferenceData(context, preferences, lp, preferences.selectedCurrency)
    }

    @JvmStatic
    fun formatCurrencyAmount(currencyCode: String, price: BigDecimal, showDecimals: Boolean): String {
        var currency : Currency? = null
        try {
            currency = Currency.getInstance(currencyCode)
        } catch (ignored: IllegalArgumentException) {}

        return if (currency != null) {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = currency
            if (showDecimals) {
                formatter.minimumFractionDigits = 5
            } else {
                formatter.maximumFractionDigits = 0
            }
            formatter.format(price)
        } else {
            DecimalFormat("0.00000000").format(price)
        }
    }
}