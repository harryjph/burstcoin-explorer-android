package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.annotation.StringRes;

import com.harry1453.burst.explorer.entity.BurstAddress;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountTransactionsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final BurstBlockchainService burstBlockchainService;
    private final BurstAddress account;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<BigInteger>> transactionIDs = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionsLabel = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();

    ViewAccountTransactionsViewModel(BurstBlockchainService burstBlockchainService, BurstAddress account) {
        this.burstBlockchainService = burstBlockchainService;
        this.account = account;

        address.postValue(account.getFullAddress());
        fetchTransactions();
    }

    private void fetchTransactions() {
        compositeDisposable.add(burstBlockchainService.fetchAccountTransactions(account.getNumericID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionIDs::postValue, t -> onError()));
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("account_id", account.getNumericID().toString());
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

    public LiveData<List<BigInteger>> getTransactionIDs() { return transactionIDs; }
    public LiveData<Integer> getTransactionsLabel() { return transactionsLabel; }
    public LiveData<String> getAddress() { return address; }
}
