package com.harrysoft.burstcoinexplorer.main.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.harry1453.burst.explorer.service.NetworkService;

import io.reactivex.Single;

public final class AndroidNetworkService implements NetworkService {

    private final RequestQueue requestQueue;

    public AndroidNetworkService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public Single<String> fetchData(@NonNull String url) {
        return Single.create(emitter -> {
            StringRequest request = new StringRequest(url, emitter::onSuccess, emitter::onError);
            requestQueue.add(request);
            emitter.setCancellable(request::cancel);
        });
    }
}
