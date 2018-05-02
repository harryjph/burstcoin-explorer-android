package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {SavedAccount.class}, version = 1, exportSchema = false)
@TypeConverters({com.harrysoft.burstcoinexplorer.accounts.db.TypeConverters.class})
public abstract class AccountsDatabase extends RoomDatabase {
    public abstract SavedAccountDao savedAccountDao();
}