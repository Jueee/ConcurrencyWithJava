package com.app.jueee.concurrency.chapter03.cache;

import java.util.concurrent.TimeUnit;

public class CleanCacheTask implements Runnable{

    private ParallelCache cache;

    public CleanCacheTask(ParallelCache cache) {
        this.cache = cache;
    }

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(10);
                cache.cleanCache();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
