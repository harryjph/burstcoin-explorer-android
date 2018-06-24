package com.harrysoft.burstcoinexplorer.main.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harrysoft.burstcoinexplorer.R;

public class AndroidConfigRepository implements ConfigRepository {

    private final Context context;

    public AndroidConfigRepository(Context context) {
        this.context = context;
    }

    @Override
    public void setSelectedCurrency(@NonNull String currencyCode) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(R.string.currency_key), currencyCode).apply();
    }

    @NonNull
    @Override
    public String getSelectedCurrency() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.currency_key), context.getString(R.string.currency_default));
    }

    @Override
    public void setNodeAddress(@NonNull String nodeAddress) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(R.string.node_address_key), nodeAddress).apply();

    }

    @NonNull
    @Override
    public String getNodeAddress() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.node_address_key), context.getString(R.string.node_address_default));
    }
}
