package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import burst.kit.entity.BurstAddress;
import burst.kit.service.BurstNodeService;

public class ViewAccountTransactionsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNodeService burstNodeService;

    @Nullable
    private BurstAddress account;

    @Inject
    public ViewAccountTransactionsViewModelFactory(BurstNodeService burstNodeService) {
        this.burstNodeService = burstNodeService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (account != null) {
            return (T) new ViewAccountTransactionsViewModel(burstNodeService, account);
        } else {
            throw new IllegalArgumentException("Account ID not set.");
        }
    }

    public void setAccount(@Nullable BurstAddress account) {
        this.account = account;
    }
}
