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

import burst.kit.entity.BurstAddress;
import burst.kit.service.BurstNodeService;

public class ViewAccountDetailsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstNodeService burstNodeService;
    private final AccountsDatabase accountsDatabase;

    @Nullable
    private BurstAddress address = null;

    @Inject
    ViewAccountDetailsViewModelFactory(Application application, BurstNodeService burstNodeService, AccountsDatabase accountsDatabase) {
        super(application);
        this.application = application;
        this.burstNodeService = burstNodeService;
        this.accountsDatabase = accountsDatabase;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (address != null) {
            return (T) new ViewAccountDetailsViewModel(application, burstNodeService, accountsDatabase, address);
        } else {
            throw new IllegalArgumentException("Account ID not set.");
        }
    }

    public void setAddress(@NonNull BurstAddress address) {
        this.address = address;
    }
}
