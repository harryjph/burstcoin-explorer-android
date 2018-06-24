package com.harrysoft.burstcoinexplorer.burst.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import io.reactivex.Single;

public final class AndroidNetworkService implements NetworkService {

    private final RequestQueue requestQueue;

    public AndroidNetworkService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public Single<String> fetchData(String url) {
        return Single.create(e -> requestQueue.add(new StringRequest(url, e::onSuccess, e::onError)));
    }
}
