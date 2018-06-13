package com.harrysoft.burstcoinexplorer.burst.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.harrysoft.burstcoinexplorer.burst.service.entity.EntityDoesNotExistException;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.burst.service.entity.NullResponseException;

import io.reactivex.Single;

public class PoCCNetworkService implements BurstNetworkService {

    private final String API_URL = "https://explore.burst.cryptoguru.org/api/v1/";
    private final String NETWORK_STATUS_URL = API_URL + "observe/";

    private final RequestQueue requestQueue;
    private final Gson gson;

    public PoCCNetworkService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.gson = new Gson();
    }

    @Override
    public Single<NetworkStatus> fetchNetworkStatus() {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, NETWORK_STATUS_URL, response -> {
                if (response != null) {
                    NetworkStatus networkStatus;
                    try {
                        networkStatus = gson.fromJson(response, NetworkStatusApiResponse.class).data;
                    } catch (JsonSyntaxException ex) {
                        e.onError(ex);
                        return;
                    }

                    if (networkStatus != null) {
                        e.onSuccess(networkStatus);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    private class NetworkStatusApiResponse {
        NetworkStatus data;
    }
}
