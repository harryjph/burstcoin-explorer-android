package com.harrysoft.burstcoinexplorer.mock;

import android.content.Context;

import com.harrysoft.burstcoinexplorer.R;
import com.harry1453.burst.explorer.repository.PreferenceRepository;

public class MockPreferenceRepository implements PreferenceRepository {

    private String currencyCode;
    private String nodeAddress;

    public MockPreferenceRepository(Context context) {
        currencyCode = context.getString(R.string.currency_default);
        nodeAddress = context.getString(R.string.node_address_default);
    }

    @Override
    public void setSelectedCurrency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String getSelectedCurrency() {
        return currencyCode;
    }

    @Override
    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    @Override
    public String getNodeAddress() {
        return nodeAddress;
    }
}
