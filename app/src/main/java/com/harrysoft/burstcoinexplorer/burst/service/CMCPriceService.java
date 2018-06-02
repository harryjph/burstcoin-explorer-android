package com.harrysoft.burstcoinexplorer.burst.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;

import java.math.BigDecimal;
import java.util.Map;

import io.reactivex.Single;

public class CMCPriceService implements BurstPriceService {

    private static final String burstPriceEndpoint = "https://api.coinmarketcap.com/v2/ticker/573/?convert=";

    private final RequestQueue requestQueue;
    private final Gson gson = new Gson();

    public CMCPriceService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public Single<BurstPrice> fetchPrice(String currencyCode) {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, burstPriceEndpoint + currencyCode, response -> {
                if (response != null) {
                    e.onSuccess(gson.fromJson(response, CMCPriceResult.class).data.quotes.get(currencyCode.toUpperCase()).toBurstPrice(currencyCode.toUpperCase()));
                }
            }, e::onError);
            requestQueue.add(request);
        });
    }

    static class CMCPriceResult {
        CMCPriceData data;
    }

    static class CMCPriceData {
        Map<String, CMCPriceQuote> quotes;
    }

    static class CMCPriceQuote {
        BigDecimal price;
        BigDecimal market_cap;

        private BurstPrice toBurstPrice(String currencyCode) {
            return new BurstPrice(currencyCode, price, market_cap);
        }
    }
}
