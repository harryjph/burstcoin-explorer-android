package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewAccountDetailsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstBlockchainService burstBlockchainService;
    private final Context context;

    @Nullable
    private BigInteger accountID = null;

    @Inject
    public ViewAccountDetailsViewModelFactory(Application application, BurstBlockchainService burstBlockchainService, Context context) {
        super(application);
        this.application = application;
        this.burstBlockchainService = burstBlockchainService;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (accountID != null) {
            return (T) new ViewAccountDetailsViewModel(application, burstBlockchainService, SavedAccountsUtils.openDatabaseConnection(context), accountID);
        } else {
            throw new IllegalArgumentException("Account ID not set.");
        }
    }

    public void setAccountID(@NonNull BigInteger accountID) {
        this.accountID = accountID;
    }
}
