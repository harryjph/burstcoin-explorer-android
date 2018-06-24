package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.Transaction;
import com.harry1453.burst.explorer.service.BurstBlockchainService;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewTransactionDetailsViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Transaction> transaction = new MutableLiveData<>();

    public ViewTransactionDetailsViewModel(BurstBlockchainService burstBlockchainService, BigInteger transactionID) {
        compositeDisposable.add(burstBlockchainService.fetchTransaction(transactionID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTransaction, t -> onError()));
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
