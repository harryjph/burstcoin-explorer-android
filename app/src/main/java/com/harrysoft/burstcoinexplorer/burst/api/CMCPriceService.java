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

    private static final String burstPriceEndpoint = "https://api.coinmarketcap.com/v1/ticker/burst/?convert=";

    private final RequestQueue requestQueue;
    private final Gson gson;

    private final BurstPriceDeserializer burstPriceDeserializer;

    public CMCPriceService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        burstPriceDeserializer = new BurstPriceDeserializer();
        gson = new GsonBuilder()
                .registerTypeAdapter(BurstPrice.class, burstPriceDeserializer)
                .create();
    }

    @Override
    public Single<BurstPrice> fetchPrice(String currencyCode) {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, burstPriceEndpoint + currencyCode, response -> {
                if (response != null) {
                    e.onSuccess(gson.fromJson(response, BurstPrice[].class)[0]);
                }
            }, e::onError);
            burstPriceDeserializer.setCurrencyCode(currencyCode);
            requestQueue.add(request);
        });
    }

    private class BurstPriceDeserializer implements JsonDeserializer<BurstPrice> {

        private String currencyCode;

        @Override
        public BurstPrice deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("price_" + currencyCode.toLowerCase());
            BigDecimal fiatPrice = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            element = jsonObj.get("price_btc");
            BigDecimal priceBTC = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            element = jsonObj.get("market_cap_" + currencyCode.toLowerCase());
            BigDecimal marketCap = element == null || element.isJsonNull() ? BigDecimal.ZERO : element.getAsBigDecimal();

            return new BurstPrice(currencyCode, fiatPrice, priceBTC, marketCap);
        }

        void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }
}
