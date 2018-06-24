package com.harrysoft.burstcoinexplorer.test;

import com.harry1453.burst.BurstUtils;
import com.harry1453.burst.explorer.entity.BurstAddress;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BurstAddressTest {
    @Test
    public void testBurstAddressFromNumeric() {
        BurstAddress ba = new BurstAddress(new BigInteger("16484518239061020631"));
        assertEquals("16484518239061020631", ba.getNumericID().toString());
        assertEquals("W5YR-ZZQC-KUBJ-G78KB", ba.getRawAddress());
        assertEquals("BURST-W5YR-ZZQC-KUBJ-G78KB", ba.getFullAddress());
    }

    @Test
    public void testReedSolomonFromRS() throws BurstUtils.ReedSolomon.DecodeException {
        BurstAddress ba = new BurstAddress("BURST-W5YR-ZZQC-KUBJ-G78KB");
        assertEquals("16484518239061020631", ba.getNumericID().toString());
        assertEquals("W5YR-ZZQC-KUBJ-G78KB", ba.getRawAddress());
        assertEquals("BURST-W5YR-ZZQC-KUBJ-G78KB", ba.getFullAddress());
    }
}
