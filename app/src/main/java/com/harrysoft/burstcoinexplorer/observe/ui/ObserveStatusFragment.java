package com.harrysoft.burstcoinexplorer.observe.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.util.PieUtils;
import com.harrysoft.burstcoinexplorer.observe.util.RemovableLabelPieEntry;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveStatusViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ObserveStatusFragment extends ObserveSubFragment {

    private ObserveStatusViewModel observeStatusViewModel;

    private PieChart peerStatusPieChart;

    private TextView peerCountLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe_pie, container, false);

        observeStatusViewModel = ViewModelProviders.of(this).get(ObserveStatusViewModel.class);

        peerStatusPieChart = view.findViewById(R.id.observe_peers_pie);

        peerCountLabel = view.findViewById(R.id.observe_peers_count);

        observeStatusViewModel.getPeersStatus().observe(this, this::updatePie);
        observeStatusViewModel.getPeerCount().observe(this, this::setPeerCount);

        setupRefresh(view);

        return view;
    }

    public void onNetworkStatus(NetworkStatus networkStatus) {
        if (observeStatusViewModel != null) {
            observeStatusViewModel.setPeersStatus(networkStatus.peersData.peersStatus);
        }
    }

    @Override
    protected void onError(Throwable error) {
        if (getContext() != null) {
            PieUtils.setupPieError(getContext(), peerStatusPieChart, error);
        }
    }

    private void updatePie(NetworkStatus.PeersData.PeersStatus peersStatus) {
        if (peerStatusPieChart == null) {
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
        colors.add(0xffaa66cc); // purple for fork
        colors.add(0xffff8800); // orange for resync
        colors.add(0xff0099cc); // blue for unreachable
        colors.add(0xffcc0000); // red for stuck
        colors.add(0xff669900); // green for valid

        PieUtils.setupPieChart(getContext(), peerStatusPieChart, pieEntryList, getString(R.string.observe_peer_status), totalPeers, colors);
    }

    private void setPeerCount(Long peerCount) {
        peerCountLabel.setText(getString(R.string.total_peers_count, String.valueOf(peerCount)));
    }
}