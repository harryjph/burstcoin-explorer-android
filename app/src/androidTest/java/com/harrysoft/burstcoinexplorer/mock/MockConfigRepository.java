package com.harrysoft.burstcoinexplorer.mock;

import android.content.Context;
import android.support.annotation.NonNull;

import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harrysoft.burstcoinexplorer.R;

public class MockConfigRepository implements ConfigRepository {

    private String currencyCode;
    private String nodeAddress;

    public MockConfigRepository(Context context) {
        currencyCode = context.getString(R.string.currency_default);
        nodeAddress = context.getString(R.string.node_address_default);
    }

    @Override
    public void setSelectedCurrency(@NonNull String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @NonNull
    @Override
    public String getSelectedCurrency() {
        return currencyCode;
    }

    @Override
    public void setNodeAddress(@NonNull String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    @NonNull
    @Override
    public String getNodeAddress() {
        return nodeAddress;
    }
}
