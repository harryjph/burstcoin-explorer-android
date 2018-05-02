package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.harrysoft.burstcoinexplorer.burst.entity.BurstValue;

import java.math.BigInteger;

@Entity
public class SavedAccount {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "numericID")
    private BigInteger numericID;

    @ColumnInfo(name = "lastKnownBalance")
    private BurstValue lastKnownBalance;

    @ColumnInfo(name = "lastKnownName")
    private String lastKnownName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigInteger getNumericID() {
        return numericID;
    }

    public void setNumericID(BigInteger numericID) {
        this.numericID = numericID;
    }

    public BurstValue getLastKnownBalance() {
        return lastKnownBalance;
    }

    public void setLastKnownBalance(BurstValue lastKnownBalance) {
        this.lastKnownBalance = lastKnownBalance;
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public void setLastKnownName(String lastKnownName) {
        this.lastKnownName = lastKnownName;
    }
}
