package com.harrysoft.burstcoinexplorer.accounts.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;

import javax.inject.Inject;

public class SavedAccountViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;
    private final AccountsDatabase accountsDatabase;

    @Inject
    SavedAccountViewModelFactory(BurstBlockchainService burstBlockchainService, AccountsDatabase accountsDatabase) {
        this.burstBlockchainService = burstBlockchainService;
        this.accountsDatabase = accountsDatabase;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SavedAccountViewModel(burstBlockchainService, accountsDatabase);
    }
}
