package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigInteger;

import javax.inject.Inject;

import burst.kit.service.BurstNodeService;

public class ViewBlockDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNodeService burstNodeService;

    @Nullable
    private BigInteger blockID;
    @Nullable
    private BigInteger blockNumber;

    @Inject
    ViewBlockDetailsViewModelFactory(BurstNodeService burstNodeService) {
        this.burstNodeService = burstNodeService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (blockID != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockID(burstNodeService, blockID);
        } else if (blockNumber != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockNumber(burstNodeService, blockNumber);
        } else {
            throw new IllegalArgumentException("No parameters set.");
        }
    }

    public boolean canCreate() {
        return blockID != null || blockNumber != null;
    }

    public void setBlockID(@Nullable BigInteger blockID) {
        this.blockID = blockID;
    }

    public void setBlockNumber(@Nullable BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }
}
