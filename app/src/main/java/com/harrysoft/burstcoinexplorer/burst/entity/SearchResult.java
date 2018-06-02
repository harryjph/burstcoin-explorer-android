package com.harrysoft.burstcoinexplorer.burst.entity;

public class SearchResult {
    public final String request;
    public final SearchRequestType requestType;

    private boolean navigated = false;

    public SearchResult(String request, SearchRequestType requestType) {
        this.request = request;
        this.requestType = requestType;
    }

    public boolean isNavigated() {
        return navigated;
    }

    public void onNavigated() {
        navigated = true;
    }
}
