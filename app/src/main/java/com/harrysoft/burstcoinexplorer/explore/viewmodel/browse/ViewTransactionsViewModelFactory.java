package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

public class ViewTransactionsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstBlockchainService burstBlockchainService;

    @Nullable
    private TransactionDisplayType transactionDisplayType = null;
    @Nullable
    private List<BigInteger> transactionIDs = null;

    @Inject
    public ViewTransactionsViewModelFactory(Application application, BurstBlockchainService burstBlockchainService) {
        super(application);
        this.application = application;
        this.burstBlockchainService = burstBlockchainService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (transactionDisplayType != null && transactionIDs != null) {
            return (T) new ViewTransactionsViewModel(application, transactionDisplayType, burstBlockchainService, transactionIDs);
        } else {
            throw new IllegalArgumentException("Factory not setup.");
        }
    }

    public void setup(TransactionDisplayType transactionDisplayType, List<BigInteger> transactionIDs) {
        this.transactionDisplayType = transactionDisplayType;
        this.transactionIDs = transactionIDs;
    }
}
