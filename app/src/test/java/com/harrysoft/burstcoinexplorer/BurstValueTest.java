package com.harrysoft.burstcoinexplorer;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BurstValueTest {

    @Test
    public void testBurstValue() throws Exception {
        BurstValue burstValue = new BurstValue("1000000000");
        assertEquals("10", burstValue.toUnformattedString());
        assertEquals("10 BURST", burstValue.toString());

        burstValue = new BurstValue("1010000000");
        assertEquals("10.1", burstValue.toUnformattedString());
        assertEquals("10.1 BURST", burstValue.toString());

        burstValue = new BurstValue("1000100000");
        assertEquals("10.001", burstValue.toUnformattedString());
        assertEquals("10.001 BURST", burstValue.toString());

        burstValue = new BurstValue("1000050000");
    }

    @Test
    public void testBurstValueRecreate() {
        BurstValue burstValue = new BurstValue("1234567890");

        BurstValue recreatedBurstValue = BurstValue.createWithoutDividing(burstValue.toUnformattedString());

        assertEquals("12.3456789", recreatedBurstValue.toUnformattedString());
        assertEquals("12.346 BURST", recreatedBurstValue.toString());
    }
}