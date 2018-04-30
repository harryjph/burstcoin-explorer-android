package com.harrysoft.burstcoinexplorer.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.HSBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.CMCPriceService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExploreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recentBlocksList;
    private TextView priceFiat, priceBtc, marketCapital, blockHeight, recentBlocksLabel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BurstExplorer burstExplorer;

    private BurstAPIService burstAPIService;
    private BurstPriceService burstPriceService;

    private BurstPrice burstPrice;
    private Block[] recentBlocks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recentBlocksList = view.findViewById(R.id.explore_blocks);

        RecyclerView.LayoutManager transactionsManager = new LinearLayoutManager(getActivity());
        recentBlocksList.setLayoutManager(transactionsManager);

        priceFiat = view.findViewById(R.id.explore_price_fiat);
        priceBtc = view.findViewById(R.id.explore_price_btc_value);
        marketCapital = view.findViewById(R.id.explore_market_cap_value);
        blockHeight = view.findViewById(R.id.explore_block_height_value);
        recentBlocksLabel = view.findViewById(R.id.explore_blocks_label);
        swipeRefreshLayout = view.findViewById(R.id.explore_swiperefresh);

        priceFiat.setText(getString(R.string.price_fiat, getString(R.string.loading)));

        burstAPIService = new PoccAPIService(getActivity());
        burstPriceService = new CMCPriceService(getActivity());

        burstExplorer = new HSBurstExplorer(getActivity());

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_recent_blocks)) && savedInstanceState.containsKey(getString(R.string.extra_burst_price))) {
            onBlocks((Block[]) savedInstanceState.getParcelableArray(getString(R.string.extra_recent_blocks)));
            onPrice(savedInstanceState.getParcelable(getString(R.string.extra_burst_price)));
        } else {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recentBlocks != null) {
            outState.putParcelableArray(getString(R.string.extra_recent_blocks), recentBlocks);
        }

        if (burstPrice != null) {
            outState.putParcelable(getString(R.string.extra_burst_price), burstPrice);
        }
    }

    private void getData() {
        burstAPIService.fetchRecentBlocks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlocks, this::onError);
    }

    private void getPrice() {
        burstPriceService.fetchPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrice, this::onPriceError);
    }

    public void onError(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        blockHeight.setText(R.string.loading_error);
        recentBlocksLabel.setText(R.string.recent_blocks_error);
        recentBlocksList.setAdapter(null);
        throwable.printStackTrace();
    }

    public void onPriceError(Throwable throwable) {
        priceFiat.setText(getString(R.string.loading_error));
        priceBtc.setText(getString(R.string.loading_error));
        marketCapital.setText(getString(R.string.loading_error));
        throwable.printStackTrace();
    }

    public void onPrice(BurstPrice burstPrice) {
        this.burstPrice = burstPrice;
        priceFiat.setText(getString(R.string.price_fiat, "$" + burstPrice.priceUsd));
        priceBtc.setText(getString(R.string.basic_data, burstPrice.priceBtc.toString()));
        marketCapital.setText(getString(R.string.basic_data, burstPrice.marketCapital.toString()));
    }

    public void onBlocks(Block[] blocks) {
        this.recentBlocks = blocks;
        recentBlocksLabel.setText(R.string.recent_blocks);
        blockHeight.setText(String.format(Locale.getDefault(), "%d", blocks[0].blockNumber));
        swipeRefreshLayout.setRefreshing(false);
        RecyclerView.Adapter transactionsAdapter = new RecentBlocksRecyclerAdapter(getActivity(), burstExplorer, blocks);
        recentBlocksList.setAdapter(transactionsAdapter);
    }

    @Override
    public void onRefresh() {
        getData();
        getPrice();
    }
}