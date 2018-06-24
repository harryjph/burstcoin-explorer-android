package com.harrysoft.burstcoinexplorer.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.harrysoft.burstcoinexplorer.burst.service.BurstServiceProvider;
import com.harrysoft.burstcoinexplorer.burst.service.BurstServiceProviders;
import com.harrysoft.burstcoinexplorer.mock.MockNetworkService;
import com.harrysoft.burstcoinexplorer.mock.MockPreferenceRepository;

public class AndroidTestUtils {
    public static BurstServiceProvider getBurstServiceProvider() {
        Context context = InstrumentationRegistry.getTargetContext();
        return BurstServiceProviders.getBurstServiceProvider(BurstServiceProviders.getObjectService(new MockNetworkService(context)), new MockPreferenceRepository(context));
    }
}
