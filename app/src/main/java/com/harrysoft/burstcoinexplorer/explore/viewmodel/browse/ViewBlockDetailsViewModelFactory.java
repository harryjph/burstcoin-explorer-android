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
    @Nullable
    private Block block;

    @Inject
    ViewBlockDetailsViewModelFactory(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (block != null) {
            return (T) ViewBlockDetailsViewModel.fromBlock(block);
        } else if (blockID != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockID(burstBlockchainService, blockID);
        } else if (blockNumber != null) {
            return (T) ViewBlockDetailsViewModel.fromBlockNumber(burstBlockchainService, blockNumber);
        } else {
            throw new IllegalArgumentException("No parameters set.");
        }
    }

    public boolean canCreate() {
        return block != null || blockID != null || blockNumber != null;
    }

    public void setBlockID(BigInteger blockID) {
        this.blockID = blockID;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
