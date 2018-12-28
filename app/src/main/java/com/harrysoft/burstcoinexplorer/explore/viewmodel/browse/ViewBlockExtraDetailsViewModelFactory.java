package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import burst.kit.entity.BurstID;
import burst.kit.service.BurstNodeService;

public class ViewBlockExtraDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final BurstNodeService burstNodeService;

    @Nullable
    private BurstID blockID;

    @Inject
    ViewBlockExtraDetailsViewModelFactory(BurstNodeService burstNodeService) {
        this.burstNodeService = burstNodeService;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (blockID != null) {
            return (T) new ViewBlockExtraDetailsViewModel(burstNodeService, blockID);
        } else {
            throw new IllegalArgumentException("Block ID not set.");
        }
    }

    public void setBlockID(@Nullable BurstID blockID) {
        this.blockID = blockID;
    }
}
