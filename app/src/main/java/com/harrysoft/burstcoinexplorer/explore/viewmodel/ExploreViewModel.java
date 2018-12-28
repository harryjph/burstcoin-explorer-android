package com.harrysoft.burstcoinexplorer.explore.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.widget.SwipeRefreshLayout;

import com.harry1453.burst.explorer.entity.BurstPrice;
import com.harry1453.burst.explorer.repository.ConfigRepository;
import com.harry1453.burst.explorer.service.BurstPriceService;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.util.CurrencyUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import burst.kit.entity.response.BlockResponse;
import burst.kit.entity.response.BlocksResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExploreViewModel extends AndroidViewModel implements SwipeRefreshLayout.OnRefreshListener {

    private final BurstNodeService burstNodeService;
    private final BurstPriceService burstPriceService;
    private final ConfigRepository configRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> refreshing = new MutableLiveData<>();
    private final MutableLiveData<List<BlockResponse>> recentBlocks = new MutableLiveData<>();
    private final MutableLiveData<String> priceFiat = new MutableLiveData<>();
    private final MutableLiveData<String> priceBtc = new MutableLiveData<>();
    private final MutableLiveData<String> marketCapital = new MutableLiveData<>();
    private final MutableLiveData<String> blockHeight = new MutableLiveData<>();
    private final MutableLiveData<String> recentBlocksLabel = new MutableLiveData<>();

    private String lastCurrencyCode = "";

    ExploreViewModel(Application application, BurstNodeService burstNodeService, BurstPriceService burstPriceService, ConfigRepository configRepository) {
        super(application);
        this.burstNodeService = burstNodeService;
        this.burstPriceService = burstPriceService;
        this.configRepository = configRepository;

        priceFiat.postValue(application.getString(R.string.price_fiat, application.getString(R.string.loading)));

        refreshing.postValue(true);
        onRefresh();
    }

    private void getData() {
        compositeDisposable.add(burstNodeService.getBlocks(0, 99)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRecentBlocks, t -> onRecentBlocksError()));
    }

    private void getPrice() {
        compositeDisposable.add(burstPriceService.fetchPrice(configRepository.getSelectedCurrency())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrice, t -> onPriceError()));

        compositeDisposable.add(burstPriceService.fetchPrice("BTC")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPrice, t -> onPriceError()));
    }

    private void onRecentBlocksError() {
        refreshing.postValue(false);
        blockHeight.postValue(getApplication().getString(R.string.loading_error));
        recentBlocksLabel.postValue(getApplication().getString(R.string.recent_blocks_error));
    }

    private void onPriceError() {
        priceFiat.postValue(getApplication().getString(R.string.loading_error));
        priceBtc.postValue(getApplication().getString(R.string.loading_error));
        marketCapital.postValue(getApplication().getString(R.string.loading_error));
    }

    private void onPrice(BurstPrice burstPrice) {
        if (burstPrice.currencyCode.equals("BTC")) {
            priceBtc.postValue(getApplication().getString(R.string.basic_data, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.price, true)));
        } else {
            priceFiat.postValue(getApplication().getString(R.string.price_fiat, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.price, true)));
            marketCapital.postValue(getApplication().getString(R.string.basic_data, CurrencyUtils.formatCurrencyAmount(burstPrice.currencyCode, burstPrice.marketCapital, false)));
        }
    }

    private void onRecentBlocks(BlocksResponse blocks) {
        refreshing.postValue(false);
        recentBlocks.postValue(Arrays.asList(blocks.getBlocks()));
        recentBlocksLabel.postValue(getApplication().getString(R.string.recent_blocks));
        blockHeight.postValue(String.format(Locale.getDefault(), "%d", blocks.getBlocks()[0].getHeight()));
    }

    public void checkForCurrencyChange() {
        if (!Objects.equals(configRepository.getSelectedCurrency(), lastCurrencyCode)) {
            lastCurrencyCode = configRepository.getSelectedCurrency();
            getPrice();
        }
    }

    @Override
    public void onRefresh() {
        getData();
        getPrice();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<Boolean> getRefreshing() { return refreshing; }
    public LiveData<List<BlockResponse>> getRecentBlocks() { return recentBlocks; }
    public LiveData<String> getPriceFiat() { return priceFiat; }
    public LiveData<String> getPriceBtc() { return priceBtc; }
    public LiveData<String> getMarketCapital() { return marketCapital; }
    public LiveData<String> getBlockHeight() { return blockHeight; }
    public LiveData<String> getRecentBlocksLabel() { return recentBlocksLabel; }
}
