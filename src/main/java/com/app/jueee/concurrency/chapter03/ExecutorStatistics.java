package com.app.jueee.concurrency.chapter03;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class that stores the statistics of a user: Number of tasks that he has executed and the total execution time
 * it has comsumed
 * @author author
 *
 */
public class ExecutorStatistics {
	
	/**
	 * Execution time
	 */
	private AtomicLong executionTime = new AtomicLong(0);
	
	/**
	 * Number of tasks
	 */
	private AtomicInteger numTasks = new AtomicInteger(0);
	
	/**
	 * Method that adds the execution time
	 * @param time Amount to time to add
	 */
	public void addExecutionTime(long time) {
		executionTime.addAndGet(time);
	}
	
	/**
	 * Method that returns the execution time
	 * @return The execution time
	 */
	public long getExecutionTime() {
		return executionTime.get();
	}
	
	/**
	 * Method that adds a task to the counter
	 */
	public void addTask() {
		numTasks.incrementAndGet();
	}

	
	/**
	 * Method that returns the number of tasks executed by this user
	 * @return The number of tasks
	 */
	public int getNumTasks() {
		return numTasks.get();
	}

	/**
	 * Method that returns a String version of this object
	 */
	@Override
	public String toString() {
		return "Executed Tasks: "+getNumTasks()+". Execution Time: "+getExecutionTime();
	
	}
	
	

}
