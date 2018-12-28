package com.harrysoft.burstcoinexplorer.explore.viewmodel.browse;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
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
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.math.BigInteger;

import burst.kit.entity.BurstAddress;
import burst.kit.entity.BurstID;
import burst.kit.entity.response.AccountResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewAccountDetailsViewModel extends AndroidViewModel implements NfcAdapter.CreateNdefMessageCallback {

    private final BurstNodeService burstNodeService;
    private final AccountsDatabase accountsDatabase;
    private final BurstAddress address;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<AccountResponse> accountData = new MutableLiveData<>();
    private final MutableLiveData<LiveData<SavedAccount>> savedAccount = new MutableLiveData<>();
    private final MutableLiveData<Integer> saveButtonVisibility = new MutableLiveData<>();

    @Nullable
    private AccountResponse account;

    ViewAccountDetailsViewModel(Application application, BurstNodeService burstNodeService, AccountsDatabase accountsDatabase, @NonNull BurstAddress address) {
        super(application);
        this.burstNodeService = burstNodeService;
        this.accountsDatabase = accountsDatabase;
        this.address = address;

        fetchAccount();
        setupAccountSave();
    }

    public void viewExtra(Context context) {
        if (account != null) {
            ExplorerRouter.viewAccountTransactions(context, new BurstID(account.getAccount().getID())); // TODO
        } else {
            ExplorerRouter.viewAccountTransactions(context, new BurstID(address.getID())); // TODO
        }
    }

    private void fetchAccount() {
        compositeDisposable.add(burstNodeService.getAccount(address)
                .subscribe(this::onAccount, t -> onAccount(null)));
    }

    private void onAccount(@Nullable AccountResponse account) {
        this.account = account;
        accountData.postValue(account);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    private Completable saveAccount(AccountsDatabase accountsDatabase) {
        if (account != null) {
            SavedAccount savedAccount =  new SavedAccount();
            savedAccount.setAddress(account.getAccount());
            savedAccount.setLastKnownName(account.getName());
            savedAccount.setLastKnownBalance(account.getBalanceNQT());
            return SavedAccountsUtils.saveAccount(getApplication(), accountsDatabase, savedAccount);
        } else {
            return SavedAccountsUtils.saveAccount(getApplication(), accountsDatabase, address);
        }
    }

    private Completable deleteAccount(AccountsDatabase accountsDatabase) {
        return SavedAccountsUtils.deleteAccount(getApplication(), accountsDatabase, address);
    }

    private void setupAccountSave() {
        compositeDisposable.add(SavedAccountsUtils.getLiveAccount(accountsDatabase, address)
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

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return NfcUtils.createBeamMessage("account_id", address.toString());
    }

    public LiveData<AccountResponse> getAccount() { return accountData; }
    public LiveData<LiveData<SavedAccount>> getSavedAccount() { return savedAccount; }
    public LiveData<Integer> getSaveButtonVisibility() { return saveButtonVisibility; }
}
