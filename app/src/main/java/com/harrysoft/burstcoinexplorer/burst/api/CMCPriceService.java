package com.harrysoft.burstcoinexplorer.burst.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstPrice;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import io.reactivex.Single;

public class CMCPriceService implements BurstPriceService {

    private static final String burstPriceEndpoint = "https://api.coinmarketcap.com/v1/ticker/burst/";

    private final RequestQueue requestQueue;
    private final Gson gson;

    public CMCPriceService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder()
                .registerTypeAdapter(BurstPrice.class, new BurstPriceDeserializer())
                .create();
    }

    @Override
    public Single<BurstPrice> fetchPrice() {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, burstPriceEndpoint, response -> {
                if (response != null) {
                    e.onSuccess(gson.fromJson(response, BurstPrice[].class)[0]);
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    private class BurstPriceDeserializer implements JsonDeserializer<BurstPrice> {

        @Override
        public BurstPrice deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("price_usd");
            BigDecimal priceUSD = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            element = jsonObj.get("price_btc");
            BigDecimal priceBTC = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            element = jsonObj.get("market_cap_usd");
            BigDecimal marketCapUSD = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            return new BurstPrice(priceUSD, priceBTC, marketCapUSD);
        }
    }
}
