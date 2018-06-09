package com.harrysoft.burstcoinexplorer.burst.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.DateTime;

import java.math.BigInteger;

public class EventInfo implements Parcelable {

    public final String name;
    @NonNull
    public final Uri infoPage;
    public final boolean infoPageSet;
    @NonNull
    public final BigInteger blockHeight;
    public final boolean blockHeightSet;

    public EventInfo(String name, @Nullable Uri infoPage, @Nullable BigInteger blockHeight) {
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

        DateTime d1 = DateTime.now();
        String s = d1.toString();
        Log.e("s", "s: " + s);
        DateTime  d = DateTime.parse(s);
        Log.e("s", "s1: " + d.toString());
    }

    private EventInfo(Parcel in) {
        name = in.readString();
        infoPage = in.readParcelable(Uri.class.getClassLoader());
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
        dest.writeParcelable(infoPage, flags);
        dest.writeByte((byte) (infoPageSet ? 1 : 0));
        dest.writeString(blockHeight.toString());
        dest.writeByte((byte) (blockHeightSet ? 1 : 0));
    }
}
