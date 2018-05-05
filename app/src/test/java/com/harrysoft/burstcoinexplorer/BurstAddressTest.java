package com.harrysoft.burstcoinexplorer;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BurstAddressTest {

    @Test
    public void testBurstAddress() {
        BurstAddress ba = new BurstAddress("16484518239061020631");
        assertEquals("16484518239061020631", ba.getNumericID().toString());
        assertEquals("W5YR-ZZQC-KUBJ-G78KB", ba.getRawAddress());
        assertEquals("BURST-W5YR-ZZQC-KUBJ-G78KB", ba.getFullAddress());
    }

}
