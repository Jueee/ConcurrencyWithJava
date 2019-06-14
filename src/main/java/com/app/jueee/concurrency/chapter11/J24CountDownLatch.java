package com.app.jueee.concurrency.chapter11;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class J24CountDownLatch {

    // CountDownLatch 提供了一种等待一个或多个并发任务完成的机制。
    // 它有一个内部计数器，必须使用要等待的任务数初始化。
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        
        System.out.println("Main: Launching tasks");
        for (int i = 0; i < 10; i++) {
            executor.execute(new CountDownTask(countDownLatch));
        }
        
        // await() 方法休眠调用线程，直到内部计数器为 0，等待任务完成
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Main: Tasks finished at : "+new Date());
        
        executor.shutdown();
    }
    
}

class CountDownTask implements Runnable{
    
    private CountDownLatch countDownLatch;
    
    public CountDownTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    
    // 使用 countDown() 方法对 CountDownLatch 对象的内部计数器做递减操作。
    @Override
    public void run() {
        CommonTask.doTask();
        countDownLatch.countDown();
    }
}