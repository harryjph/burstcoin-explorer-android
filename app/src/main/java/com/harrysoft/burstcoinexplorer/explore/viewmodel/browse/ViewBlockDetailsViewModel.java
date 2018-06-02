package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockDetailsViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Block> blockData = new MutableLiveData<>();
    private final MutableLiveData<BigInteger> blockIDData = new MutableLiveData<>();

    @Nullable
    private BigInteger blockID;
    @Nullable
    private BigInteger blockNumber;

    private ViewBlockDetailsViewModel(BurstBlockchainService burstBlockchainService, @NonNull BigInteger block, ConfigurationType configurationType) {
        switch (configurationType) {
            case BLOCK_ID:
                this.blockID = block;
                compositeDisposable.add(burstBlockchainService.fetchBlockByID(blockID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                blockIDData.postValue(blockID);
                break;

            case BLOCK_NUMBER:
                this.blockNumber = block;
                compositeDisposable.add(burstBlockchainService.fetchBlockByHeight(blockNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    private ViewBlockDetailsViewModel(@NonNull Block block) {
        onBlock(block);
    }

    static ViewBlockDetailsViewModel fromBlockID(BurstBlockchainService burstBlockchainService, @NonNull BigInteger blockID) {
        return new ViewBlockDetailsViewModel(burstBlockchainService, blockID, ConfigurationType.BLOCK_ID);
    }

    static ViewBlockDetailsViewModel fromBlockNumber(BurstBlockchainService burstBlockchainService, @NonNull BigInteger blockNumber) {
        return new ViewBlockDetailsViewModel(burstBlockchainService, blockNumber, ConfigurationType.BLOCK_NUMBER);
    }

    static ViewBlockDetailsViewModel fromBlock(@NonNull Block block) {
        return new ViewBlockDetailsViewModel(block);
    }

    private void onError() {
        blockData.postValue(null);
    }

    private void onBlock(Block block) {
        this.blockID = block.blockID;
        this.blockNumber = block.blockNumber;

        blockData.postValue(block);
        blockIDData.postValue(block.blockID);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    private enum ConfigurationType {
        BLOCK_ID,
        BLOCK_NUMBER,
    }

    public LiveData<Block> getBlock() { return blockData; }
    public LiveData<BigInteger> getBlockID() { return blockIDData; }
}
