package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import com.harry1453.burst.explorer.entity.Transaction;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewTransactionDetailsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BigInteger transactionID;

    private final MutableLiveData<Transaction> transaction = new MutableLiveData<>();

    public ViewTransactionDetailsViewModel(BurstBlockchainService burstBlockchainService, BigInteger transactionID) {
        this.transactionID = transactionID;
        compositeDisposable.add(burstBlockchainService.fetchTransaction(transactionID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTransaction, t -> onError()));
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("transaction_id", transactionID.toString());
    }

    private void onTransaction(Transaction transaction) {
        this.transaction.postValue(transaction);
    }

    private void onError() {
        transaction.postValue(null);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<Transaction> getTransaction() { return transaction; }
}
