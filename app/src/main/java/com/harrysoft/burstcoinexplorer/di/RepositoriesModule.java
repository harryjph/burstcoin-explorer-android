package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;
import com.harrysoft.burstcoinexplorer.main.repository.SharedPreferenceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesModule {
    @Singleton
    @Provides
    public PreferenceRepository providePreferenceRepositor(Context context) {
        return new SharedPreferenceRepository(context);
    }
}
