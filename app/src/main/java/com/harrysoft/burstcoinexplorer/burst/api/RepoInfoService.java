package com.harrysoft.burstcoinexplorer.burst.api;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

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
import com.harrysoft.burstcoinexplorer.burst.entity.ForkInfo;

import java.lang.reflect.Type;
import java.math.BigInteger;

import io.reactivex.Single;

public class RepoInfoService implements BurstInfoService {

    private static final String repoUrl = "https://harry1453.github.io/burstcoin-explorer-android/";
    private static final String forksInfoPage = "forks.html";

    private final RequestQueue requestQueue;
    private final Gson gson;

    public RepoInfoService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ForkInfo[].class, new ForkInfosDeserializer())
                .create();
    }

    @Override
    public Single<ForkInfo[]> getForks() {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, repoUrl + forksInfoPage, response -> {
                if (response != null) {
                    ForkInfo[] forkInfos = gson.fromJson(response, ForksApiResponse.class).forks;
                    if (forkInfos != null) {
                        e.onSuccess(forkInfos);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    private static class ForkInfoDeserializer implements JsonDeserializer<ForkInfo> {

        @Override
        public ForkInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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

            return new ForkInfo(name, infoPage, blockHeight);
        }
    }

    private static class ForkInfosDeserializer implements JsonDeserializer<ForkInfo[]> {

        private final ForkInfoDeserializer itemDeserializer = new ForkInfoDeserializer();

        @Override
        public ForkInfo[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArr = json.getAsJsonArray();
            int len = jsonArr.size();
            ForkInfo[] forkInfos = new ForkInfo[len];
            for (int i = 0; i < len; i++) {
                forkInfos[i] = itemDeserializer.deserialize(jsonArr.get(i).getAsJsonObject(), typeOfT, context);
            }
            return forkInfos;
        }
    }

    private class ForksApiResponse {
        ForkInfo[] forks;
    }
}
