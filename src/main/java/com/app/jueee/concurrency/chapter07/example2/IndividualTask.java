package com.app.jueee.concurrency.chapter07.example2;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import com.app.jueee.concurrency.chapter07.common2.CensusData;
import com.app.jueee.concurrency.chapter07.common2.Filter;
import com.app.jueee.concurrency.chapter07.common2.FilterData;
import com.app.jueee.concurrency.chapter07.example2.TaskManager;

public class IndividualTask extends RecursiveTask<CensusData>{
    // 含有所有 CensusData 对象的数组
    private CensusData[] data;
    // Start 属性和 end 属性，它们确定了要处理的元素
    private int start, end;
    // 确定了在无须分割任务的前提下所处理的最大元素数
    private int size;
    // 用于在必要之时撤销任务
    private TaskManager manager;
    private List<FilterData> filters;

    public IndividualTask(CensusData[] data, int start, int end, TaskManager manager, int size,
        List<FilterData> filters) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.manager = manager;
        this.size = size;
        this.filters = filters;
    }
    
    /**
     *  如果任务需要处理的元素数比 size 属性值小，该方法直接进行对象查找。
     *  如果该方法找到了想要的对象，那么它将返回该对象并且使用 cancelTasks() 方法撤销剩余任务的执行。
     *  如果该方法没有找到想要的对象，那么它将返回 null 值
     */
    @Override
    protected CensusData compute() {
        if (end - start <= size) {
            for (int i = start; i < end&& !Thread.currentThread().isInterrupted(); i++ ) {
                CensusData censusData = data[i];
                if (Filter.filter(censusData, filters)) {
                    System.out.println("Found:" + i);
                    manager.cancelTasks(this);
                    return censusData;
                }
            }
            return null;
         
        // 如果要处理的项数要比 size 属性规定的多，那么要创建两个子任务来分别处理其中的一半元素。
        } else {
            int mid = (start+end)/2;
            IndividualTask task1 = new IndividualTask(data, start, mid, manager, size, filters);
            IndividualTask task2 = new IndividualTask(data, mid, end, manager, size, filters);
            
            // 向任务管理器添加新创建的任务，并且删除实际任务
            manager.addTask(task1);
            manager.addTask(task2);
            manager.deleteTask(this);
            // 使用 fork() 方法以异步方式将任务发送给 ForkJoinPool ，并且使用 quietlyJoin() 方法等待其执行结束。
            task1.fork();
            task2.fork();
            task1.quietlyJoin();
            task2.quietlyJoin();
            // 从 TaskManager 类中删除子任务
            manager.deleteTask(task1);
            manager.deleteTask(task2);
            
            try {
                CensusData res = task1.join();
                if (res != null) {
                    return res;
                }
                manager.deleteTask(task1);
            } catch (Exception e) {}
            
            try {
                CensusData res = task2.join();
                if (res != null) {
                    return res;
                }
                manager.deleteTask(task2);
            } catch (Exception e) {}
            
            return null;
        }
    }
}
