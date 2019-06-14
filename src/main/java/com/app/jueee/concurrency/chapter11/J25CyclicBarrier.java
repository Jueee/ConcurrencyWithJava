package com.app.jueee.concurrency.chapter11;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class J25CyclicBarrier {

    // 当所有的任务都到达调用 await() 方法的公共点时，将执行 FinishBarrierTask ， 然后所有的任务都继续其执行过程。
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(10, new FinishBarrierTask());
        
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        
        for (int i = 0; i < 10; i++) {
            executor.execute(new BarrierTask(barrier));
        }
        
        executor.shutdown();
        
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class BarrierTask implements Runnable{
    
    private CyclicBarrier barrier;
    
    public BarrierTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+": Phase 1");
        CommonTask.doTask();
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+": Phase 2");
    }
}

// 当所有的任务都执行了 await() 方法之后，它将被 CyclicBarrier 执行。
class FinishBarrierTask implements Runnable{
    
    @Override
    public void run() {
        System.out.println("FinishBarrierTask: All the tasks have finished");
    }
}