package com.app.jueee.concurrency.chapter01;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池模式
 */
public class J5ThreadPool {

    /**
     * ExecutorService产生的Thread命名
     */
    static class NamedThreadFactory implements ThreadFactory {
        private AtomicInteger tag = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("resumption调度器线程：" + tag.getAndIncrement());
            return thread;
        }

    }

    /**
     * newCachedThreadPool
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     */
    public static void newCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new NamedThreadFactory());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "\t" + index);
                }
            });
        }
    }

    /**
     * newFixedThreadPool
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     */
    public static void newFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3, new NamedThreadFactory());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "\t" + index);
                }
            });
        }
    }

    /**
     * newScheduledThreadPool
     * 创建一个定长线程池，支持定时及周期性任务执行。
     */
    public static void newScheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        System.out.println(new Date() + "\t" + "start");
        // 表示延迟3秒执行
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date() + "\t" + "delay 3 seconds");
            }
        }, 3, TimeUnit.SECONDS);

        // 表示延迟1秒后每3秒执行一次
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date() + "\t" + "delay 1 seconds, and excute every 3 seconds");
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    /**
     * newSingleThreadExecutor
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */
    public static void newSingleThreadExecutor() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "\t" + index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void testThreadPool(int num) {
        switch (num) {
            case 1:
                newCachedThreadPool();
                break;
            case 2:
                newFixedThreadPool();
                break;
            case 3:
                newScheduledThreadPool();
                break;
            case 4:
                newSingleThreadExecutor();
                break;
            default:
                System.out.println("--error--");
        }
    }

    public static void main(String[] args) {
        testThreadPool(4);
    }
}
