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

import burst.kit.entity.BurstID;
import burst.kit.entity.response.BlockResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockDetailsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final BurstNodeService burstNodeService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<BlockResponse> blockData = new MutableLiveData<>();

    @Nullable
    private BurstID blockID;
    @Nullable
    private Long blockNumber;

    private ViewBlockDetailsViewModel(BurstNodeService burstNodeService, @NonNull BigInteger block, ConfigurationType configurationType) {
        this.burstNodeService = burstNodeService;
        switch (configurationType) {
            case BLOCK_ID:
                this.blockID = new BurstID(block.toString());
                compositeDisposable.add(burstNodeService.getBlock(blockID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                break;

            case BLOCK_NUMBER:
                this.blockNumber = block.longValue();
                compositeDisposable.add(burstNodeService.getBlock(blockNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBlock, t -> onError()));
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    static ViewBlockDetailsViewModel fromBlockID(BurstNodeService burstNodeService, @NonNull BigInteger blockID) {
        return new ViewBlockDetailsViewModel(burstNodeService, blockID, ConfigurationType.BLOCK_ID);
    }

    static ViewBlockDetailsViewModel fromBlockNumber(BurstNodeService burstNodeService, @NonNull BigInteger blockNumber) {
        return new ViewBlockDetailsViewModel(burstNodeService, blockNumber, ConfigurationType.BLOCK_NUMBER);
    }

    private void onError() {
        blockData.postValue(null);
    }

    private void onBlock(BlockResponse block) {
        this.blockID = block.getBlock();
        this.blockNumber = block.getHeight();

        if (block.getGenerator() != null) {
             onBlockWithGenerator(block);
        } else {
            compositeDisposable.add(burstNodeService.getAccount(block.getGenerator())
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

    private void onBlockWithGenerator(BlockResponse block) {
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

    public LiveData<BlockResponse> getBlock() { return blockData; }
}
