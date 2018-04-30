package com.harrysoft.burstcoinexplorer.observe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.pieutils.PieUtils;
import com.harrysoft.burstcoinexplorer.observe.pieutils.RemovableLabelPieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ObserveVersionsFragment extends ObserveSubFragment {

    private final String peerVersionsKey = "peerVersions";

    private PieChart peerVersionPieChart;

    private ArrayList<NetworkStatus.PeersData.PeerVersion> peerVersions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            peerVersions = savedInstanceState.getParcelableArrayList(peerVersionsKey);
        }

        View view = inflater.inflate(R.layout.fragment_observe_version, container, false);

        peerVersionPieChart = view.findViewById(R.id.observe_peer_version_pie);

        updatePie();

        setupRefresh(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(peerVersionsKey, peerVersions);
    }

    public void onNetworkStatus(NetworkStatus networkStatus) {
        peerVersions = networkStatus.peersData.getPeerVersionsFromMap();

        updatePie();
    }

    @Override
    protected void onRefreshError(Throwable error, boolean refreshing) {
        PieUtils.setupPieError(getContext(), peerVersionPieChart, error);
    }

    private void updatePie() {
        if (peerVersions == null || peerVersionPieChart == null) {
            return;
        }

        ArrayList<PieEntry> pieEntryList = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        long totalPeers = 0;

        for (NetworkStatus.PeersData.PeerVersion peerVersion : peerVersions) {
            totalPeers += peerVersion.count;
        }

        for (NetworkStatus.PeersData.PeerVersion peerVersion : peerVersions) {
            String peerVersionPercent = decimalFormat.format((float) peerVersion.count / ((float) totalPeers) * 100f);

            pieEntryList.add(new RemovableLabelPieEntry((float) peerVersion.count, getString(R.string.observe_node_label, peerVersion.version, peerVersionPercent)));
        }

        PieUtils.setupPieChart(getContext(), peerVersionPieChart, pieEntryList, getString(R.string.observe_peer_versions), totalPeers, PieUtils.getColours());
    }
}