package com.harrysoft.burstcoinexplorer.test;

import android.support.test.runner.AndroidJUnit4;

import com.harry1453.burst.explorer.entity.EventInfo;
import com.harry1453.burst.explorer.service.BurstInfoService;
import com.harrysoft.burstcoinexplorer.util.AndroidTestUtils;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class BurstInfoServiceTest {

    private BurstInfoService burstInfoService;

    @Before
    public void setUpBurstInfoServiceTest() {
        burstInfoService = AndroidTestUtils.getBurstServiceProvider().getBurstInfoService();
    }

    @Test
    public void testBurstInfoServiceTestGetEvents() {
        List<EventInfo> eventInfoArray = SingleTestUtils.testSingle(burstInfoService.getEvents());
        assertNotSame(0, eventInfoArray.size());
        for (EventInfo eventInfo : eventInfoArray) {
            assertNotNull(eventInfo);
            if (eventInfo.blockHeightSet) {
                assertNotSame(BigInteger.ZERO, eventInfo.blockHeight);
            } else {
                assertEquals(BigInteger.ZERO, eventInfo.blockHeight);
            }
            if (eventInfo.infoPageSet) {
                assertNotSame("", eventInfo.infoPage);
            } else {
                assertEquals("", eventInfo.infoPage);
            }
        }
    }
}
