package com.harrysoft.burstcoinexplorer.test;

import com.harry1453.burst.explorer.entity.BurstValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BurstValueTest {

    @Test
    public void testBurstValueFromNQT() {
        BurstValue burstValue = BurstValue.fromNQT("1000000000");
        assertEquals("10", burstValue.toUnformattedString());
        assertEquals("10 BURST", burstValue.toString());

        burstValue = BurstValue.fromNQT("1010000000");
        assertEquals("10.1", burstValue.toUnformattedString());
        assertEquals("10.1 BURST", burstValue.toString());

        burstValue = BurstValue.fromNQT("1000100000");
        assertEquals("10.001", burstValue.toUnformattedString());
        assertEquals("10.001 BURST", burstValue.toString());

        burstValue = BurstValue.fromNQT("1000050000");
        assertEquals("10.0005", burstValue.toUnformattedString());
        assertEquals("10.001 BURST", burstValue.toString());

        burstValue = BurstValue.fromNQT("0");
        assertEquals("0", burstValue.toUnformattedString());
        assertEquals("0 BURST", burstValue.toString());
    }

    @Test
    public void testBurstValueFromBurst() {
        BurstValue burstValue = BurstValue.fromBurst("10");
        assertEquals("10", burstValue.toUnformattedString());
        assertEquals("10 BURST", burstValue.toString());

        burstValue = BurstValue.fromBurst("10.1");
        assertEquals("10.1", burstValue.toUnformattedString());
        assertEquals("10.1 BURST", burstValue.toString());

        burstValue = BurstValue.fromBurst("10.001");
        assertEquals("10.001", burstValue.toUnformattedString());
        assertEquals("10.001 BURST", burstValue.toString());

        burstValue = BurstValue.fromBurst("10.0005");
        assertEquals("10.0005", burstValue.toUnformattedString());
        assertEquals("10.001 BURST", burstValue.toString());

        burstValue = BurstValue.fromBurst("0");
        assertEquals("0", burstValue.toUnformattedString());
        assertEquals("0 BURST", burstValue.toString());
    }

    @Test
    public void testBurstValueRecreate() {
        BurstValue burstValue = BurstValue.fromNQT("1234567890");

        BurstValue recreatedBurstValue = BurstValue.fromBurst(burstValue.toUnformattedString());

        assertEquals("12.3456789", recreatedBurstValue.toUnformattedString());
        assertEquals("12.346 BURST", recreatedBurstValue.toString());
    }

    @Test
    public void testBurstValueBadInput() {
        BurstValue fromBurst = BurstValue.fromBurst("not a number");
        BurstValue fromNQT = BurstValue.fromNQT("not a number");

        assertEquals("0", fromBurst.toUnformattedString());
        assertEquals("0", fromNQT.toUnformattedString());
    }
}