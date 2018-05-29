package com.harrysoft.burstcoinexplorer.runner;

import com.harrysoft.burstcoinexplorer.test.DetermineSearchRequestTypeTest;
import com.harrysoft.burstcoinexplorer.test.TransactionTypeUtilsTest;
import com.harrysoft.burstcoinexplorer.test.VersionUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DetermineSearchRequestTypeTest.class,
        TransactionTypeUtilsTest.class,
        VersionUtilsTest.class,
})

public class OfflineAndroidTests {
    // Runs all android tests that do not require an internet connection to pass
}
