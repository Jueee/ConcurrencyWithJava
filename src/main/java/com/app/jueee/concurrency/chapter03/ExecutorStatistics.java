package com.app.jueee.concurrency.chapter03;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 将计算每个用户在服务器上执行的任务数量，以及这些任务的总执行时间。
 *
 */
public class ExecutorStatistics {
	
	/**
	 * 支持在单个变量上进行原子操作的 AtomicVariables 型变量，可以在不同的线程中使用这些变量，而且不需要采用任何同步机制。
	 */
    // 任务的总执行时间
	private AtomicLong executionTime = new AtomicLong(0);
	// 执行的任务数量
	private AtomicInteger numTasks = new AtomicInteger(0);
	
	/**
	 * 增加执行时间
	 * @param 执行时间
	 */
	public void addExecutionTime(long time) {
		executionTime.addAndGet(time);
	}
	
	/**
	 * 获取执行时间
	 * @return 执行时间
	 */
	public long getExecutionTime() {
		return executionTime.get();
	}
	
	/**
	 * 增加任务数
	 */
	public void addTask() {
		numTasks.incrementAndGet();
	}

	
	/**
	 * 执行时间任务数
	 * @return 任务数
	 */
	public int getNumTasks() {
		return numTasks.get();
	}

	/**
	 * 重载了 toString() 方法以可读方式获取信息。
	 */
	@Override
	public String toString() {
		return "Executed Tasks: "+getNumTasks()+". Execution Time: "+getExecutionTime();
	
	}
	
	

}
