package com.harrysoft.burstcoinexplorer.test;

import android.support.test.runner.AndroidJUnit4;

import com.harry1453.burst.explorer.entity.BurstPrice;
import com.harry1453.burst.explorer.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.util.AndroidTestUtils;
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
        burstPriceService = AndroidTestUtils.getBurstServiceProvider().getBurstPriceService();
    }

    @Test
    public void testBurstPriceServiceFetchPrice() {
        BurstPrice price = SingleTestUtils.testSingle(burstPriceService.fetchPrice("USD"));
        assertEquals("USD", price.currencyCode);
        assertNotNull(price.price);
        assertNotNull(price.marketCapital);
    }
}
