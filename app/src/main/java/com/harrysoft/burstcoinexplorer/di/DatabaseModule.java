package com.harrysoft.burstcoinexplorer.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Provides
    public AccountsDatabase provideAccountsDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AccountsDatabase.class, context.getString(R.string.accounts_db_name)).build();
    }
}
