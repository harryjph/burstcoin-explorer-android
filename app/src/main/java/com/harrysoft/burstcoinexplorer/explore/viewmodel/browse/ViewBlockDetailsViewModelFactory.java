package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewBlockDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstBlockchainService burstBlockchainService;

    @Nullable
    private BigInteger blockID;
    @Nullable
    private BigInteger blockNumber;

    @Inject
    ViewBlockDetailsViewModelFactory(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (blockID != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockID(burstBlockchainService, blockID);
        } else if (blockNumber != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockNumber(burstBlockchainService, blockNumber);
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
