package com.harrysoft.burstcoinexplorer.observe;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.pieutils.OnRefreshRequestListener;

public abstract class ObserveSubFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnRefreshRequestListener onRefreshRequestListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean refreshing = false;

    void setupRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.observe_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setUp(OnRefreshRequestListener onRefreshRequestListener) {
        this.onRefreshRequestListener = onRefreshRequestListener;
    }

    @Override
    public void onRefresh() {
        refreshing = true;
        if (onRefreshRequestListener != null) {
            onRefreshRequestListener.requestRefresh(this);
        } else {
            refreshing = false;
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onRefreshError(Throwable error) {
        refreshing = false;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        onRefreshError(error, refreshing);
    }

    public void onRefreshed() {
        refreshing = false;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onForkInfos(ForkInfo[] forkInfos) {}

    protected abstract void onRefreshError(Throwable error, boolean refreshing);

    public abstract void onNetworkStatus(NetworkStatus networkStatus);
}