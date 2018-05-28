package com.harrysoft.burstcoinexplorer;

import com.harrysoft.burstcoinexplorer.util.VersionUtils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BurstBlockchainServiceTest.class,
        DetermineSearchRequestTypeTest.class,
        VersionUtils.class,
})

public class AllAndroidTests {
    // Runs all Unit tests that can be run on a local JVM
}
