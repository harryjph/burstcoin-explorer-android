package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;

import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBlockExtraDetailsViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<BigInteger>> transactionIDs = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionsLabel = new MutableLiveData<>();
    private final MutableLiveData<String> blockNumberText = new MutableLiveData<>();
    private final MutableLiveData<String> blockRewardText = new MutableLiveData<>();

    ViewBlockExtraDetailsViewModel(BurstBlockchainService burstBlockchainService, BigInteger blockID) {
        compositeDisposable.add(burstBlockchainService.fetchBlockByID(blockID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBlock, t -> onError()));
    }

    private void onBlock(Block block) {
        transactionIDs.postValue(block.transactionIDs);
        blockNumberText.postValue(String.format(Locale.getDefault(), "%d", block.blockNumber));
        blockRewardText.postValue(block.blockReward.toString());
    }

    private void onError() {
        transactionIDs.postValue(null);
        blockNumberText.postValue(null);
        blockRewardText.postValue(null);
    }

    public void setTransactionsLabel(@StringRes int text) {
        transactionsLabel.postValue(text);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<List<BigInteger>> getTransactionIDs() { return transactionIDs; }
    public LiveData<Integer> getTransactionsLabel() { return transactionsLabel; }
    public LiveData<String> getBlockNumberText() { return blockNumberText; }
    public LiveData<String> getBlockRewardText() { return blockRewardText; }
}
