package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.harry1453.burst.explorer.entity.Account;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;
import com.harrysoft.burstcoinexplorer.accounts.util.SavedAccountsUtils;
import com.harrysoft.burstcoinexplorer.main.router.ExplorerRouter;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountDetailsViewModel extends AndroidViewModel {

    private final BurstBlockchainService burstBlockchainService;
    private final AccountsDatabase accountsDatabase;
    private final BigInteger accountID;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Account> accountData = new MutableLiveData<>();
    private final MutableLiveData<LiveData<SavedAccount>> savedAccount = new MutableLiveData<>();
    private final MutableLiveData<Integer> saveButtonVisibility = new MutableLiveData<>();

    @Nullable
    private Account account;

    ViewAccountDetailsViewModel(Application application, BurstBlockchainService burstBlockchainService, AccountsDatabase accountsDatabase, @NonNull BigInteger accountID) {
        super(application);
        this.burstBlockchainService = burstBlockchainService;
        this.accountsDatabase = accountsDatabase;
        this.accountID = accountID;

        fetchAccount();
        setupAccountSave();
    }

    public void viewExtra(Context context) {
        if (account != null) {
            ExplorerRouter.viewAccountTransactions(context, account.address.getNumericID());
        } else {
            ExplorerRouter.viewAccountTransactions(context, accountID);
        }
    }

    private void fetchAccount() {
        compositeDisposable.add(burstBlockchainService.fetchAccount(accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAccount, t -> onAccount(null)));
    }

    private void onAccount(@Nullable Account account) {
        this.account = account;
        accountData.postValue(account);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        accountsDatabase.close();
    }

    private Completable saveAccount(AccountsDatabase accountsDatabase) {
        if (account != null) {
            SavedAccount savedAccount =  new SavedAccount();
            savedAccount.setNumericID(account.address.getNumericID());
            savedAccount.setLastKnownName(account.name);
            savedAccount.setLastKnownBalance(account.balance);
            return SavedAccountsUtils.saveAccount(getApplication(), accountsDatabase, savedAccount);
        } else {
            return SavedAccountsUtils.saveAccount(getApplication(), accountsDatabase, accountID);
        }
    }

    private Completable deleteAccount(AccountsDatabase accountsDatabase) {
        return SavedAccountsUtils.deleteAccount(getApplication(), accountsDatabase, accountID);
    }

    private void setupAccountSave() {
        compositeDisposable.add(SavedAccountsUtils.getLiveAccount(accountsDatabase, accountID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(savedAccount -> {
                    saveButtonVisibility.postValue(View.VISIBLE);
                    this.savedAccount.postValue(savedAccount);
                }, t -> saveButtonVisibility.postValue(View.GONE)));
    }

    public View.OnClickListener getSaveOnClickListener() {
        return v -> {
            Completable saveAccountCompletable = saveAccount(accountsDatabase);
            if (saveAccountCompletable != null) {
                compositeDisposable.add(saveAccountCompletable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {},
                                t -> {
                                    if (t.getMessage().equals(getApplication().getString(R.string.error_account_already_in_database))) {
                                        Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Crashlytics.logException(t);
                                    }
                                }));
            }
        };
    }

    public View.OnClickListener getDeleteOnClickListener() {
        return v -> {
            Completable deleteAccountCompletable = deleteAccount(accountsDatabase);
            if (deleteAccountCompletable != null) {
                compositeDisposable.add(deleteAccountCompletable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {},
                                t -> {
                                    if (t.getMessage().equals(getApplication().getString(R.string.error_account_already_in_database))) {
                                        Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Crashlytics.logException(t);
                                    }
                                }));
            }
        };
    }

    public LiveData<Account> getAccount() { return accountData; }
    public LiveData<LiveData<SavedAccount>> getSavedAccount() { return savedAccount; }
    public LiveData<Integer> getSaveButtonVisibility() { return saveButtonVisibility; }
}
