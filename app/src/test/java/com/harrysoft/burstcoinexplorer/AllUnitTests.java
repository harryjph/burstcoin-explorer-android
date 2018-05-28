package com.harrysoft.burstcoinexplorer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BurstAddressTest.class,
        BurstValueTest.class,
        FileSizeUtilsTest.class,
})

public class AllUnitTests {
    // Runs all Unit tests that can be run on a local JVM
}
