package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.main.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
interface SettingsModule {
    @FragmentScope
    @ContributesAndroidInjector
    SettingsFragment settingsFragment();
}
