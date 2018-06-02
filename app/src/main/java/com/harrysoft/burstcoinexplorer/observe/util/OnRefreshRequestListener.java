package com.harrysoft.burstcoinexplorer.observe.util;

import android.support.annotation.NonNull;

import com.harrysoft.burstcoinexplorer.observe.ui.ObserveSubFragment;

public interface OnRefreshRequestListener {
    void requestRefresh(@NonNull ObserveSubFragment sender);
}