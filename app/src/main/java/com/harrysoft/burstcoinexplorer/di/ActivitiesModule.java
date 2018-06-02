package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewAccountTransactionsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewBlockExtraDetailsActivity;
import com.harrysoft.burstcoinexplorer.explore.ui.browse.ViewTransactionDetailsActivity;
import com.harrysoft.burstcoinexplorer.main.ui.MainActivity;
import com.harrysoft.burstcoinexplorer.main.ui.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesModule {
    @ActivityScope
    @ContributesAndroidInjector (modules = MainModule.class)
    abstract MainActivity bindMainActivity();

    @ActivityScope
    @ContributesAndroidInjector (modules = SettingsModule.class)
    abstract SettingsActivity bindSettingsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ViewAccountDetailsActivity bindViewAccountDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ViewAccountTransactionsActivity bindViewAccountTransactionsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ViewBlockDetailsActivity bindViewBlockDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ViewBlockExtraDetailsActivity bindViewBlockExtraDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract ViewTransactionDetailsActivity bindViewTransactionDetailsActivity();
}
