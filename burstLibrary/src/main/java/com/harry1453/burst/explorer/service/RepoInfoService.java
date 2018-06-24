package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.EventInfo;
import com.harry1453.burst.explorer.entity.NetworkStatus;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public final class RepoInfoService implements BurstInfoService {

    private static final String REPO_URL = "https://harry1453.github.io/burstcoin-explorer-android/";
    private static final String EVENTS_INFO_PAGE = "events.html";
    private static final String MAP_PAGE = "map.html";

    private final ObjectService objectService;

    RepoInfoService(ObjectService objectService) {
        this.objectService = objectService;
    }

    @NotNull
    @Override
    public Single<List<EventInfo>> getEvents() {
        return objectService.fetchObject(REPO_URL + EVENTS_INFO_PAGE, EventsApiResponse.class)
                .flattenAsObservable(eventsApiResponse -> eventsApiResponse.events)
                .map(EventResponse::toEventInfo)
                .toList();
    }

    public static Single<String> getNetworkMapDisplayURL(NetworkStatus networkStatus) {
        return Single.fromCallable(() -> {
            StringBuilder url = new StringBuilder(REPO_URL + MAP_PAGE + "?");

            for (Map.Entry<String, BigInteger> country : networkStatus.peersActiveInCountry.entrySet()) {
                url.append(country.getKey()).append("=").append(country.getValue().toString()).append("&");
            }

            return url.toString();
        });
    }

    private class EventsApiResponse {
        List<EventResponse> events;
    }

    private class EventResponse {
        String name;
        String infoPage;
        String blockHeight;

        EventInfo toEventInfo() {
            BigInteger blockHeightBigInteger = null;

            if (blockHeight != null) {
                try {
                    blockHeightBigInteger = new BigInteger(blockHeight);
                } catch (Exception ignored) {}
            }

            return new EventInfo(name, infoPage, blockHeightBigInteger);
        }
    }
}
