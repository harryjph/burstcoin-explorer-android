package com.harrysoft.burstcoinexplorer.accounts;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.accounts.db.AccountsDatabase;
import com.harrysoft.burstcoinexplorer.accounts.db.SavedAccount;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SavedAccountsUtils {
    public static Single<LiveData<SavedAccount>> getLiveAccount(AccountsDatabase accountsDatabase, BigInteger accountID) {
        return Single.fromCallable(() -> accountsDatabase.savedAccountDao().findLiveByNumericID(accountID));
    }
    public static Completable saveAccount(Context context, AccountsDatabase accountsDatabase, SavedAccount accountToSave) {
        return Completable.fromAction(() -> {
            if (accountsDatabase.savedAccountDao().findByNumericID(accountToSave.getNumericID()) != null) {
                throw new IllegalArgumentException(context.getString(R.string.error_account_already_in_database));
            }
            accountsDatabase.savedAccountDao().insert(accountToSave);
        });
    }

    public static Completable deleteAccount(Context context, AccountsDatabase accountsDatabase, BigInteger accountIDToRemove) {
        return Completable.fromAction(() -> {
            SavedAccount accountToRemove = accountsDatabase.savedAccountDao().findByNumericID(accountIDToRemove);
            if (accountToRemove != null) {
                accountsDatabase.savedAccountDao().delete(accountToRemove);
            } else {
                throw new IllegalArgumentException(context.getString(R.string.error_account_not_in_database));
            }
        });
    }

    public static Completable saveAccount(Context context, AccountsDatabase accountsDatabase, BigInteger accountID) {
        SavedAccount savedAccount = new SavedAccount();
        savedAccount.setNumericID(accountID);
        return saveAccount(context, accountsDatabase, savedAccount);
    }
}
