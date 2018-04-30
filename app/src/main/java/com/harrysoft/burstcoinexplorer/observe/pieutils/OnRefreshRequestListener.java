package com.harrysoft.burstcoinexplorer.observe.pieutils;

import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.observe.ObserveSubFragment;

public interface OnRefreshRequestListener {
    void requestRefresh(@NonNull ObserveSubFragment sender);
}