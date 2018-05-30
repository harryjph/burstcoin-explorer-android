package com.harrysoft.burstcoinexplorer.accounts.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.crashlytics.android.Crashlytics;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;
import com.harrysoft.burstcoinexplorer.burst.api.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SavedAccountViewModel extends ViewModel implements SwipeRefreshLayout.OnRefreshListener {

    private final BurstBlockchainService burstBlockchainService;
    private final AccountsDatabase accountsDatabase;
    private final CompositeDisposable compositeDisposable;

    private final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();
    private final MutableLiveData<String> addressBoxText = new MutableLiveData<>();
    private final MutableLiveData<String> addressBoxError = new MutableLiveData<>();
    private final MutableLiveData<LiveData<List<SavedAccount>>> savedAccountsList = new MutableLiveData<>();

    SavedAccountViewModel(BurstBlockchainService burstBlockchainService, AccountsDatabase accountsDatabase) {
        this.burstBlockchainService = burstBlockchainService;
        this.accountsDatabase = accountsDatabase;
        compositeDisposable = new CompositeDisposable();

        // Update immediately
        refreshing.postValue(true);
        onRefresh();
    }

    private void onSavedAccountsList(LiveData<List<SavedAccount>> savedAccountsList) {
        refreshing.postValue(false);
        this.savedAccountsList.postValue(savedAccountsList);
    }

    public void addToDatabase(Context context, BigInteger numericID) {
        compositeDisposable.add(SavedAccountsUtils.saveAccount(context, accountsDatabase, numericID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            addressBoxText.postValue("");
                            onRefresh();
                        },
                        t -> {
                            if (t.getMessage().equals(context.getString(R.string.error_account_already_in_database))) {
                                addressBoxError.postValue(t.getMessage());
                            } else {
                                Crashlytics.logException(t);
                            }
                        }));
    }

    public void updateSavedInfo(BigInteger accountID) {
        compositeDisposable.add(Completable.fromAction(() -> {
            SavedAccount savedAccount = accountsDatabase.savedAccountDao().findByNumericID(accountID);
            if (savedAccount != null) {
                Account account = burstBlockchainService.fetchAccount(savedAccount.getNumericID()).blockingGet();
                savedAccount.setLastKnownBalance(account.balance);
                savedAccount.setLastKnownName(account.name);
                if (accountsDatabase.isOpen()) {
                    accountsDatabase.savedAccountDao().update(savedAccount);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(() -> {}, t -> {}));
    }

    @Override
    public void onRefresh() {
        compositeDisposable.add(Single.fromCallable(() -> accountsDatabase.savedAccountDao().getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSavedAccountsList, t -> refreshing.postValue(false)));
    }

    @Override
    protected void onCleared() {
        accountsDatabase.close();
        compositeDisposable.dispose();
        super.onCleared();
    }

    public LiveData<Boolean> getRefreshing() { return refreshing; }
    public LiveData<String> getAddressBoxText() { return addressBoxText; }
    public LiveData<String> getAddressBoxError() { return addressBoxError; }
    public LiveData<LiveData<List<SavedAccount>>> getSavedAccountsList() { return savedAccountsList; }
}
