package com.harry1453.burst.explorer.repository;

public interface ConfigRepository {
    void setSelectedCurrency(String currencyCode);
    String getSelectedCurrency();

    void setNodeAddress(String nodeAddress);
    String getNodeAddress();
}
