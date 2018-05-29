package com.harrysoft.burstcoinexplorer.test;

import android.support.test.InstrumentationRegistry;

import com.harrysoft.burstcoinexplorer.TestVariables;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.PoCCBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DetermineSearchRequestTypeTest {

    private BurstBlockchainService burstBlockchainService;

    @Before
    public void setUpBurstAPIServiceTest() {
        burstBlockchainService = new PoCCBlockchainService(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountRS() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_ACCOUNT_RS));
        assertEquals(SearchRequestType.ACCOUNT_RS, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountID() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_ACCOUNT_ID));
        assertEquals(SearchRequestType.ACCOUNT_ID, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockHeight() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_BLOCK_HEIGHT));
        assertThat(type, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockID() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_BLOCK_ID));
        // In the PoCCBlockchainService, it is impossible to separate a BLOCK_ID and a BLOCK_NUMBER
        assertThat(type, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeTransactionID() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_TRANSACTION_ID));
        assertEquals(SearchRequestType.TRANSACTION_ID, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeInvalid() {
        SearchRequestType type = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType("Not a search term"));
        assertEquals(SearchRequestType.INVALID, type);
    }
}
