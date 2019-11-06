package com.app.flikrsearchdemo.executors;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Your name on 2019-11-05.
 */
public class BackgroundExecutor implements AppTaskExecutor {

    @Inject
    BackgroundExecutor() {}

    @Override
    public Scheduler getScheduler() {
        return Schedulers.io();
    }
}
