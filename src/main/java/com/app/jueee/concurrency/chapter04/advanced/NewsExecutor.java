package com.app.jueee.concurrency.chapter04.advanced;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *  扩展ScheduledThreadPoolExecutor 类。
 *  
 *  重载了 decorateTask() 方法，有了该方法，就可以替换预定执行器使用的内部任务。
 *	
 *	@author hzweiyongqiang
 */
public class NewsExecutor extends ScheduledThreadPoolExecutor{

    public NewsExecutor(int corePoolSize) {
        super(corePoolSize);
    }
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        ExecutorTask<V> myTask = new ExecutorTask<V>(runnable, null, task, this);
        return myTask;
    }

}
