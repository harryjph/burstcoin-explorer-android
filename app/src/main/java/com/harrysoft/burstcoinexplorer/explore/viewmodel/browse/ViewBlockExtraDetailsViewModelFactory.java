package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;

import javax.inject.Inject;

public class ViewBlockExtraDetailsViewModelFactory implements ViewModelProvider.Factory {

    @Nullable
    private Block block;

    @Inject
    ViewBlockExtraDetailsViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (block != null) {
            return (T) new ViewBlockExtraDetailsViewModel(block);
        } else {
            throw new IllegalArgumentException("Block ID not set.");
        }
    }

    public void setBlock(@Nullable Block block) {
        this.block = block;
    }
}
