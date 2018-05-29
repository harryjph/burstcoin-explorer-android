package com.harrysoft.burstcoinexplorer.runner;

import com.harrysoft.burstcoinexplorer.test.BurstAddressTest;
import com.harrysoft.burstcoinexplorer.test.BurstValueTest;
import com.harrysoft.burstcoinexplorer.test.FileSizeUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BurstAddressTest.class,
        BurstValueTest.class,
        FileSizeUtilsTest.class,
})

public class AllUnitTests {
    // Runs all unit tests
}
