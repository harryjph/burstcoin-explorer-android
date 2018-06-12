package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountTransactionsViewModel extends ViewModel {

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
