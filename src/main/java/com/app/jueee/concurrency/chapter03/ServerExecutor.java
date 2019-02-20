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
 * 执行器
 * 扩展了 ThreadPoolExecutor 类并且还有一些内部属性
 */
public class ServerExecutor extends ThreadPoolExecutor {

	/**
	 * 用于存储每个任务开始日期
	 */
	private ConcurrentHashMap<Runnable, Date> startTimes;

	/**
	 * 存储每个用户使用情况统计
	 */
	private ConcurrentHashMap<String, ExecutorStatistics> executionStatistics;
	

	/**
	 * 定义执行器特征
	 */
	private static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 定义执行器特征
	 */
	private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 定义执行器特征
	 */
	private static long KEEP_ALIVE_TIME = 10;
	
	/**
	 * 控制执行器拒绝的任务
	 */
	private static RejectedTaskController REJECTED_TASK_CONTROLLER = new RejectedTaskController();
	
	
	/**
	 * 构造函数
	 */
	public ServerExecutor() {
	    // 调用父类的构造函数，创建了一个 PriorityBlockingQueue 类，用于存储那些将在执行器中执行的任务。
		super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new PriorityBlockingQueue<>(),REJECTED_TASK_CONTROLLER);
		
		startTimes = new ConcurrentHashMap<>();
		executionStatistics = new ConcurrentHashMap<String, ExecutorStatistics>();
	}

	/**
	 *  在执行器的每个任务执行完毕后执行
         *  接收已执行的 ServerTask 对象和 Throwable 对象
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
	  *  在每个任务执行之前执行。
	  *  接收 ServerTask 对象和执行该任务的线程作为参数。
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		startTimes.put(r, new Date());
	}

	/**
	 *  该方法执行后会转换发送给执行器的 Runnable 对象
	 *  使用执行器待执行的 FutureTask 实例中的 submit() 方法。
	 */
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		return new ServerTask<T>((ConcurrentCommand)runnable);
	}


	/**
	 *  将执行器中存储的所有统计信息写入日志系统。
	 */
	public void writeStatistics() {
		for(Entry<String, ExecutorStatistics> entry: executionStatistics.entrySet()) {
			 String user = entry.getKey();
			 ExecutorStatistics stats = entry.getValue();  Logger.sendMessage(user+":"+stats);
		}	

	}
	
}
