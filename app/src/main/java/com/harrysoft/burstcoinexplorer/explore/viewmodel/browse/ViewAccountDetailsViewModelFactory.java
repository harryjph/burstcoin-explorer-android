package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewAccountDetailsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstBlockchainService burstBlockchainService;
    private final AccountsDatabase accountsDatabase;

    @Nullable
    private BigInteger accountID = null;

    @Inject
    ViewAccountDetailsViewModelFactory(Application application, BurstBlockchainService burstBlockchainService, AccountsDatabase accountsDatabase) {
        super(application);
        this.application = application;
        this.burstBlockchainService = burstBlockchainService;
        this.accountsDatabase = accountsDatabase;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (accountID != null) {
            return (T) new ViewAccountDetailsViewModel(application, burstBlockchainService, accountsDatabase, accountID);
        } else {
            throw new IllegalArgumentException("Account ID not set.");
        }
    }

    public void setAccountID(@NonNull BigInteger accountID) {
        this.accountID = accountID;
    }
}
