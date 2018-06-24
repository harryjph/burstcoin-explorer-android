package com.harry1453.burst.explorer.entity;

import java.math.BigInteger;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class EventInfo {

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
}