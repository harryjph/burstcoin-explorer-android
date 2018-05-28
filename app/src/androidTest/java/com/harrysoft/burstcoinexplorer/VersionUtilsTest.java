package com.harrysoft.burstcoinexplorer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.harrysoft.burstcoinexplorer.util.VersionUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class VersionUtilsTest {

    private Context context;

    @Before
    public void setupVersionUtilsTest() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testVersionUtils() {
        Assert.assertTrue(!TextUtils.isEmpty(VersionUtils.getVersionName(context)));
        Assert.assertTrue(VersionUtils.getVersionCode(context) > 0);
    }

}
