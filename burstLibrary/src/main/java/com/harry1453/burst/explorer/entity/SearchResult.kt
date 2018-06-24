package com.harry1453.burst.explorer.entity

data class SearchResult (
        @JvmField val request: String,
        @JvmField val requestType: SearchRequestType
) {
    private var navigated: Boolean = false

    fun isNavigated(): Boolean {
        return navigated
    }

    fun onNavigated() {
        navigated = true
    }
}
