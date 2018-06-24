package com.harry1453.burst.explorer.repository;

public interface PreferenceRepository {
    void setSelectedCurrency(String currencyCode);
    String getSelectedCurrency();

    void setNodeAddress(String nodeAddress);
    String getNodeAddress();
}
