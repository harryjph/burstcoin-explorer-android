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
interface ActivitiesModule {
    @ContributesAndroidInjector
    MainActivity bindMainActivity();

    @ContributesAndroidInjector
    SettingsActivity bindSettingsActivity();

    @ContributesAndroidInjector
    ViewAccountDetailsActivity bindViewAccountDetailsActivity();

    @ContributesAndroidInjector
    ViewAccountTransactionsActivity bindViewAccountTransactionsActivity();

    @ContributesAndroidInjector
    ViewBlockDetailsActivity bindViewBlockDetailsActivity();

    @ContributesAndroidInjector
    ViewBlockExtraDetailsActivity bindViewBlockExtraDetailsActivity();

    @ContributesAndroidInjector
    ViewTransactionDetailsActivity bindViewTransactionDetailsActivity();
}
