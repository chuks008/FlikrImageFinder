package com.app.flikrsearchdemo.executors;

import io.reactivex.Scheduler;

/**
 * Created by Your name on 2019-11-05.
 */
public interface AppTaskExecutor {
    Scheduler getScheduler();
}
