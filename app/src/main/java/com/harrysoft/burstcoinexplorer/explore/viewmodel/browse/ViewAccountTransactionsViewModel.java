package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.annotation.StringRes;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.util.Arrays;
import java.util.List;

import burst.kit.entity.BurstAddress;
import burst.kit.entity.BurstID;
import burst.kit.service.BurstNodeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountTransactionsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final BurstNodeService burstNodeService;
    private final BurstAddress account;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<BurstID>> transactionIDs = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionsLabel = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();

    ViewAccountTransactionsViewModel(BurstNodeService burstNodeService, BurstAddress account) {
        this.burstNodeService = burstNodeService;
        this.account = account;

        address.postValue(account.getFullAddress());
        fetchTransactions();
    }

    private void fetchTransactions() {
        compositeDisposable.add(burstNodeService.getAccountTransactionIDs(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionIDs -> this.transactionIDs.postValue(Arrays.asList(transactionIDs.getTransactionIds())),t -> onError()));
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("account_id", account.toString());
    }

    public void setTransactionsLabel(@StringRes int text) {
        transactionsLabel.postValue(text);
    }

    private void onError() {
        transactionsLabel.postValue(R.string.transactions_error);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<List<BurstID>> getTransactionIDs() { return transactionIDs; }
    public LiveData<Integer> getTransactionsLabel() { return transactionsLabel; }
    public LiveData<String> getAddress() { return address; }
}
