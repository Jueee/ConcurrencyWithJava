package com.app.jueee.concurrency.chapter07.example2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import com.app.jueee.concurrency.chapter07.common2.CensusData;
import com.app.jueee.concurrency.chapter07.common2.Filter;
import com.app.jueee.concurrency.chapter07.common2.FilterData;
import com.app.jueee.concurrency.chapter07.example2.TaskManager;

public class ListTask extends RecursiveTask<List<CensusData>> {

 // 含有所有 CensusData 对象的数组
    private CensusData[] data;
    // Start 属性和 end 属性，它们确定了要处理的元素
    private int start, end;
    // 确定了在无须分割任务的前提下所处理的最大元素数
    private int size;
    // 用于在必要之时撤销任务
    private TaskManager manager;
    private List<FilterData> filters;

    public ListTask(CensusData data[], int start, int end, TaskManager manager, int size, List<FilterData> filters) {
        this.data = data;
        this.start = start;
        this.end = end;
        this.size = size;
        this.filters = filters;
        this.manager = manager;
    }
    
    @Override
    protected List<CensusData> compute() {
        List<CensusData> ret = new ArrayList<>();
        List<CensusData> tmp;
        
        // 如果任务要处理的元素数量小于 size 属性，将满足筛选器指定标准的所有对象添加到结果列表中
        if (end - start <= size) {
            for (int i = start; i < end; i++) {
                CensusData censusData = data[i];
                if (Filter.filter(censusData, filters)) {
                    ret.add(censusData);
                }
            }
        // 如果要处理的项数多于 size 属性，将创建两个子任务来处理其中各一半的元素。
        } else {
            int mid = (start + end) / 2;
            ListTask task1 = new ListTask(data, start, mid, manager, size, filters);
            ListTask task2 = new ListTask(data, mid, end, manager, size, filters);
            // 将新创建的任务添加到任务管理器并且删除原来的实际任务
            manager.addTask(task1);
            manager.addTask(task2);
            manager.deleteTask(this);
            // 使用 fork() 方法以异步方式将任务发送给 ForkJoinPool ，并且使用 quietlyJoin() 方法等待其执行结束
            task1.fork();
            task2.fork();
            task2.quietlyJoin();
            task1.quietlyJoin();
            // 从 TaskManager 中删除子任务
            manager.deleteTask(task1);
            manager.deleteTask(task2);

            
            try {
                tmp = task1.join();
                if (tmp != null) {
                    ret.addAll(tmp);
                }
                manager.deleteTask(task1);
            } catch (Exception e) {}
            
            try {
                tmp = task2.join();
                if (tmp != null) {
                    ret.addAll(tmp);
                }
                manager.deleteTask(task2);
            } catch (Exception e) {}
        }
        return ret;
    }

}
