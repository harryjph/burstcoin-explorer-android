package com.harrysoft.burstcoinexplorer.events.entity;


import com.harry1453.burst.explorer.entity.EventInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EventsList {
    public static final EventsList EMPTY = new EventsList(BigInteger.ZERO, new ArrayList<>());

    public final BigInteger blockHeight;
    public final List<EventInfo> events;

    public EventsList(BigInteger blockHeight, List<EventInfo> events) {
        this.blockHeight = blockHeight;
        this.events = events;
    }
}
