package com.harrysoft.burstcoinexplorer.di;

import android.app.Application;
import android.content.Context;

import com.harrysoft.burstcoinexplorer.App;

import dagger.Module;
import dagger.Provides;

@Module
class ToolsModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    Application provideApplication(App application) {
        return application;
    }
}
