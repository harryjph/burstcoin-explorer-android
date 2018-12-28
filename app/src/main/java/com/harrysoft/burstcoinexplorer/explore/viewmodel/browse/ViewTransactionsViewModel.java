package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.ArrayMap;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.explore.entity.TransactionDisplayType;

import java.util.List;
import java.util.Map;

import burst.kit.entity.BurstID;
import burst.kit.entity.response.TransactionResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.disposables.CompositeDisposable;

public class ViewTransactionsViewModel extends AndroidViewModel {

    private final BurstNodeService burstNodeService;
    private final TransactionDisplayType displayType;
    private final List<BurstID> transactionIDs;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Map<BurstID, TransactionResponse>> transactionsData = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionsLabel = new MutableLiveData<>();

    private final Map<BurstID, TransactionResponse> transactions = new ArrayMap<>();

    ViewTransactionsViewModel(Application application, TransactionDisplayType displayType, BurstNodeService burstNodeService, List<BurstID> transactionIDs) {
        super(application);
        this.displayType = displayType;
        this.burstNodeService = burstNodeService;
        this.transactionIDs = transactionIDs;

        if (transactionIDs.size() == 0) {
            transactionsLabel.postValue(R.string.transactions_empty);
        } else {
            loadMoreTransactions();
        }
    }

    private boolean loading;
    private int displayedItems = 0;

    public void loadMoreTransactions() {
        if (!loading) {
            loading = true;
            transactionsLabel.postValue(R.string.transactions_loading);

            int tempDisplayedItems = displayedItems + 25;
            if (tempDisplayedItems > transactionIDs.size()) {
                tempDisplayedItems = transactionIDs.size();
            }

            int transactionsToAdd = tempDisplayedItems - displayedItems;

            ArrayMap<BurstID, TransactionResponse> newTransactions = new ArrayMap<>();

            for (int i = 1; i <= transactionsToAdd; i++) {
                BurstID transactionID = transactionIDs.get(displayedItems + i - 1); // get counts from 0, i counts from 1
                compositeDisposable.add(burstNodeService.getTransaction(transactionID)
                        .subscribe(transaction -> {
                            newTransactions.put(transactionID, transaction);
                            if (newTransactions.size() == transactionsToAdd) {
                                finaliseTransactionLoad(newTransactions, transactionsToAdd);
                            }
                        }, error -> Toast.makeText(getApplication(), "Error loading transaction #" + transactionID.toString(), Toast.LENGTH_LONG).show()));
            }
        }
    }

    private void finaliseTransactionLoad(Map<BurstID, TransactionResponse> newTransactions, int newDisplayedItems) {
        displayedItems += newDisplayedItems;
        transactions.putAll(newTransactions);
        transactionsData.postValue(transactions);
        loading = false;
        transactionsLabel.postValue(R.string.transactions);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<Map<BurstID, TransactionResponse>> getTransactions() { return transactionsData; }
    public LiveData<Integer> getTransactionsLabel() { return transactionsLabel; }
    public List<BurstID> getTransactionIDs() { return transactionIDs; }
    public TransactionDisplayType getDisplayType() { return displayType; }
}
