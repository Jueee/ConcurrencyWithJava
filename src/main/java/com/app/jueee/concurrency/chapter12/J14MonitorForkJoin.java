package com.app.jueee.concurrency.chapter12;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class J14MonitorForkJoin {

    public static void main(String[] args) {

        CommonForkTask task=new CommonForkTask(0, 10000);
        ForkJoinPool pool=new ForkJoinPool(2);
        pool.execute(task);
        
        while (!task.isDone()) {
            System.out.println("**********************");
            System.out.println("Parallelism: "+pool.getParallelism());
            System.out.println("Pool Size: "+pool.getPoolSize());
            System.out.println("Active Thread Count: "+pool.getActiveThreadCount());
            System.out.println("Running Thread Count: "+pool.getRunningThreadCount());
            System.out.println("Queued Submission: "+pool.getQueuedSubmissionCount());
            System.out.println("Queued Tasks: "+pool.getQueuedTaskCount());
            System.out.println("Queued Submissions: "+pool.hasQueuedSubmissions());
            System.out.println("Steal Count: "+pool.getStealCount());
            System.out.println("Terminated : "+pool.isTerminated());
            System.out.println("**********************");
            
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
    }
}

class CommonForkTask extends RecursiveAction {
    private static final long serialVersionUID = -2313055686828957266L;
    private int start;
    private int end;

    public CommonForkTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if ((end - start) > 100) {
            int mid=start+((end-start)/2);
            CommonForkTask task1=new CommonForkTask(start, mid);
            CommonForkTask task2=new CommonForkTask(mid, end);
            
            task1.fork();
            task2.fork();
            
            task1.join();
            task2.join();
        } else {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s-%s: Working %d seconds\n", new Date(), Thread.currentThread().getName(), duration);
            try {
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

