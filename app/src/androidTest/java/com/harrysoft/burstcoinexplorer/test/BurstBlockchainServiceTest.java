package com.harrysoft.burstcoinexplorer.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.TestVariables;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.NodeBlockchainService;
import com.harrysoft.burstcoinexplorer.main.repository.SharedPreferenceRepository;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.List;

import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class BurstBlockchainServiceTest {

    private BurstBlockchainService burstBlockchainService;

    @Before
    public void setUpBurstAPIServiceTest() {
        Context context = InstrumentationRegistry.getTargetContext();
        burstBlockchainService = new NodeBlockchainService(new SharedPreferenceRepository(context), context);
    }

    @Test
    public void testBurstAPIServiceFetchRecentBlocks() {
        List<Block> blocks = SingleTestUtils.testSingle(burstBlockchainService.fetchRecentBlocks());
        assertNotSame(0, blocks.size());
    }

    @Test
    public void testBurstAPIServiceFetchBlockByHeight() {
        SingleTestUtils.testSingle(burstBlockchainService.fetchBlockByHeight(new BigInteger(TestVariables.EXAMPLE_BLOCK_HEIGHT)));
    }

    @Test
    public void testBurstAPIServiceFetchBlockByID() {
        SingleTestUtils.testSingle(burstBlockchainService.fetchBlockByID(new BigInteger(TestVariables.EXAMPLE_BLOCK_ID)));
    }

    @Test
    public void testBurstAPIServiceFetchAccount() {
        SingleTestUtils.testSingle(burstBlockchainService.fetchAccount(new BigInteger(TestVariables.EXAMPLE_ACCOUNT_ID)));
    }

    @Test
    public void testBurstAPIServiceFetchAccountTransactions() {
        SingleTestUtils.testSingle(burstBlockchainService.fetchAccountTransactions(new BigInteger(TestVariables.EXAMPLE_ACCOUNT_ID)));
    }

    @Test
    public void testBurstAPIServiceFetchTransaction() {
        SingleTestUtils.testSingle(burstBlockchainService.fetchTransaction(new BigInteger(TestVariables.EXAMPLE_TRANSACTION_ID)));
    }
}
