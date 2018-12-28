package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;

import java.util.List;

import javax.inject.Inject;

import burst.kit.entity.BurstID;
import burst.kit.service.BurstNodeService;

public class ViewTransactionsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application application;
    private final BurstNodeService burstNodeService;

    @Nullable
    private TransactionDisplayType transactionDisplayType = null;
    @Nullable
    private List<BurstID> transactionIDs = null;

    @Inject
    public ViewTransactionsViewModelFactory(Application application, BurstNodeService burstNodeService) {
        super(application);
        this.application = application;
        this.burstNodeService = burstNodeService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (transactionDisplayType != null && transactionIDs != null) {
            return (T) new ViewTransactionsViewModel(application, transactionDisplayType, burstNodeService, transactionIDs);
        } else {
            throw new IllegalArgumentException("Factory not setup.");
        }
    }

    public void setup(TransactionDisplayType transactionDisplayType, List<BurstID> transactionIDs) {
        this.transactionDisplayType = transactionDisplayType;
        this.transactionIDs = transactionIDs;
    }
}
