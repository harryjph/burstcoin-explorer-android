package com.harrysoft.burstcoinexplorer.observe.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.util.PieUtils;
import com.harrysoft.burstcoinexplorer.observe.util.RemovableLabelPieEntry;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveVersionsViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ObserveVersionsFragment extends ObserveSubFragment {

    private ObserveVersionsViewModel observeVersionsViewModel;

    private PieChart peerVersionPieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe_version, container, false);

        observeVersionsViewModel = ViewModelProviders.of(this).get(ObserveVersionsViewModel.class);

        peerVersionPieChart = view.findViewById(R.id.observe_peer_version_pie);

        observeVersionsViewModel.getPeerVersions().observe(this, this::updatePie);

        setupRefresh(view);

        return view;
    }

    public void onNetworkStatus(NetworkStatus networkStatus) {
        if (observeVersionsViewModel != null) {
            observeVersionsViewModel.setPeerVersions(networkStatus.peersData.getPeerVersionsFromMap());
        }
    }

    @Override
    protected void onError(Throwable error) {
        if (getContext() != null) {
            PieUtils.setupPieError(getContext(), peerVersionPieChart, error);
        }
    }

    private void updatePie(List<NetworkStatus.PeersData.PeerVersion> peerVersions) {
        if (peerVersionPieChart == null) {
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