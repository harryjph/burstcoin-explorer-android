package com.harrysoft.burstcoinexplorer.observe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;


public class ObserveForksFragment extends ObserveSubFragment {

    private final String forksKey = "forks";
    private final String blockHeightKey = "forks_blockHeight";

    private RecyclerView list;

    private ForkListRecyclerAdapter forkListRecyclerAdapter;

    private BigInteger blockHeight;

    private ArrayList<ForkInfo> forkInfos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(forksKey)) {
                forkInfos = savedInstanceState.getParcelableArrayList(forksKey);
            }
            if (savedInstanceState.containsKey(blockHeightKey)) {
                blockHeight = new BigInteger(savedInstanceState.getString(blockHeightKey));
            }
        }

        View view = inflater.inflate(R.layout.fragment_observe_forks, container, false);

        list = view.findViewById(R.id.observe_forks_list);

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(listManager);

        if (forkListRecyclerAdapter != null) {
            list.setAdapter(forkListRecyclerAdapter);
        }

        updateList();

        setupRefresh(view);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (forkInfos != null) {
            outState.putParcelableArrayList(forksKey, forkInfos);
        }
        if (blockHeight != null) {
            outState.putString(blockHeightKey, blockHeight.toString());
        }
    }

    private void updateList() {
        if (getContext() == null || list == null) {
            return;
        }

        forkListRecyclerAdapter = new ForkListRecyclerAdapter(getContext());
        updateListData();

        list.setAdapter(forkListRecyclerAdapter);
    }

    @Override
    protected void onRefreshError(Throwable error, boolean refreshing) {
        if (getContext() == null || list == null) {
            return;
        }
        // todo
    }

    @Override
    public void onNetworkStatus(NetworkStatus networkStatus) {
        this.blockHeight = new BigInteger(String.valueOf(networkStatus.blockHeight));
        updateListData();
    }

    @Override
    public void onForkInfos(ForkInfo[] newForkInfos) {
        forkInfos = new ArrayList<>(Arrays.asList(newForkInfos));
        updateListData();
    }

    private void updateListData() {
        if (forkListRecyclerAdapter != null) {
            if (forkInfos != null) {
                forkListRecyclerAdapter.updateData(forkInfos);
            }
            if (blockHeight != null) {
                forkListRecyclerAdapter.setCurrentBlockHeight(blockHeight);
            }
        }
    }
}
