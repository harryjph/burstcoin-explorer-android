package com.harrysoft.burstcoinexplorer.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.harry1453.burst.explorer.entity.SearchResult;
import com.harry1453.burst.explorer.service.BurstBlockchainService;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.util.NfcUtils;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final BurstBlockchainService burstBlockchainService;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<SearchResult> searchResult = new MutableLiveData<>();

    private Intent lastNfcIntent;

    MainActivityViewModel(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    public void search(String query) {
        compositeDisposable.add(burstBlockchainService.determineSearchRequestType(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResult::postValue, t -> {}));
    }

    public boolean handleNfcIntent(Context context, Intent intent) {
        if (Objects.equals(intent, lastNfcIntent)) return false;

        lastNfcIntent = intent;

        try {
            NfcUtils.processNfcIntent(context, intent);
            return true;
        } catch (Throwable t) {
            Toast.makeText(context, R.string.invalid_data, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<SearchResult> getSearchResult() { return searchResult; }
}
