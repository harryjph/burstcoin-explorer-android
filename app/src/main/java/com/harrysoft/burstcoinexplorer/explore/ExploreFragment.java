package com.harrysoft.burstcoinexplorer.explore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.api.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;
import com.harrysoft.burstcoinexplorer.util.CurrencyUtils;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExploreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recentBlocksList;
    private TextView priceFiat, priceBtc, marketCapital, blockHeight, recentBlocksLabel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    BurstBlockchainService burstBlockchainService;
    @Inject
    BurstPriceService burstPriceService;

    private BurstPrice burstPrice;
    private Block[] recentBlocks;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recentBlocksList = view.findViewById(R.id.explore_blocks);
        recentBlocksList.setLayoutManager(new LinearLayoutManager(getActivity()));

        priceFiat = view.findViewById(R.id.explore_price_fiat);
        priceBtc = view.findViewById(R.id.explore_price_btc_value);
        marketCapital = view.findViewById(R.id.explore_market_cap_value);
        blockHeight = view.findViewById(R.id.explore_block_height_value);
        recentBlocksLabel = view.findViewById(R.id.explore_blocks_label);
        swipeRefreshLayout = view.findViewById(R.id.explore_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        priceFiat.setText(getString(R.string.price_fiat, getString(R.string.loading)));

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.extra_recent_blocks)) && savedInstanceState.containsKey(getString(R.string.extra_burst_price))) {
            try {
                onBlocks((Block[]) savedInstanceState.getParcelableArray(getString(R.string.extra_recent_blocks)));
                onPrice(savedInstanceState.getParcelable(getString(R.string.extra_burst_price)));
            } catch (ClassCastException e) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        } else {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recentBlocks != null) {
            outState.putParcelableArray(getString(R.string.extra_recent_blocks), recentBlocks);
        }

        if (burstPrice != null) {
            outState.putParcelable(getString(R.string.extra_burst_price), burstPrice);
        }
    }

    private void getData() {
        burstBlockchainService.fetchRecentBlocks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlocks, this::onError);
    }

    private void getPrice() {
        burstPriceService.fetchPrice(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.currency), getString(R.string.currency_default)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrice, this::onPriceError);

        burstPriceService.fetchPrice("BTC")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrice, this::onPriceError);
    }

    private void onError(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        blockHeight.setText(R.string.loading_error);
        recentBlocksLabel.setText(R.string.recent_blocks_error);
        recentBlocksList.setAdapter(null);
    }

    private void onPriceError(Throwable throwable) {
        priceFiat.setText(getString(R.string.loading_error));
        priceBtc.setText(getString(R.string.loading_error));
        marketCapital.setText(getString(R.string.loading_error));
    }

    private void onPrice(BurstPrice burstPrice) {
        this.burstPrice = burstPrice;
        if (burstPrice.currencyCode.equals("BTC")) {
            priceBtc.setText(getString(R.string.basic_data, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.price)));
        } else {
            priceFiat.setText(getString(R.string.price_fiat, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.price)));
            marketCapital.setText(getString(R.string.basic_data, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.marketCapital)));
        }
    }

    private void onBlocks(Block[] blocks) {
        swipeRefreshLayout.setRefreshing(false);
        this.recentBlocks = blocks;
        recentBlocksLabel.setText(R.string.recent_blocks);
        blockHeight.setText(String.format(Locale.getDefault(), "%d", blocks[0].blockNumber));
        RecyclerView.Adapter transactionsAdapter = new RecentBlocksRecyclerAdapter(getContext(), blocks);
        recentBlocksList.setAdapter(transactionsAdapter);
    }

    @Override
    public void onRefresh() {
        getData();
        getPrice();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPrice();
    }
}
