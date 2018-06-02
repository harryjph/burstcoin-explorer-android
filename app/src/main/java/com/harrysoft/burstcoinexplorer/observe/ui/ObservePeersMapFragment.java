package com.harrysoft.burstcoinexplorer.observe.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.observe.viewmodel.ObserveMapViewModel;

public class ObservePeersMapFragment extends ObserveSubFragment {

    private ViewTreeObserver.OnScrollChangedListener scrollChangedListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ObserveMapViewModel observeMapViewModel;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe_map, container, false);

        setupRefresh(view);

        observeMapViewModel = ViewModelProviders.of(this).get(ObserveMapViewModel.class);

        swipeRefreshLayout = view.findViewById(R.id.observe_swiperefresh);
        WebView webView = view.findViewById(R.id.observe_map_webview);
        ProgressBar progressBar = view.findViewById(R.id.observe_map_progressbar);

        scrollChangedListener = () -> swipeRefreshLayout.setEnabled(webView.getScrollY() == 0);

        // Setup
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        // Optimise
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(scrollChangedListener);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                progressBar.setProgress(progress);
                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        observeMapViewModel.getURL().observe(this, webView::loadUrl);

        return view;
    }

    @Override
    protected void onError(Throwable error) {
        // todo error
    }

    @Override
    public void onNetworkStatus(NetworkStatus networkStatus) {
        if (observeMapViewModel != null) {
            observeMapViewModel.onNetworkStatus(networkStatus);
        }
    }

    @Override
    public void onDestroyView() {
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(scrollChangedListener);
        super.onDestroyView();
    }
}
