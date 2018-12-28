package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import burst.kit.entity.BurstAddress;

@Dao
public interface SavedAccountDao {
    @Query("SELECT * FROM SavedAccount")
    LiveData<List<SavedAccount>> getAll();

    @Query("SELECT * FROM SavedAccount WHERE id IN (:ids)")
    List<SavedAccount> loadAllByIds(int[] ids);

    @Query("SELECT * FROM SavedAccount WHERE id = (:id)")
    SavedAccount findByID(long id);

    @Query("SELECT * FROM SavedAccount WHERE address = (:address)")
    SavedAccount findByAddress(BurstAddress address);

    @Query("SELECT * FROM SavedAccount WHERE address = (:address)")
    LiveData<SavedAccount> findLiveByAddress(BurstAddress address);

    @Insert
    void insert(SavedAccount... savedAccounts);

    @Delete
    void delete(SavedAccount savedAccount);

    @Update
    void update(SavedAccount... savedAccounts);
}