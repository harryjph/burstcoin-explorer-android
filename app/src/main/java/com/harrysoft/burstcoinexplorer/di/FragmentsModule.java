package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.accounts.ui.AccountsFragment;
import com.harrysoft.burstcoinexplorer.events.ui.EventsFragment;
import com.harrysoft.burstcoinexplorer.explore.ui.ExploreFragment;
import com.harrysoft.burstcoinexplorer.main.ui.SettingsFragment;
import com.harrysoft.burstcoinexplorer.observe.ui.ObserveFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
interface FragmentsModule {
    @ContributesAndroidInjector
    ExploreFragment exploreFragment();

    @ContributesAndroidInjector
    AccountsFragment accountsFragment();

    @ContributesAndroidInjector
    EventsFragment eventsFragment();

    @ContributesAndroidInjector
    ObserveFragment observeFragment();

    @ContributesAndroidInjector
    SettingsFragment settingsFragment();
}
