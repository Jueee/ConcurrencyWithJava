package com.app.jueee.concurrency.chapter07;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class J1ForkJoinDemo extends RecursiveTask<Integer>{
    
    public static final int threshold = 2;
    private int start;
    private int end;
    
    public J1ForkJoinDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) <= threshold;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 如果任务大于阈值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
            J1ForkJoinDemo demo1 = new J1ForkJoinDemo(start, middle);
            J1ForkJoinDemo demo2 = new J1ForkJoinDemo(middle + 1, end);
            
            demo1.fork();
            demo2.fork();
            
            int result1 = demo1.join();
            int result2 = demo2.join();
            sum = result1 + result2;
        }
        return sum;
    }

    
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        
        J1ForkJoinDemo demo = new J1ForkJoinDemo(1, 100);
        Future<Integer> result = forkJoinPool.submit(demo);
        try {
            System.out.println(result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
