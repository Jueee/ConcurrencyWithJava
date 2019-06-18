package com.app.jueee.concurrency.chapter12;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class J12MonitorLock {

    public static void main(String[] args) {

        MyLock lock = new MyLock();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executor.execute(new CommonLockTask(lock));
        }

        for (int i = 0; i < 100; i++) {
            System.out.println("************************\n");
            System.out.println("Owner : " + lock.getOwnerName());
            System.out.println("Queued Threads: " + lock.hasQueuedThreads());
            if (lock.hasQueuedThreads()) {
                System.out.println("Queue Length: " + lock.getQueueLength());
                System.out.println("Queued Threads: ");
                Collection<Thread> lockedThreads = lock.getThreads();
                for (Thread lockedThread : lockedThreads) {
                    System.out.println(lockedThread.getName());
                }
            }
            System.out.println("Fairness: " + lock.isFair());
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Holds: "+lock.getHoldCount());
            System.out.println("************************\n");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        
        System.out.println("************************\n");
        System.out.println("Owner : " + lock.getOwnerName());
        System.out.println("Queued Threads: " + lock.hasQueuedThreads());
        if (lock.hasQueuedThreads()) {
            System.out.println("Queue Length: " + lock.getQueueLength());
            System.out.println("Queued Threads: ");
            Collection<Thread> lockedThreads = lock.getThreads();
            for (Thread lockedThread : lockedThreads) {
                System.out.println(lockedThread.getName());
            }
        }
        System.out.println("Fairness: " + lock.isFair());
        System.out.println("Locked: " + lock.isLocked());
        System.out.println("Holds: "+lock.getHoldCount());      
        System.out.println("************************\n");       

    }
}

class CommonLockTask implements Runnable {

    private Lock lock;
    
    public CommonLockTask(Lock lock) {
        this.lock=lock;
    }
    
    @Override
    public void run() {

        for (int i = 0; i < 3; i++) {
            lock.lock();
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s-%s: Working %d seconds\n", new Date(), Thread.currentThread().getName(), duration);
            try {
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }
    }

}


class MyLock extends ReentrantLock {

    private static final long serialVersionUID = 8025713657321635686L;

    public String getOwnerName() {
        if (this.getOwner() == null) {
            return "None";
        }
        return this.getOwner().getName();
    }

    public Collection<Thread> getThreads() {
        return this.getQueuedThreads();
    }
}