package com.harrysoft.burstcoinexplorer.burst.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class NetworkStatus {

    @SerializedName("broken_peers")
    public Map<String, BrokenPeer> brokenPeers;
    @SerializedName("peers_at")
    public PeersData peersData;
    @SerializedName("peers_active_in_country")
    public Map<String, BigDecimal> peersActiveInCountry; // todo implement this

    public static class BrokenPeer implements Parcelable {
        public String address; // not set by Gson
        public long height;
        @SerializedName("country_code")
        public String countryCode;
        public String platform;
        public String status;
        public String version;

        BrokenPeer(Parcel in) {
            address = in.readString();
            height = in.readLong();
            countryCode = in.readString();
            platform = in.readString();
            status = in.readString();
            version = in.readString();
        }

        public static final Creator<BrokenPeer> CREATOR = new Creator<BrokenPeer>() {
            @Override
            public BrokenPeer createFromParcel(Parcel in) {
                return new BrokenPeer(in);
            }

            @Override
            public BrokenPeer[] newArray(int size) {
                return new BrokenPeer[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address);
            dest.writeLong(height);
            dest.writeString(countryCode);
            dest.writeString(platform);
            dest.writeString(status);
            dest.writeString(version);
        }
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
        @SerializedName("current_height")
        public long blockHeight;

        public static class PeersStatus implements Parcelable {
            public long stuck;
            public long unreachable;
            public long fork;
            public long valid;
            public long resync;

            PeersStatus(Parcel in) {
                stuck = in.readLong();
                unreachable = in.readLong();
                fork = in.readLong();
                valid = in.readLong();
                resync = in.readLong();
            }

            public static final Creator<PeersStatus> CREATOR = new Creator<PeersStatus>() {
                @Override
                public PeersStatus createFromParcel(Parcel in) {
                    return new PeersStatus(in);
                }

                @Override
                public PeersStatus[] newArray(int size) {
                    return new PeersStatus[size];
                }
            };

            public long total() {
                return stuck + unreachable + fork + valid + resync;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(stuck);
                dest.writeLong(unreachable);
                dest.writeLong(fork);
                dest.writeLong(valid);
                dest.writeLong(resync);
            }
        }

        public ArrayList<PeerVersion> getPeerVersionsFromMap() {
            ArrayList<PeerVersion> peerVersions = new ArrayList<>();

            for (Map.Entry<String, Long> entry : versions.entrySet()) {
                peerVersions.add(new PeerVersion(entry.getKey(), entry.getValue()));
            }

            return peerVersions;
        }

        public static class PeerVersion implements Parcelable {
            public String version;
            public long count;

            PeerVersion(String version, long count) {
                this.version = version;
                this.count = count;
            }

            PeerVersion(Parcel in) {
                version = in.readString();
                count = in.readLong();
            }

            public static final Creator<PeerVersion> CREATOR = new Creator<PeerVersion>() {
                @Override
                public PeerVersion createFromParcel(Parcel in) {
                    return new PeerVersion(in);
                }

                @Override
                public PeerVersion[] newArray(int size) {
                    return new PeerVersion[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(version);
                dest.writeLong(count);
            }
        }
    }
}
