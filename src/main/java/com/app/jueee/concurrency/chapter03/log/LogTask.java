package com.app.jueee.concurrency.chapter03.log;

import java.util.concurrent.TimeUnit;

/**
 * Task that writes all the messages in the log file every ten seconds.
 * It implements the Runnable interface. It will be executed as a thread
 * @author author
 *
 */
public class LogTask implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(10);
                Logger.writeLogs();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
