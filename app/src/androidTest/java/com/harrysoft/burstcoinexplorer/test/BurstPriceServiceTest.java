package com.harrysoft.burstcoinexplorer.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BurstPriceServiceTest {

    private BurstPriceService burstPriceService;

    @Before
    public void setUpBurstPriceServiceTest() {
        burstPriceService = new CMCPriceService(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testBurstPriceServiceFetchPrice() {
        BurstPrice price = SingleTestUtils.testSingle(burstPriceService.fetchPrice("USD"));
        assertEquals("USD", price.currencyCode);
        assertNotNull(price.price);
        assertNotNull(price.marketCapital);
    }
}
