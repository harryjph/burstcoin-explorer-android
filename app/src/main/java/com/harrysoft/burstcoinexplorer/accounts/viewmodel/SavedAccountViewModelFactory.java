package com.harrysoft.burstcoinexplorer.accounts.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;

import javax.inject.Inject;

import burst.kit.service.BurstNodeService;

public class SavedAccountViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNodeService burstNodeService;
    private final AccountsDatabase accountsDatabase;

    @Inject
    SavedAccountViewModelFactory(BurstNodeService burstNodeService, AccountsDatabase accountsDatabase) {
        this.burstNodeService = burstNodeService;
        this.accountsDatabase = accountsDatabase;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SavedAccountViewModel(burstNodeService, accountsDatabase);
    }
}
