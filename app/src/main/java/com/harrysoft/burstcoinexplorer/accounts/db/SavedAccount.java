package com.harrysoft.burstcoinexplorer.accounts.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import burst.kit.entity.BurstAddress;
import burst.kit.entity.BurstValue;

@Entity
public class SavedAccount {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "address")
    private BurstAddress address;

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

    public BurstAddress getAddress() {
        return address;
    }

    public void setAddress(BurstAddress address) {
        this.address = address;
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
