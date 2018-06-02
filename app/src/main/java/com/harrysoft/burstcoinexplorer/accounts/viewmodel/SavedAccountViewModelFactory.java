package com.harrysoft.burstcoinexplorer.accounts.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import javax.inject.Inject;

public class SavedAccountViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;
    private final Context context;

    @Inject
    public SavedAccountViewModelFactory(BurstBlockchainService burstBlockchainService, Context context) {
        this.burstBlockchainService = burstBlockchainService;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SavedAccountViewModel(burstBlockchainService, SavedAccountsUtils.openDatabaseConnection(context));
    }
}
