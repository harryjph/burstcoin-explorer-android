package com.harrysoft.burstcoinexplorer;

import com.harrysoft.burstcoinexplorer.test.BurstBlockchainServiceTest;
import com.harrysoft.burstcoinexplorer.test.BurstInfoServiceTest;
import com.harrysoft.burstcoinexplorer.test.BurstNetworkServiceTest;
import com.harrysoft.burstcoinexplorer.test.BurstPriceServiceTest;
import com.harrysoft.burstcoinexplorer.test.DetermineSearchRequestTypeTest;
import com.harrysoft.burstcoinexplorer.test.TransactionTypeUtilsTest;
import com.harrysoft.burstcoinexplorer.test.VersionUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BurstBlockchainServiceTest.class,
        BurstInfoServiceTest.class,
        BurstNetworkServiceTest.class,
        BurstPriceServiceTest.class,
        DetermineSearchRequestTypeTest.class,
        TransactionTypeUtilsTest.class,
        VersionUtilsTest.class,
})

public class AllAndroidTests {
    // Runs all Unit tests that need to be run on an Android device
}
