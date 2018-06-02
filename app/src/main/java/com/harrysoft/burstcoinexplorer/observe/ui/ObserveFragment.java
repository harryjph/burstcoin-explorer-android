package com.harrysoft.burstcoinexplorer.observe.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveViewModel;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ObserveFragment extends Fragment {

    @Inject
    ObserveViewModelFactory observeViewModelFactory;

    private ObserveViewModel observeViewModel;

    private ObservePagerAdapter observePagerAdapter;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe, container, false);

        observeViewModel = ViewModelProviders.of(this, observeViewModelFactory).get(ObserveViewModel.class);

        ViewPager viewPager = view.findViewById(R.id.observe_viewpager);
        TabLayout tabLayout = view.findViewById(R.id.observe_tab_layout);

        observePagerAdapter = setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        observeViewModel.getNetworkStatus().observe(this, this::onNetworkStatus);
        observeViewModel.getRefreshing().observe(this, observePagerAdapter::setRefreshing);

        return view;
    }

    private void onNetworkStatus(@Nullable NetworkStatus networkStatus) {
        if (networkStatus != null) {
            observePagerAdapter.onNetworkStatus(networkStatus);
        } else {
            Toast.makeText(getContext(), R.string.loading_error, Toast.LENGTH_LONG).show();
        }
    }

    private ObservePagerAdapter setupViewPager(ViewPager viewPager) {
        ObservePagerAdapter observePagerAdapter = new ObservePagerAdapter(getChildFragmentManager());

        ObserveStatusFragment statusFragment = new ObserveStatusFragment();
        ObserveVersionsFragment versionsFragment = new ObserveVersionsFragment();
        ObservePeersMapFragment mapFragment = new ObservePeersMapFragment();
        ObserveBrokenPeersFragment brokenPeersFragment = new ObserveBrokenPeersFragment();

        statusFragment.setUp(observeViewModel::fetchNetworkStatus);
        versionsFragment.setUp(observeViewModel::fetchNetworkStatus);
        mapFragment.setUp(observeViewModel::fetchNetworkStatus);
        brokenPeersFragment.setUp(observeViewModel::fetchNetworkStatus);

        observePagerAdapter.addFragment(statusFragment, getString(R.string.observe_peer_status));
        observePagerAdapter.addFragment(versionsFragment, getString(R.string.observe_peer_versions));
        observePagerAdapter.addFragment(mapFragment, getString(R.string.observe_map));
        observePagerAdapter.addFragment(brokenPeersFragment, getString(R.string.observe_broken_peers));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(observePagerAdapter);
        return observePagerAdapter;
    }
}
