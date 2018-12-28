package com.harrysoft.burstcoinexplorer.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import burst.kit.entity.BurstTimestamp;

public class TimestampUtils {
    public static String formatBurstTimestamp(BurstTimestamp timestamp) {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy HH:mm:ss").toFormatter().withZone(DateTimeZone.UTC);
        return new DateTime(timestamp.getAsDate()).toString(dateTimeFormatter);
    }
}
