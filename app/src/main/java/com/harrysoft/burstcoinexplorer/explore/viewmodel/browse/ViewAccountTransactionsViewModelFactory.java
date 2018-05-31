package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import javax.inject.Inject;

public class ViewAccountTransactionsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;

    @Nullable
    private BurstAddress account;

    @Inject
    public ViewAccountTransactionsViewModelFactory(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (account != null) {
            return (T) new ViewAccountTransactionsViewModel(burstBlockchainService, account);
        } else {
            throw new IllegalArgumentException("Account ID not set.");
        }
    }

    public void setAccount(@Nullable BurstAddress account) {
        this.account = account;
    }
}
