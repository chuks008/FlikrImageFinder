package com.app.flikrsearchdemo.executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Your name on 2019-11-05.
 */
@Singleton
public class PostTaskExecutor implements AppTaskExecutor {

    @Inject
    PostTaskExecutor() {}

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
