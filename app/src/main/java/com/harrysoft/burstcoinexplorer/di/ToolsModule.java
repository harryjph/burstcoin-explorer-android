package com.harrysoft.burstcoinexplorer.di;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.App;

import dagger.Module;
import dagger.Provides;

@Module
public class ToolsModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }
}
