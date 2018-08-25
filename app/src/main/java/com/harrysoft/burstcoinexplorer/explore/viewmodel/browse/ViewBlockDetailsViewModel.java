package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harry1453.burst.explorer.entity.Block;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockDetailsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final BurstBlockchainService burstBlockchainService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Block> blockData = new MutableLiveData<>();

    @Nullable
    private BigInteger blockID;
    @Nullable
    private Long blockNumber;

    private ViewBlockDetailsViewModel(BurstBlockchainService burstBlockchainService, @NonNull BigInteger block, ConfigurationType configurationType) {
        this.burstBlockchainService = burstBlockchainService;
        switch (configurationType) {
            case BLOCK_ID:
                this.blockID = block;
                compositeDisposable.add(burstBlockchainService.fetchBlockByID(blockID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                break;

            case BLOCK_NUMBER:
                this.blockNumber = block.longValue();
                compositeDisposable.add(burstBlockchainService.fetchBlockByHeight(BigInteger.valueOf(blockNumber))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    static ViewBlockDetailsViewModel fromBlockID(BurstBlockchainService burstBlockchainService, @NonNull BigInteger blockID) {
        return new ViewBlockDetailsViewModel(burstBlockchainService, blockID, ConfigurationType.BLOCK_ID);
    }

    static ViewBlockDetailsViewModel fromBlockNumber(BurstBlockchainService burstBlockchainService, @NonNull BigInteger blockNumber) {
        return new ViewBlockDetailsViewModel(burstBlockchainService, blockNumber, ConfigurationType.BLOCK_NUMBER);
    }

    private void onError() {
        blockData.postValue(null);
    }

    private void onBlock(Block block) {
        this.blockID = block.blockID;
        this.blockNumber = block.blockNumber;

        if (block.generator != null) {
             onBlockWithGenerator(block);
        } else {
            compositeDisposable.add(burstBlockchainService.fetchAccount(block.generatorID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(account -> {
                        block.setGenerator(account);
                        onBlockWithGenerator(block);
                    }, t -> onError()));
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        if (blockID != null) {
            return NfcUtils.createBeamMessage("block_id", blockID.toString());
        } else {
            return null;
        }
    }

    private void onBlockWithGenerator(Block block) {
        blockData.postValue(block);
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
}
