package com.harrysoft.burstcoinexplorer.main.repository;

public interface PreferenceRepository {
    void setSelectedCurrency(String currencyCode);
    String getSelectedCurrency();

    void setNodeAddress(String nodeAddress);
    String getNodeAddress();
}
