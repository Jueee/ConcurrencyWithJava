package com.app.jueee.concurrency.chapter11;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CommonTask {

    // 在随机的一段时间（0 到 10 秒）内将调用线程休眠。
    public static void doTask() {
        long duration = ThreadLocalRandom.current().nextLong(10);
        System.out.printf("%s-%s: Working %d seconds\n", new Date(), Thread.currentThread().getName(), duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
