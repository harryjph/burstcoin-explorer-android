package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class BurstPrice implements Parcelable {
    public final BigDecimal priceUsd;
    public final BigDecimal priceBtc;
    public final BigDecimal marketCapital;

    public BurstPrice(BigDecimal priceUsd, BigDecimal priceBtc, BigDecimal marketCapital) {
        this.priceUsd = priceUsd;
        this.priceBtc = priceBtc;
        this.marketCapital = marketCapital;
    }

    private BurstPrice(Parcel in) {
        this.priceUsd = new BigDecimal(in.readString());
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
        dest.writeString(priceUsd.toString());
        dest.writeString(priceBtc.toString());
        dest.writeString(marketCapital.toString());
    }
}
