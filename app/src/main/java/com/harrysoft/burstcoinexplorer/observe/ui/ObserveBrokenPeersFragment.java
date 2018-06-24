package com.harrysoft.burstcoinexplorer.observe.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveBrokenPeersViewModel;

public class ObserveBrokenPeersFragment extends ObserveSubFragment {

    private ObserveBrokenPeersViewModel observeBrokenPeersViewModel;

    private RecyclerView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe_broken, container, false);

        observeBrokenPeersViewModel = ViewModelProviders.of(this).get(ObserveBrokenPeersViewModel.class);

        list = view.findViewById(R.id.observe_broken_peers_list);

        BrokenPeersRecyclerAdapter adapter = new BrokenPeersRecyclerAdapter(getContext());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        observeBrokenPeersViewModel.getBrokenPeers().observe(this, adapter::updateData);

        setupRefresh(view);

        return view;
    }

    @Override
    public void onNetworkStatus(NetworkStatus networkStatus) {
        if (observeBrokenPeersViewModel != null) {
            observeBrokenPeersViewModel.setBrokenPeers(networkStatus.getBrokenPeersFromMap());
        }
    }

    @Override
    protected void onError(Throwable error) {
        if (getContext() == null || list == null) {
            return;
        }
        // todo
    }
}