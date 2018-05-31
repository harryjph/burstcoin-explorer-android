package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.ArrayMap;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewTransactionsViewModel extends AndroidViewModel {

    private final BurstBlockchainService burstBlockchainService;
    private final TransactionDisplayType displayType;
    private final List<BigInteger> transactionIDs;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<Map<BigInteger, Transaction>> transactionsData = new MutableLiveData<>();

    private Map<BigInteger, Transaction> transactions = new ArrayMap<>();

    ViewTransactionsViewModel(Application application, TransactionDisplayType displayType, BurstBlockchainService burstBlockchainService, List<BigInteger> transactionIDs) {
        super(application);
        this.displayType = displayType;
        this.burstBlockchainService = burstBlockchainService;
        this.transactionIDs = transactionIDs;

        loadMoreTransactions();
    }

    private boolean loading;
    private int displayedItems = 0;

    public void loadMoreTransactions() {
        if (!loading) {
            loading = true;
            int tempDisplayedItems = displayedItems + 25;
            if (tempDisplayedItems > transactionIDs.size()) {
                tempDisplayedItems = transactionIDs.size();
            }

            int transactionsToAdd = tempDisplayedItems - displayedItems;

            ArrayMap<BigInteger, Transaction> newTransactions = new ArrayMap<>();

            for (int i = 1; i <= transactionsToAdd; i++) {
                BigInteger transactionID = transactionIDs.get(displayedItems + i - 1); // get counts from 0, i counts from 1
                compositeDisposable.add(burstBlockchainService.fetchTransaction(transactionID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(transaction -> {
                            newTransactions.put(transactionID, transaction);
                            if (newTransactions.size() == transactionsToAdd) {
                                finaliseTransactionLoad(newTransactions, transactionsToAdd);
                            }
                        }, error -> Toast.makeText(getApplication(), "Error loading transaction #" + transactionID.toString(), Toast.LENGTH_LONG).show()));
            }
        }
    }

    private void finaliseTransactionLoad(Map<BigInteger, Transaction> newTransactions, int newDisplayedItems) {
        displayedItems += newDisplayedItems;
        transactions.putAll(newTransactions);
        transactionsData.postValue(transactions);
        loading = false;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<Map<BigInteger, Transaction>> getTransactions() { return transactionsData; }
    public List<BigInteger> getTransactionIDs() { return transactionIDs; }
    public TransactionDisplayType getDisplayType() { return displayType; }
}
