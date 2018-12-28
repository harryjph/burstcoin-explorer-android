package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.persistence.room.TypeConverter;

import burst.kit.entity.BurstAddress;
import burst.kit.entity.BurstID;
import burst.kit.entity.BurstValue;

class TypeConverters {

    @TypeConverter
    public static BurstAddress burstAddressFromString(String value) {
        return value == null ? BurstAddress.fromId(new BurstID("0")) : BurstAddress.fromId(new BurstID(value));
    }

    @TypeConverter
    public static String burstAddressToString(BurstAddress value) {
        return value == null ? null : value.getID();
    }

    @TypeConverter
    public static BurstValue burstValueFromString(String value) {
        return value == null ? null : BurstValue.fromBurst(value);
    }

    @TypeConverter
    public static String burstValueToString(BurstValue value) {
        return value == null ? null : value.toUnformattedString();
    }
}
