package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harry1453.burst.explorer.service.BurstBlockchainService;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewBlockExtraDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;

    @Nullable
    private BigInteger blockID;

    @Inject
    ViewBlockExtraDetailsViewModelFactory(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (blockID != null) {
            return (T) new ViewBlockExtraDetailsViewModel(burstBlockchainService, blockID);
        } else {
            throw new IllegalArgumentException("Block ID not set.");
        }
    }

    public void setBlockID(@Nullable BigInteger blockID) {
        this.blockID = blockID;
    }
}
