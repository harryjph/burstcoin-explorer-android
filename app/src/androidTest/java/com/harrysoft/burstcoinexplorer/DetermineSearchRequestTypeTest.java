package com.harrysoft.burstcoinexplorer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.PoCCBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DetermineSearchRequestTypeTest {

    private Context context;
    private BurstBlockchainService burstBlockchainService;

    @Before
    public void setUpBurstAPIServiceTest() {
        context = InstrumentationRegistry.getTargetContext();
        burstBlockchainService = new PoCCBlockchainService(context);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountRS() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType(context.getString(R.string.donate_address));
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        assertEquals(SearchRequestType.ACCOUNT_RS, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeAccountID() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType("7009665667967103287");
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        assertEquals(SearchRequestType.ACCOUNT_ID, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockNumber() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType("470000");
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        assertThat(type, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeBlockID() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType("9466704733664017405");
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        // In PoCCBlockchainService, it is impossible to separate a BLOCK_ID and a BLOCK_NUMBER
        assertThat(type, anyOf(is(SearchRequestType.BLOCK_ID), is(SearchRequestType.BLOCK_NUMBER)));
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeTransactionID() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType("10489995701880641892");
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        assertEquals(SearchRequestType.TRANSACTION_ID, type);
    }

    @Test
    public void testBurstBlockchainServiceDetermineIDTypeInvalid() {
        Single<SearchRequestType> single = burstBlockchainService.determineSearchRequestType("Not a search term");
        TestObserver<SearchRequestType> testObserver = single.test();

        testObserver.awaitTerminalEvent();

        testObserver.assertNoErrors();

        SearchRequestType type = testObserver.values().get(0);

        assertEquals(SearchRequestType.INVALID, type);
    }
}
