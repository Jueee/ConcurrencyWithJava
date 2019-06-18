package com.app.jueee.concurrency.chapter12;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommonThreadTask implements Runnable {
    
    @Override
    public void run() {
        long duration=(long)(Math.random()*10);
        System.out.printf("%s-%s: Working %d seconds\n",new Date(),Thread.currentThread().getName(),duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}