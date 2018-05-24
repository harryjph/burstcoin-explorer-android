package com.harrysoft.burstcoinexplorer.util

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.ListPreference
import com.harrysoft.burstcoinexplorer.R
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

object CurrencyUtils {

    @JvmStatic
    fun getCurrencyCodeList(context: Context) : Array<String> {
        return context.resources.getStringArray(R.array.currencies)
    }

    @JvmStatic
    fun getDisplayInfoList(context: Context, currencyCodes: Array<String>) : Array<String> {
        val currencyDisplayStrings = ArrayList<String>()

        for (currencyCode in currencyCodes) {
            currencyDisplayStrings.add(getDisplayInfo(context, currencyCode))
        }

        return currencyDisplayStrings.toTypedArray()
    }

    @JvmStatic
    fun getDisplayInfo(context: Context, currencyCode: String) : String {
        val currency = Currency.getInstance(currencyCode)
        return context.getString(R.string.currency_format, currency.displayName, currency.currencyCode)
    }

    @JvmStatic
    fun setupCurrencyPreferenceData(context: Context, preferences: SharedPreferences, lp: ListPreference, currentCurrencyCode: String) {
        val currencyCodes = CurrencyUtils.getCurrencyCodeList(context)
        val displayValues = CurrencyUtils.getDisplayInfoList(context, currencyCodes)

        preferences.edit().putString(context.getString(R.string.currency), currentCurrencyCode).apply()

        val currentDisplayValue = CurrencyUtils.getDisplayInfo(context, currentCurrencyCode)

        lp.entries = displayValues
        lp.setDefaultValue(currentCurrencyCode)
        lp.value = currentCurrencyCode
        lp.summary = currentDisplayValue
        lp.entryValues = currencyCodes
    }

    @JvmStatic
    fun setupCurrencyPreferenceData(context: Context, preferences: SharedPreferences, lp: ListPreference) {
        setupCurrencyPreferenceData(context, preferences, lp, preferences.getString(context.getString(R.string.currency), context.getString(R.string.currency_default)))
    }

    @JvmStatic
    fun formatFiatPrice(currency: Currency, price: BigDecimal): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        formatter.currency = currency
        return formatter.format(price)
    }

    @JvmStatic
    fun formatMarketCap(currency: Currency, marketCap: BigDecimal): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        formatter.currency = currency
        return formatter.format(marketCap)
    }

}