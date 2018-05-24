package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Currency;

public class BurstPrice implements Parcelable {
    public final Currency fiatCurrency;
    public final BigDecimal priceFiat;
    public final BigDecimal priceBtc;
    public final BigDecimal marketCapital;

    public BurstPrice(String fiatCurrencyCode, BigDecimal priceFiat, BigDecimal priceBtc, BigDecimal marketCapital) {
        this.fiatCurrency = Currency.getInstance(fiatCurrencyCode);
        this.priceFiat = priceFiat;
        this.priceBtc = priceBtc;
        this.marketCapital = marketCapital;
    }

    private BurstPrice(Parcel in) {
        this.fiatCurrency = Currency.getInstance(in.readString());
        this.priceFiat = new BigDecimal(in.readString());
        this.priceBtc = new BigDecimal(in.readString());
        this.marketCapital = new BigDecimal(in.readString());
    }

    public static final Creator<BurstPrice> CREATOR = new Creator<BurstPrice>() {
        @Override
        public BurstPrice createFromParcel(Parcel in) {
            return new BurstPrice(in);
        }

        @Override
        public BurstPrice[] newArray(int size) {
            return new BurstPrice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fiatCurrency.getCurrencyCode());
        dest.writeString(priceFiat.toString());
        dest.writeString(priceBtc.toString());
        dest.writeString(marketCapital.toString());
    }
}
