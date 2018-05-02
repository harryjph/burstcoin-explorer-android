package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.math.BigInteger;
import java.util.List;

@Dao
public interface SavedAccountDao {
    @Query("SELECT * FROM SavedAccount")
    LiveData<List<SavedAccount>> getAll();

    @Query("SELECT * FROM SavedAccount WHERE id IN (:ids)")
    List<SavedAccount> loadAllByIds(int[] ids);

    @Query("SELECT * FROM SavedAccount WHERE id = (:id)")
    SavedAccount findByID(long id);

    @Query("SELECT * FROM SavedAccount WHERE numericID = (:numericID)")
    SavedAccount findByNumericID(BigInteger numericID);

    @Query("SELECT * FROM SavedAccount WHERE numericID = (:numericID)")
    LiveData<SavedAccount> findLiveByNumericID(BigInteger numericID);

    @Insert
    void insert(SavedAccount... savedAccounts);

    @Delete
    void delete(SavedAccount savedAccount);

    @Update
    void update(SavedAccount... savedAccounts);
}