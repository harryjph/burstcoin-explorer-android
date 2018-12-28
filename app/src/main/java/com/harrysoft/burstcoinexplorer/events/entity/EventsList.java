package com.harrysoft.burstcoinexplorer.events.entity;


import com.harry1453.burst.explorer.entity.EventInfo;

import java.util.ArrayList;
import java.util.List;

public class EventsList {
    public static final EventsList EMPTY = new EventsList(0, new ArrayList<>());

    public final long blockHeight;
    public final List<EventInfo> events;

    public EventsList(long blockHeight, List<EventInfo> events) {
        this.blockHeight = blockHeight;
        this.events = events;
    }
}
