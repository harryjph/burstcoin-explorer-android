package com.harrysoft.burstcoinexplorer.main.repository;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.harrysoft.burstcoinexplorer.R;

public class SharedPreferenceRepository implements PreferenceRepository {

    private final Context context;

    public SharedPreferenceRepository(Context context) {
        this.context = context;
    }

    @Override
    public String getSelectedCurrency() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.currency), context.getString(R.string.currency_default));
    }
}
