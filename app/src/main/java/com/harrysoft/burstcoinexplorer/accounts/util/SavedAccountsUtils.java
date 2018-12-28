package com.harrysoft.burstcoinexplorer.accounts.util;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;

import burst.kit.entity.BurstAddress;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SavedAccountsUtils {
    public static Single<LiveData<SavedAccount>> getLiveAccount(AccountsDatabase accountsDatabase, BurstAddress address) {
        return Single.fromCallable(() -> accountsDatabase.savedAccountDao().findLiveByAddress(address));
    }
    public static Completable saveAccount(Context context, AccountsDatabase accountsDatabase, SavedAccount accountToSave) {
        return Completable.fromAction(() -> {
            if (accountsDatabase.savedAccountDao().findByAddress(accountToSave.getAddress()) != null) {
                throw new IllegalArgumentException(context.getString(R.string.error_account_already_in_database));
            }
            accountsDatabase.savedAccountDao().insert(accountToSave);
        });
    }

    public static Completable deleteAccount(Context context, AccountsDatabase accountsDatabase, BurstAddress addressToRemove) {
        return Completable.fromAction(() -> {
            SavedAccount accountToRemove = accountsDatabase.savedAccountDao().findByAddress(addressToRemove);
            if (accountToRemove != null) {
                accountsDatabase.savedAccountDao().delete(accountToRemove);
            } else {
                throw new IllegalArgumentException(context.getString(R.string.error_account_not_in_database));
            }
        });
    }

    public static Completable saveAccount(Context context, AccountsDatabase accountsDatabase, BurstAddress address) {
        SavedAccount savedAccount = new SavedAccount();
        savedAccount.setAddress(address);
        return saveAccount(context, accountsDatabase, savedAccount);
    }
}
