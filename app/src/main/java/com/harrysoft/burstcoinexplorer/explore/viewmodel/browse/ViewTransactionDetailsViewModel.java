package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import burst.kit.entity.BurstID;
import burst.kit.entity.response.TransactionResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewTransactionDetailsViewModel extends ViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BurstID transactionID;

    private final MutableLiveData<TransactionResponse> transaction = new MutableLiveData<>();

    public ViewTransactionDetailsViewModel(BurstNodeService burstNodeService, BurstID transactionID) {
        this.transactionID = transactionID;
        compositeDisposable.add(burstNodeService.getTransaction(transactionID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTransaction, t -> onError()));
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("transaction_id", transactionID.toString());
    }

    private void onTransaction(TransactionResponse transaction) {
        this.transaction.postValue(transaction);
    }

    private void onError() {
        transaction.postValue(null);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<TransactionResponse> getTransaction() { return transaction; }
}
