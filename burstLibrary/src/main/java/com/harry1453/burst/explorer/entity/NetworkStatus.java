package com.harry1453.burst.explorer.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class NetworkStatus {

    @SerializedName("broken_peers")
    public Map<String, BrokenPeer> brokenPeers;
    @SerializedName("peers_at")
    public PeersData peersData;
    @SerializedName("peers_active_in_country")
    public Map<String, BigInteger> peersActiveInCountry;
    @SerializedName("current_height")
    public BigInteger blockHeight;

    public static class BrokenPeer {
        public String address; // not set by Gson
        public long height;
        @SerializedName("country_code")
        public String countryCode;
        public String platform;
        public String status;
        public String version;
    }

    public ArrayList<BrokenPeer> getBrokenPeersFromMap() {
        ArrayList<BrokenPeer> brokenPeersList = new ArrayList<>();

        for (Map.Entry<String, BrokenPeer> entry : brokenPeers.entrySet()) {
            BrokenPeer brokenPeer = entry.getValue();
            brokenPeer.address = entry.getKey();
            brokenPeersList.add(brokenPeer);
        }

        return brokenPeersList;
    }

    public static class PeersData {
        @SerializedName("status")
        public PeersStatus peersStatus;
        @SerializedName("version")
        public Map<String, Long> versions;

        public static class PeersStatus {
            public long stuck;
            public long unreachable;
            public long fork;
            public long valid;
            public long resync;

            public long total() {
                return stuck + unreachable + fork + valid + resync;
            }
        }

        public ArrayList<PeerVersion> getPeerVersionsFromMap() {
            ArrayList<PeerVersion> peerVersions = new ArrayList<>();

            for (Map.Entry<String, Long> entry : versions.entrySet()) {
                peerVersions.add(new PeerVersion(entry.getKey(), entry.getValue()));
            }

            return peerVersions;
        }

        public static class PeerVersion {
            public final String version;
            public final long count;

            PeerVersion(String version, long count) {
                this.version = version;
                this.count = count;
            }
        }
    }
}
