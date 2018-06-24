package com.harrysoft.burstcoinexplorer.observe.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.harry1453.burst.explorer.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.util.OnRefreshRequestListener;

public abstract class ObserveSubFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    private OnRefreshRequestListener onRefreshRequestListener;
    @Nullable
    private SwipeRefreshLayout swipeRefreshLayout;

    void setupRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.observe_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setUp(OnRefreshRequestListener onRefreshRequestListener) {
        this.onRefreshRequestListener = onRefreshRequestListener;
    }

    @Override
    public void onRefresh() {
        if (onRefreshRequestListener != null) {
            onRefreshRequestListener.requestRefresh();
        } else {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected abstract void onError(Throwable error);

    public abstract void onNetworkStatus(NetworkStatus networkStatus);
}