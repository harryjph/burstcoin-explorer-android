package com.harrysoft.burstcoinexplorer.burst.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigInteger;

public class ForkInfo implements Parcelable {

    public final String name;
    @NonNull
    public final Uri infoPage;
    public final boolean infoPageSet;
    @NonNull
    public final BigInteger blockHeight;
    public final boolean blockHeightSet;

    public ForkInfo(String name, @Nullable Uri infoPage, @Nullable BigInteger blockHeight) {
        this.name = name;

        if (infoPage == null) {
            this.infoPage = Uri.EMPTY;
            infoPageSet = false;
        } else {
            this.infoPage = infoPage;
            infoPageSet = true;
        }

        if (blockHeight == null) {
            this.blockHeight = BigInteger.ZERO;
            blockHeightSet = false;
        } else {
            this.blockHeight = blockHeight;
            blockHeightSet = true;
        }
    }

    private ForkInfo(Parcel in) {
        name = in.readString();
        infoPage = in.readParcelable(Uri.class.getClassLoader());
        infoPageSet = in.readByte() != 0;
        blockHeight = new BigInteger(in.readString());
        blockHeightSet = in.readByte() != 0;
    }

    public static final Creator<ForkInfo> CREATOR = new Creator<ForkInfo>() {
        @Override
        public ForkInfo createFromParcel(Parcel in) {
            return new ForkInfo(in);
        }

        @Override
        public ForkInfo[] newArray(int size) {
            return new ForkInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(infoPage, flags);
        dest.writeByte((byte) (infoPageSet ? 1 : 0));
        dest.writeString(blockHeight.toString());
        dest.writeByte((byte) (blockHeightSet ? 1 : 0));
    }
}
