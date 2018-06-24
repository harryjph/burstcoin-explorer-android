package com.harrysoft.burstcoinexplorer.test;

import com.harry1453.burst.explorer.entity.SearchRequestType;
import com.harry1453.burst.explorer.entity.SearchResult;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.TestVariables;
import com.harrysoft.burstcoinexplorer.util.AndroidTestUtils;
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
        burstBlockchainService = AndroidTestUtils.getBurstServiceProvider().getBurstBlockchainService();
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountRS() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_ACCOUNT_RS));
        assertEquals(SearchRequestType.ACCOUNT_RS, result.requestType);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountID() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_ACCOUNT_ID));
        assertEquals(SearchRequestType.ACCOUNT_ID, result.requestType);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockHeight() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_BLOCK_HEIGHT));
        assertThat(result.requestType, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockID() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_BLOCK_ID));
        // In the NodeBlockchainService, it is impossible to separate a BLOCK_ID and a BLOCK_NUMBER
        assertThat(result.requestType, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeTransactionID() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType(TestVariables.EXAMPLE_TRANSACTION_ID));
        assertEquals(SearchRequestType.TRANSACTION_ID, result.requestType);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeInvalid() {
        SearchResult result = SingleTestUtils.testSingle(burstBlockchainService.determineSearchRequestType("Not a search term"));
        assertEquals(SearchRequestType.INVALID, result.requestType);
    }
}
