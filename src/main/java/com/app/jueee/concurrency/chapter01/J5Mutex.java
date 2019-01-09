package com.app.jueee.concurrency.chapter01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 互斥模式
 *	
 *  一次只有一个任务可以执行由互斥机制保护的代码片段。  
 *	@author hzweiyongqiang
 */
public class J5Mutex {
    
    static Lock lockObject = new ReentrantLock();

    private static class ThreadMutex extends Thread{
        
        @Override
        public void run() {
            preCriticalSection();
            try {
                lockObject.lock(); // 临界段开始
                criticalSection();
                System.out.println("[Thread]"+currentThread().getName()+"\tstart sleep....");
                Thread.sleep(10000);
                System.out.println("[Thread]"+currentThread().getName()+"\tend sleep....");
            } catch (Exception e) {
            } finally {
                lockObject.unlock(); // 临界段结束
                postCriticalSection();
            }
        }
        
        public void preCriticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t--preCriticalSection--");
        }
        
        public void criticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t--criticalSection--");
        }
        
        public void postCriticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t--postCriticalSection--");
        }
    }
    
    
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new ThreadMutex();
            thread.setName("Thread-"+i);
            thread.start();
        }
    }
}
