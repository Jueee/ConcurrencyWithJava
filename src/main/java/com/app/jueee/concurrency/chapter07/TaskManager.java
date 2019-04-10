package com.app.jueee.concurrency.chapter07;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskManager {
    // 存放所有待撤销任务
    private Set<RecursiveTask> tasks;
    // 用于保证只有一个任务执行 cancelTasks() 方法
    private AtomicBoolean cancelled;

    public TaskManager() {
        tasks = ConcurrentHashMap.newKeySet();
        cancelled = new AtomicBoolean(false);
    }
    
    public void addTask(RecursiveTask task) {
        tasks.add(task);
    }
    
    public void cancelTasks(RecursiveTask sourceTask) {
        for(RecursiveTask task: tasks) {
            if (task != sourceTask) {
                if (cancelled.get()) {
                    task.cancel(true);
                } else {
                    tasks.add(task);
                }
            }
        }
    }
    
    public void deleteTask(RecursiveTask task) {
        tasks.remove(task);
    }
}
