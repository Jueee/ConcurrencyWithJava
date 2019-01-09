package com.app.jueee.concurrency.chapter01;

import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 *  多元复用模式
 *  
 *  规定数目的任务可以同时执行临界段。  
 *	
 *	@author hzweiyongqiang
 */
public class J5Multiplex {

    static Semaphore semaphoreObject = new Semaphore(2);

    private static class ThreadMultiplex extends Thread{
        
        @Override
        public void run() {
            preCriticalSection();
            try {
                semaphoreObject.acquire(); // 临界段开始
                criticalSection();
                System.out.println("[Thread]"+currentThread().getName()+"\tstart sleep....");
                Thread.sleep(10000);
                System.out.println("[Thread]"+currentThread().getName()+"\tend sleep....");
            } catch (Exception e) {
            } finally {
                semaphoreObject.release(); // 临界段结束
                postCriticalSection();
            }
        }
        
        public void preCriticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t--preCriticalSection--");
        }
        
        public void criticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\t--criticalSection--");
        }
        
        public void postCriticalSection() {
            System.out.println("[Thread]"+currentThread().getName()+"\t--postCriticalSection--");
        }
    }
    
    
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new ThreadMultiplex();
            thread.setName("Thread-"+i);
            thread.start();
        }
    }
}
