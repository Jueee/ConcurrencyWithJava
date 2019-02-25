package com.app.jueee.concurrency.chapter04.advanced;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter04.basic.NewsTask;

/**
 * 实现执行器的内部任务。

 * 必须实现一个类用以扩展 FutureTask 类，而且因为将在预定的执行器中执行这些任务。  
 * 必须实现 RunnableScheduledFuture 接口。该接口提供了 getDelay() 方法，该方法返回了距离任务下一次执行所剩余的时间。1
 * @author hzweiyongqiang
 */
public class ExecutorTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
    
    // 由 ScheduledThreadPoolExecutor 类创建的初始内部任务
    private RunnableScheduledFuture<V> task;
    // 执行该任务的预定执行器
    private NewsExecutor executor;
    // 该任务下一次执行的起始时间
    private long startDate;
    // RSS 订阅的名称
    private String name;

    public ExecutorTask(Runnable runnable, V result, RunnableScheduledFuture<V> task, NewsExecutor executor) {
        super(runnable, result);
        this.task = task;
        this.executor = executor;
        this.name = ((NewsTask)runnable).getName();
        this.startDate = new Date().getTime();
    }

    // 该方法在给定的时间单位内返回距离任务下次执行的剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        long delay;
        if (!isPeriodic()) {
            delay = task.getDelay(unit);
        } else {
            if (startDate == 0) {
                delay = task.getDelay(unit);
            } else {
                Date now = new Date();
                delay = startDate - now.getTime();
                delay = unit.convert(delay, TimeUnit.MILLISECONDS);
            }
        }
        return delay;
    }

    // 对比两个任务
    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getStartDate(), ((ExecutorTask<V>)o).getStartDate());
    }

    // 如果任务是周期性的，则 isPeriodic() 方法返回 true ，否则返回 false 
    @Override
    public boolean isPeriodic() {
        return task.isPeriodic();
    }
    
    // 在 run() 方法中实现了本例最重要的部分。
    // 首先，调用 FutureTask 类的 runAndReset() 方法。该方法执行任务并且重置其状态，这样任务就可以再次执行。
    // 然后，使用 Timer 类计算下次执行的起始时间。
    // 最后，还要在 ScheduledThreadPoolExecutor 类的队列中再次插入该任务。
    // 如果不做最后一步，那么任务就不会像如下所示这样再次执行。
    @Override
    public void run() {
        if (isPeriodic() && (!executor.isShutdown())) {
            super.runAndReset();
            Date now = new Date();
            startDate = now.getTime() + Timer.getPeriod();
            executor.getQueue().add(this);
            System.out.println("Start Date:" + new Date(startDate));
        }
    }

    public RunnableScheduledFuture<V> getTask() {
        return task;
    }

    public void setTask(RunnableScheduledFuture<V> task) {
        this.task = task;
    }

    public NewsExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(NewsExecutor executor) {
        this.executor = executor;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
