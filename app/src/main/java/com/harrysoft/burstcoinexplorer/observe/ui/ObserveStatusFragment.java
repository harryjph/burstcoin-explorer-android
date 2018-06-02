package com.harrysoft.burstcoinexplorer.observe.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.util.PieUtils;
import com.harrysoft.burstcoinexplorer.observe.util.RemovableLabelPieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ObserveStatusFragment extends ObserveSubFragment {

    private final String peerStatusKey = "peersStatus";

    private PieChart peerStatusPieChart;

    private NetworkStatus.PeersData.PeersStatus peersStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            peersStatus = savedInstanceState.getParcelable(peerStatusKey);
        }

        View view = inflater.inflate(R.layout.fragment_observe_status, container, false);

        peerStatusPieChart = view.findViewById(R.id.observe_peer_status_pie);

        updatePie();

        setupRefresh(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(peerStatusKey, peersStatus);
    }

    public void onNetworkStatus(NetworkStatus networkStatus) {
        this.peersStatus = networkStatus.peersData.peersStatus;
        updatePie();
    }

    @Override
    protected void onError(Throwable error) {
        if (getContext() != null) {
            PieUtils.setupPieError(getContext(), peerStatusPieChart, error);
        }
    }

    private void updatePie() {
        if (peersStatus == null || peerStatusPieChart == null) {
            return;
        }

        ArrayList<PieEntry> pieEntryList = new ArrayList<>();

        float totalPeers = (float) peersStatus.total();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String forkPercent = decimalFormat.format(((float) peersStatus.fork) / totalPeers * 100f);
        String resyncPercent = decimalFormat.format(((float) peersStatus.resync) / totalPeers * 100f);
        String stuckPercent = decimalFormat.format(((float) peersStatus.stuck) / totalPeers * 100f);
        String unreachablePercent = decimalFormat.format(((float) peersStatus.unreachable) / totalPeers * 100f);
        String validPercent = decimalFormat.format(((float) peersStatus.valid) / totalPeers * 100f);

        pieEntryList.add(new RemovableLabelPieEntry((float) peersStatus.fork, getString(R.string.observe_status_fork, forkPercent)));
        pieEntryList.add(new RemovableLabelPieEntry((float) peersStatus.resync, getString(R.string.observe_status_resync, resyncPercent)));
        pieEntryList.add(new RemovableLabelPieEntry((float) peersStatus.unreachable, getString(R.string.observe_status_unreachable, unreachablePercent)));
        pieEntryList.add(new RemovableLabelPieEntry((float) peersStatus.stuck, getString(R.string.observe_status_stuck, stuckPercent)));
        pieEntryList.add(new RemovableLabelPieEntry((float) peersStatus.valid, getString(R.string.observe_status_valid, validPercent)));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(0xffaa66cc); // purple
        colors.add(0xffff8800); // orange
        colors.add(0xff0099cc); // dark blue
        colors.add(0xffcc0000); // red
        colors.add(0xff669900); // dark green

        PieUtils.setupPieChart(getContext(), peerStatusPieChart, pieEntryList, getString(R.string.observe_peer_status), totalPeers, colors);
    }
}