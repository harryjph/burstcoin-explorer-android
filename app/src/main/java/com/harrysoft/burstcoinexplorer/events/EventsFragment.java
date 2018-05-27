package com.harrysoft.burstcoinexplorer.events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstInfoService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstNetworkService;
import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    BurstNetworkService burstNetworkService;
    @Inject
    BurstInfoService burstInfoService;

    private final String forksKey = "forks";
    private final String blockHeightKey = "forks_blockHeight";

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView list;

    private EventListRecyclerAdapter eventListRecyclerAdapter;

    private BigInteger blockHeight;

    private ArrayList<EventInfo> eventInfos;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(forksKey)) {
                eventInfos = savedInstanceState.getParcelableArrayList(forksKey);
            }
            if (savedInstanceState.containsKey(blockHeightKey)) {
                blockHeight = new BigInteger(savedInstanceState.getString(blockHeightKey));
            }
        }

        View view = inflater.inflate(R.layout.fragment_events, container, false);

        list = view.findViewById(R.id.events_list);

        RecyclerView.LayoutManager listManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(listManager);

        if (eventListRecyclerAdapter != null) {
            list.setAdapter(eventListRecyclerAdapter);
        }

        swipeRefreshLayout = view.findViewById(R.id.observe_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        setupList();

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (eventInfos != null) {
            outState.putParcelableArrayList(forksKey, eventInfos);
        }
        if (blockHeight != null) {
            outState.putString(blockHeightKey, blockHeight.toString());
        }
    }

    private void setupList() {
        if (getContext() == null || list == null) {
            return;
        }

        eventListRecyclerAdapter = new EventListRecyclerAdapter(getContext());
        updateListData();

        list.setAdapter(eventListRecyclerAdapter);
    }

    protected void onError(Throwable error) {
        Toast.makeText(getContext(), R.string.loading_error, Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
        error.printStackTrace();
    }

    public void onNetworkStatus(NetworkStatus networkStatus) {
        this.blockHeight = new BigInteger(String.valueOf(networkStatus.blockHeight));
        updateListData();
    }

    public void onForkInfos(EventInfo[] newEventInfos) {
        swipeRefreshLayout.setRefreshing(false);
        eventInfos = new ArrayList<>(Arrays.asList(newEventInfos));
        updateListData();
    }

    private void updateListData() {
        if (eventListRecyclerAdapter != null) {
            if (eventInfos != null) {
                eventListRecyclerAdapter.updateData(eventInfos);
            }
            if (blockHeight != null) {
                eventListRecyclerAdapter.setCurrentBlockHeight(blockHeight);
            }
        }
    }

    @Override
    public void onRefresh() {
        burstNetworkService.fetchNetworkStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNetworkStatus, this::onError);
        burstInfoService.getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onForkInfos, this::onError);
    }
}
