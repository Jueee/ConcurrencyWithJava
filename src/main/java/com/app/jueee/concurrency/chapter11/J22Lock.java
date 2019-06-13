package com.app.jueee.concurrency.chapter11;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class J22Lock {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(new LockTask("Task " + i));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class LockTask implements Runnable {

    private static ReentrantLock lock = new ReentrantLock();
    private String name;

    public LockTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // 使用 lock() 方法获得了一个锁
            lock.lock();
            System.out.println("Task: " + name + "; Date: " + new Date() + ": Running the task");
            CommonTask.doTask();
            System.out.println("Task: " + name + "; Date: " + new Date() + ": The execution has finished");
        } finally {
            // 使用 unlock() 方法释放了该锁
            lock.unlock();
        }
    }

}