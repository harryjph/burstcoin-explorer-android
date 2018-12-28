package com.harrysoft.burstcoinexplorer.main.service;

import burst.kit.util.SchedulerAssigner;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AndroidSchedulerAssigner implements SchedulerAssigner {
    @Override
    public <T> Single<T> assignSchedulers(Single<T> source) {
        return source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
