package com.app.jueee.concurrency.chapter01;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 *  栅栏模式
 *	
 *  每个任务都必须等到所有任务都到达同步点后才能继续执行。  
 *  
 *	@author hzweiyongqiang
 */
public class J5Barrier {

    static CyclicBarrier barrierObject = new CyclicBarrier(2);

    private static class ThreadBarrier extends Thread{
        
        @Override
        public void run() {
            try {
                preSyncPoint();
                barrierObject.await();
                postSyncPoint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void preSyncPoint() throws InterruptedException {
            System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\t--preSyncPoint--");
            Thread.sleep(10000);
            System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\t--stop sleep--");
        }
        
        public void postSyncPoint() {
            System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\t--postSyncPoint--");
        }
    }
    
    
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new ThreadBarrier();
            thread.setName("Thread-"+i);
            thread.start();
        }
    }
}
