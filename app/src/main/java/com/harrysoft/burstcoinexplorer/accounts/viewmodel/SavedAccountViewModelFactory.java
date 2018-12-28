package com.harrysoft.burstcoinexplorer.accounts.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;

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
