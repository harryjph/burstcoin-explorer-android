package com.harrysoft.burstcoinexplorer.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.harry1453.burst.explorer.service.BurstServiceProvider;
import com.harry1453.burst.explorer.service.BurstServiceProviders;
import com.harrysoft.burstcoinexplorer.mock.MockNetworkService;
import com.harrysoft.burstcoinexplorer.mock.MockPreferenceRepository;

public class AndroidTestUtils {
    public static BurstServiceProvider getBurstServiceProvider() {
        Context context = InstrumentationRegistry.getTargetContext();
        return BurstServiceProviders.getBurstServiceProvider(BurstServiceProviders.getObjectService(new MockNetworkService(context)), new MockPreferenceRepository(context));
    }
}
