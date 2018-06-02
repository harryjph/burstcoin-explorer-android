package com.harrysoft.burstcoinexplorer.observe.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.util.ArrayList;
import java.util.List;

class ObservePagerAdapter extends FragmentPagerAdapter {

    private final List<ObserveSubFragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    ObservePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addFragment(ObserveSubFragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    void onNetworkStatus(NetworkStatus networkStatus) {
        for(ObserveSubFragment fragment : fragments) {
            fragment.onNetworkStatus(networkStatus);
        }
    }

    public void setRefreshing(boolean refreshing) {
        for(ObserveSubFragment fragment : fragments) {
            fragment.setRefreshing(refreshing);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}