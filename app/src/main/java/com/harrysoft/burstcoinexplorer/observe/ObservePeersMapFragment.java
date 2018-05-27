package com.harrysoft.burstcoinexplorer.observe;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.RepoInfoService;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ObservePeersMapFragment extends ObserveSubFragment {

    private ViewTreeObserver.OnScrollChangedListener scrollChangedListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    private MutableLiveData<String> url = new MutableLiveData<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe_map, container, false);

        setupRefresh(view);

        swipeRefreshLayout = view.findViewById(R.id.observe_swiperefresh);
        WebView webView = view.findViewById(R.id.observe_map_webview);
        ProgressBar progressBar = view.findViewById(R.id.observe_map_progressbar);

        scrollChangedListener = () -> swipeRefreshLayout.setEnabled(webView.getScrollY() == 0);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

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

        url.observe(this, webView::loadUrl);

        return view;
    }

    @Override
    protected void onRefreshError(Throwable error, boolean refreshing) {
        // todo error
    }

    @Override
    public void onNetworkStatus(NetworkStatus networkStatus) {
        RepoInfoService.getNetworkMapDisplayURL(networkStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUrl, Throwable::printStackTrace);
    }

    public void onUrl(String url) {
        this.url.postValue(url);
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(scrollChangedListener);
        super.onStop();
    }
}
