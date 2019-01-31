package com.app.jueee.concurrency.chapter03;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;
import com.app.jueee.concurrency.chapter03.log.Logger;

/**
 * This class implements the Executor that will execute the server tasks. It extends the ThreadPoolExecutor
 * @author author
 *
 */
public class ServerExecutor extends ThreadPoolExecutor {

	/**
	 * Hashmap to store the start times of the tasks and calculate the CPU time used per user
	 */
	private ConcurrentHashMap<Runnable, Date> startTimes;

	/**
	 * Hashmap to store the execution statistics per user
	 */
	private ConcurrentHashMap<String, ExecutorStatistics> executionStatistics;
	

	/**
	 * Core pool size of the executor. Initialize with the available processors
	 */
	private static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * Maximum pool size of the executor. Initialize with the available processors
	 */
	private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * Time the threads of the executor can be idle
	 */
	private static long KEEP_ALIVE_TIME = 10;
	
	/**
	 * Controller for the rejected tasks
	 */
	private static RejectedTaskController REJECTED_TASK_CONTROLLER = new RejectedTaskController();
	
	
	/**
	 * Constructor of the class
	 */
	public ServerExecutor() {
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new PriorityBlockingQueue<>(),REJECTED_TASK_CONTROLLER);
		
		startTimes = new ConcurrentHashMap<>();
		executionStatistics = new ConcurrentHashMap<String, ExecutorStatistics>();
	}

	/**
	 * This method will be executed after the execution of every task in the executor. We calculate the
	 * execution time of the tasks and adds it to the statistics of the user
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		ServerTask<?> task=(ServerTask<?>)r;
		ConcurrentCommand command=task.getCommand();
		
		if (t==null)  {
			if (!task.isCancelled()) {
				Date startDate=startTimes.remove(r);
				Date endDate=new Date();
				long executionTime=endDate.getTime()-startDate.getTime();
				ExecutorStatistics statistics = executionStatistics.computeIfAbsent(command.getUsername(), n -> new ExecutorStatistics());
				statistics.addExecutionTime(executionTime);
				statistics.addTask();
				J3ConcurrentVersionServer.finishTask(command.getUsername(), command);
			}
			else {
				String message="The task "+command.hashCode()
					+" of user "+command.getUsername()
					+" has been cancelled.";
				Logger.sendMessage(message);				
			}
				
		} else {
			String message="The exception "
					+t.getMessage()
					+" has been thrown.";
			Logger.sendMessage(message);
		}
	}

	/**
	 * This method will be executed before the execution of every task in the executor. We store
	 * the start date of the task to calculate its execution time
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		startTimes.put(r, new Date());
	}

	/**
	 * This method is executed to create the Task object that will execute a Runnable
	 * (our commands in this case) in the executor. We override the default task to
	 * store the command on it and have access to its data in the afterExecute() method
	 */
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		return new ServerTask<T>((ConcurrentCommand)runnable);
	}


	/**
	 * Method that writes the execution statistics per user to the Logger system
	 */
	public void writeStatistics() {
		for(Entry<String, ExecutorStatistics> entry: executionStatistics.entrySet()) {
			 String user = entry.getKey();
			 ExecutorStatistics stats = entry.getValue();  Logger.sendMessage(user+":"+stats);
		}	

	}
	
}
