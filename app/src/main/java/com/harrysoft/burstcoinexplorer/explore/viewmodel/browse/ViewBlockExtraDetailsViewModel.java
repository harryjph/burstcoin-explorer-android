package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.annotation.StringRes;

import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import burst.kit.entity.BurstID;
import burst.kit.entity.response.BlockResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockExtraDetailsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BurstID blockID;

    private final MutableLiveData<List<BurstID>> transactionIDs = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionsLabel = new MutableLiveData<>();
    private final MutableLiveData<String> blockNumberText = new MutableLiveData<>();
    private final MutableLiveData<String> blockRewardText = new MutableLiveData<>();

    ViewBlockExtraDetailsViewModel(BurstNodeService burstNodeService, BurstID blockID) {
        this.blockID = blockID;
        compositeDisposable.add(burstNodeService.getBlock(blockID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlock, t -> onError()));
    }

    private void onBlock(BlockResponse block) {
        transactionIDs.postValue(Arrays.asList(block.getTransactions()));
        blockNumberText.postValue(String.format(Locale.getDefault(), "%d", block.getHeight()));
        blockRewardText.postValue(block.getBlockReward().toString());
    }

    private void onError() {
        transactionIDs.postValue(null);
        blockNumberText.postValue(null);
        blockRewardText.postValue(null);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("block_id", blockID.toString());
    }

    public void setTransactionsLabel(@StringRes int text) {
        transactionsLabel.postValue(text);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<List<BurstID>> getTransactionIDs() { return transactionIDs; }
    public LiveData<Integer> getTransactionsLabel() { return transactionsLabel; }
    public LiveData<String> getBlockNumberText() { return blockNumberText; }
    public LiveData<String> getBlockRewardText() { return blockRewardText; }
}
