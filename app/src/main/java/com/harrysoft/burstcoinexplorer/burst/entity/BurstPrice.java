package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class BurstPrice implements Parcelable {
    public final String currencyCode;
    public final BigDecimal price;
    public final BigDecimal marketCapital;

    public BurstPrice(String currencyCode, BigDecimal price, BigDecimal marketCapital) {
        this.currencyCode = currencyCode;
        this.price = price;
        this.marketCapital = marketCapital;
    }

    private BurstPrice(Parcel in) {
        this.currencyCode = in.readString();
        this.price = new BigDecimal(in.readString());
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
        dest.writeString(currencyCode);
        dest.writeString(price.toString());
        dest.writeString(marketCapital.toString());
    }
}
