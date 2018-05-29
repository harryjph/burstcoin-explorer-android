package com.harrysoft.burstcoinexplorer.test;

import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.burst.api.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.api.RepoInfoService;
import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class BurstInfoServiceTest {

    private BurstInfoService burstInfoService;

    @Before
    public void setUpBurstInfoServiceTest() {
        burstInfoService = new RepoInfoService(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testBurstInfoServiceTestGetEvents() {
        EventInfo[] eventInfoArray = SingleTestUtils.testSingle(burstInfoService.getEvents());
        assertNotSame(0, eventInfoArray.length);
        for (EventInfo eventInfo : eventInfoArray) {
            assertNotNull(eventInfo);
            if (eventInfo.blockHeightSet) {
                assertNotSame(BigInteger.ZERO, eventInfo.blockHeight);
            } else {
                assertEquals(BigInteger.ZERO, eventInfo.blockHeight);
            }
            if (eventInfo.infoPageSet) {
                assertNotSame(Uri.EMPTY, eventInfo.infoPage);
            } else {
                assertEquals(Uri.EMPTY, eventInfo.infoPage);
            }
        }
    }
}
