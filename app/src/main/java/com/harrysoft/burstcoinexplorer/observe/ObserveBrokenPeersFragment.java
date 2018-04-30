package com.harrysoft.burstcoinexplorer.observe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.pieutils.BrokenPeersRecyclerAdapter;

import java.util.ArrayList;

public class ObserveBrokenPeersFragment extends ObserveSubFragment {

    private final String brokenPeersKey = "brokenPeers";

    private RecyclerView list;

    private BrokenPeersRecyclerAdapter adapter;

    private ArrayList<NetworkStatus.BrokenPeer> brokenPeers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            brokenPeers = savedInstanceState.getParcelableArrayList(brokenPeersKey);
        }

        View view = inflater.inflate(R.layout.fragment_observe_broken, container, false);

        list = view.findViewById(R.id.observe_broken_peers_list);

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(listManager);

        if (adapter != null) {
            list.setAdapter(adapter);
        }

        updateList();

        setupRefresh(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(brokenPeersKey, brokenPeers);
    }

    @Override
    public void onNetworkStatus(NetworkStatus networkStatus) {
        brokenPeers = networkStatus.getBrokenPeersFromMap();

        updateList();
    }

    @Override
    protected void onRefreshError(Throwable error, boolean refreshing) {
        if (getContext() == null || list == null) {
            return;
        }
        // todo
    }

    private void updateList() {
        if (getContext() == null || list == null || brokenPeers == null) {
            return;
        }

        adapter = new BrokenPeersRecyclerAdapter(getContext(), brokenPeers);

        list.setAdapter(adapter);
    }
}