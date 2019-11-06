package com.app.flikrsearchdemo.executors;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Your name on 2019-11-05.
 */
public class PostTaskExecutor implements AppTaskExecutor {

    @Inject
    PostTaskExecutor() {}

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
