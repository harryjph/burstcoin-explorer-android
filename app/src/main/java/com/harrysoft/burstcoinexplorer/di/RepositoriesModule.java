package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harrysoft.burstcoinexplorer.main.repository.AndroidClipboardRepository;
import com.harrysoft.burstcoinexplorer.main.repository.AndroidConfigRepository;
import com.harrysoft.burstcoinexplorer.main.repository.ClipboardRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class RepositoriesModule {
    @Singleton
    @Provides
    public ConfigRepository providePreferenceRepository(Context context) {
        return new AndroidConfigRepository(context);
    }

    @Singleton
    @Provides
    public ClipboardRepository provideClipboardRepository(Context context) {
        return new AndroidClipboardRepository(context);
    }
}
