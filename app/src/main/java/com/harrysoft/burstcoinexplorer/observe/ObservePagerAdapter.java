package com.harrysoft.burstcoinexplorer.observe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;
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

    void onNetworkStatus(NetworkStatus networkStatus, ObserveSubFragment sender) {
        for(ObserveSubFragment fragment : fragments) {
            fragment.onNetworkStatus(networkStatus);
        }

        if (sender != null) {
            sender.onRefreshed();
        }
    }

    void onForkInfos(ForkInfo[] forkInfos) {
        for(ObserveSubFragment fragment : fragments) {
            fragment.onForkInfos(forkInfos);
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