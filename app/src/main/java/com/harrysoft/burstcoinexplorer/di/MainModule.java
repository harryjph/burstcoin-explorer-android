package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.accounts.AccountsFragment;
import com.harrysoft.burstcoinexplorer.explore.ExploreFragment;
import com.harrysoft.burstcoinexplorer.observe.ObserveForksFragment;
import com.harrysoft.burstcoinexplorer.observe.ObserveFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
interface MainModule {
    @FragmentScope
    @ContributesAndroidInjector
    ExploreFragment exploreFragment();

    @FragmentScope
    @ContributesAndroidInjector
    AccountsFragment accountsFragment();

    @FragmentScope
    @ContributesAndroidInjector
    ObserveFragment observeFragment();
}
