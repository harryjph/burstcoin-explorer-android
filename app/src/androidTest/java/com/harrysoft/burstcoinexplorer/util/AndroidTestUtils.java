package com.harrysoft.burstcoinexplorer.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.harry1453.burst.explorer.service.BurstServiceProvider;
import com.harry1453.burst.explorer.service.BurstServiceProviders;
import com.harrysoft.burstcoinexplorer.mock.MockConfigRepository;
import com.harrysoft.burstcoinexplorer.mock.MockNetworkService;

public class AndroidTestUtils {
    public static BurstServiceProvider getBurstServiceProvider() {
        Context context = InstrumentationRegistry.getTargetContext();
        return BurstServiceProviders.getBurstServiceProvider(new MockNetworkService(context), new MockConfigRepository(context));
    }
}
