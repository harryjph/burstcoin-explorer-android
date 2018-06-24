package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.BurstPrice;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;

import io.reactivex.Single;

public final class CMCPriceService implements BurstPriceService {

    private static final String BURST_PRICE_ENDPOINT = "https://api.coinmarketcap.com/v2/ticker/573/?convert=";

    private final ObjectService objectService;

    CMCPriceService(ObjectService objectService) {
        this.objectService = objectService;
    }

    @NotNull
    @Override
    public Single<BurstPrice> fetchPrice(@NotNull String currencyCode) {
        return objectService.fetchObject(BURST_PRICE_ENDPOINT + currencyCode, CMCPriceResult.class)
                .map(cmcPriceResult -> cmcPriceResult.data.quotes.get(currencyCode.toUpperCase()).toBurstPrice(currencyCode.toUpperCase()));
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
