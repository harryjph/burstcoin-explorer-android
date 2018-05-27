package com.harrysoft.burstcoinexplorer.burst.api;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.harrysoft.burstcoinexplorer.burst.entity.EntityDoesNotExistException;
import com.harrysoft.burstcoinexplorer.burst.entity.EventInfo;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;

import io.reactivex.Single;

public class RepoInfoService implements BurstInfoService {

    private static final String repoUrl = "https://harry1453.github.io/burstcoin-explorer-android/";
    private static final String eventsInfoPage = "events.html";
    private static final String mapPage = "map.html";

    private final RequestQueue requestQueue;
    private final Gson gson;

    public RepoInfoService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(EventInfo[].class, new EventInfosDeserializer())
                .create();
    }

    @Override
    public Single<EventInfo[]> getEvents() {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, repoUrl + eventsInfoPage, response -> {
                if (response != null) {
                    Log.e("Test", "response:" + response);
                    EventInfo[] eventInfos = gson.fromJson(response, EventsApiResponse.class).events;
                    if (eventInfos != null) {
                        e.onSuccess(eventInfos);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    public static Single<String> getNetworkMapDisplayURL(NetworkStatus networkStatus) {
        return Single.fromCallable(() -> {
            StringBuilder url = new StringBuilder(repoUrl + mapPage + "?");

            for (Map.Entry<String, BigInteger> country : networkStatus.peersActiveInCountry.entrySet()) {
                url.append(country.getKey()).append("=").append(country.getValue().toString()).append("&");
            }

            return url.toString();
        });
    }

    private static class EventInfoDeserializer implements JsonDeserializer<EventInfo> {

        @Override
        public EventInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("name");
            String name = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("infoPage");
            String infoPageRaw = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("blockHeight");
            String blockHeightRaw = element == null || element.isJsonNull() ? "" : element.getAsString();

            Uri infoPage = null;
            BigInteger blockHeight = null;

            if (!TextUtils.isEmpty(infoPageRaw)) {
                try {
                    infoPage = Uri.parse(infoPageRaw);
                } catch (Exception ignored) {}
            }

            if (!TextUtils.isEmpty(blockHeightRaw)) {
                try {
                    blockHeight = new BigInteger(blockHeightRaw);
                } catch (Exception ignored) {}
            }

            return new EventInfo(name, infoPage, blockHeight);
        }
    }

    private static class EventInfosDeserializer implements JsonDeserializer<EventInfo[]> {

        private final EventInfoDeserializer itemDeserializer = new EventInfoDeserializer();

        @Override
        public EventInfo[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArr = json.getAsJsonArray();
            int len = jsonArr.size();
            EventInfo[] eventInfos = new EventInfo[len];
            for (int i = 0; i < len; i++) {
                eventInfos[i] = itemDeserializer.deserialize(jsonArr.get(i).getAsJsonObject(), typeOfT, context);
            }
            return eventInfos;
        }
    }

    private class EventsApiResponse {
        EventInfo[] events;
    }
}
