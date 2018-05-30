package com.harrysoft.burstcoinexplorer.explore.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.ExploreViewModel;
import com.harrysoft.burstcoinexplorer.explore.viewmodel.ExploreViewModelFactory;
import com.harrysoft.burstcoinexplorer.util.CurrencyUtils;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExploreFragment extends Fragment {

    @Inject
    ExploreViewModelFactory exploreViewModelFactory;

    private ExploreViewModel exploreViewModel;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreViewModel = ViewModelProviders.of(this, exploreViewModelFactory).get(ExploreViewModel.class);

        RecyclerView recentBlocksList = view.findViewById(R.id.explore_blocks);
        TextView priceFiat = view.findViewById(R.id.explore_price_fiat);
        TextView priceBtc = view.findViewById(R.id.explore_price_btc_value);
        TextView marketCapital = view.findViewById(R.id.explore_market_cap_value);
        TextView blockHeight = view.findViewById(R.id.explore_block_height_value);
        TextView recentBlocksLabel = view.findViewById(R.id.explore_blocks_label);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.explore_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(exploreViewModel);

        recentBlocksList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecentBlocksRecyclerAdapter recentBlocksAdapter = new RecentBlocksRecyclerAdapter(getContext());
        recentBlocksList.setAdapter(recentBlocksAdapter);

        exploreViewModel.getRefreshing().observe(this, swipeRefreshLayout::setRefreshing);
        exploreViewModel.getRecentBlocks().observe(this, recentBlocksAdapter::updateData);
        exploreViewModel.getPriceFiat().observe(this, priceFiat::setText);
        exploreViewModel.getPriceBtc().observe(this, priceBtc::setText);
        exploreViewModel.getMarketCapital().observe(this, marketCapital::setText);
        exploreViewModel.getBlockHeight().observe(this, blockHeight::setText);
        exploreViewModel.getRecentBlocksLabel().observe(this, recentBlocksLabel::setText);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getPrice(); todo check if the currency code changed
    }
}
