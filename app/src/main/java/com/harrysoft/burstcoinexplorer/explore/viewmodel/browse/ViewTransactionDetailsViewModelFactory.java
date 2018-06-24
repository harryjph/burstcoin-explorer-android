package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewTransactionDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;

    @Nullable
    private BigInteger transactionID;

    @Inject
    public ViewTransactionDetailsViewModelFactory(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (transactionID != null) {
            return (T) new ViewTransactionDetailsViewModel(burstBlockchainService, transactionID);
        } else {
            throw new IllegalArgumentException("No parameters set.");
        }
    }

    public boolean canCreate() {
        return transactionID != null;
    }

    public void setTransactionID(@Nullable BigInteger transactionID) {
        this.transactionID = transactionID;
    }
}
