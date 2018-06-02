package com.harrysoft.burstcoinexplorer.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.burst.service.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.burst.service.PoCCNetworkService;
import com.harrysoft.burstcoinexplorer.util.SingleTestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BurstNetworkServiceTest {

    private BurstNetworkService burstNetworkService;

    @Before
    public void setUpBurstNetworkServiceTest() {
        burstNetworkService = new PoCCNetworkService(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testBurstNetworkServiceFetchNetworkStatus() {
        NetworkStatus networkStatus = SingleTestUtils.testSingle(burstNetworkService.fetchNetworkStatus());
        assertNotNull(networkStatus.blockHeight);
        assertNotNull(networkStatus.peersActiveInCountry);
        assertNotNull(networkStatus.peersData);
        assertNotNull(networkStatus.peersData.peersStatus);
        List<NetworkStatus.BrokenPeer> brokenPeerList = networkStatus.getBrokenPeersFromMap();
        assertNotNull(brokenPeerList);
        for(NetworkStatus.BrokenPeer brokenPeer : brokenPeerList) {
            assertNotNull(brokenPeer);
        }
        List<NetworkStatus.PeersData.PeerVersion> peerVersionList = networkStatus.peersData.getPeerVersionsFromMap();
        assertNotNull(peerVersionList);
        for(NetworkStatus.PeersData.PeerVersion peerVersion : peerVersionList) {
            assertNotNull(peerVersion);
        }
    }
}
