package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigInteger;
import java.util.Objects;

public class EventInfo implements Parcelable {

    public final String name;
    @NonNull
    public final String infoPage;
    public final boolean infoPageSet;
    @NonNull
    public final BigInteger blockHeight;
    public final boolean blockHeightSet;

    public EventInfo(String name, @Nullable String infoPage, @Nullable BigInteger blockHeight) {
        this.name = name;

        if (infoPage == null || Objects.equals(infoPage, "")) {
            this.infoPage = "";
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

    private EventInfo(Parcel in) {
        name = in.readString();
        infoPage = in.readString();
        infoPageSet = in.readByte() != 0;
        blockHeight = new BigInteger(in.readString());
        blockHeightSet = in.readByte() != 0;
    }

    public static final Creator<EventInfo> CREATOR = new Creator<EventInfo>() {
        @Override
        public EventInfo createFromParcel(Parcel in) {
            return new EventInfo(in);
        }

        @Override
        public EventInfo[] newArray(int size) {
            return new EventInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(infoPage);
        dest.writeByte((byte) (infoPageSet ? 1 : 0));
        dest.writeString(blockHeight.toString());
        dest.writeByte((byte) (blockHeightSet ? 1 : 0));
    }
}
