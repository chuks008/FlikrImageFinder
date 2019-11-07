package com.app.flikrsearchdemo.executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Your name on 2019-11-05.
 */
@Singleton
public class BackgroundExecutor implements AppTaskExecutor {

    @Inject
    BackgroundExecutor() {
    }

    @Override
    public Scheduler getScheduler() {
        return Schedulers.io();
    }
}
