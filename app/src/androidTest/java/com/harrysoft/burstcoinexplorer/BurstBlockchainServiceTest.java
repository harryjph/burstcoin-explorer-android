package com.harrysoft.burstcoinexplorer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.PoCCBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class BurstBlockchainServiceTest {

    private Context context;
    private BurstBlockchainService burstBlockchainService;

    @Before
    public void setUpBurstAPIServiceTest() {
        context = InstrumentationRegistry.getTargetContext();
        burstBlockchainService = new PoCCBlockchainService(context);
    }

    @Test
    public void testBurstAPIServiceFetchRecentBlocks() {
        Single<Block[]> single = burstBlockchainService.fetchRecentBlocks();
        TestObserver testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        List blocks = testObserver.values();

        assertNotSame(0, blocks.size());
    }
}
