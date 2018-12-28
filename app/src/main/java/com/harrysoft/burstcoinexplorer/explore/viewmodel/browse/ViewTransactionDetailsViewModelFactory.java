package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import burst.kit.entity.BurstID;
import burst.kit.service.BurstNodeService;

public class ViewTransactionDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNodeService burstNodeService;

    @Nullable
    private BurstID transactionID;

    @Inject
    public ViewTransactionDetailsViewModelFactory(BurstNodeService burstNodeService) {
        this.burstNodeService = burstNodeService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (transactionID != null) {
            return (T) new ViewTransactionDetailsViewModel(burstNodeService, transactionID);
        } else {
            throw new IllegalArgumentException("No parameters set.");
        }
    }

    public boolean canCreate() {
        return transactionID != null;
    }

    public void setTransactionID(@Nullable BurstID transactionID) {
        this.transactionID = transactionID;
    }
}
