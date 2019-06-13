package com.app.jueee.concurrency.chapter11;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class J23Semaphore {

    // 在主程序中执行了 10 个任务，它们共享一个 Semaphore 类。
    // 该类使用两个共享资源初始化，这样就可以同时运行两个任务。
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        
        for (int i = 0; i < 10; i++) {
            executor.execute(new SemaphoreTask(semaphore));
        }
        
        executor.shutdown();
        
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SemaphoreTask implements Runnable{
    
    private Semaphore semaphore;
    
    public SemaphoreTask(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        try {
            semaphore.acquire();
            CommonTask.doTask();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}