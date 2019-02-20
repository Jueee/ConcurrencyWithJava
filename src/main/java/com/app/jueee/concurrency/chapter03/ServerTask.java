package com.app.jueee.concurrency.chapter03;

import java.util.concurrent.FutureTask;

import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;

/**
 * 向执行器提交 Runnable 对象时，它并不会直接执行该对象，而是创建一个新的对象，即 FutureTask 类的一个实例，而且这项任务由执行器的工作线程执行。
 *
 * 该类扩展了 FutureTask 类并且实现了 Comparable 接口.
 */
public class ServerTask<V> extends FutureTask<V> implements Comparable<ServerTask<V>>{

	/**
	 * 将作为 ConcurrentCommand 对象执行的查询
	 */
	private ConcurrentCommand command;
	
	/**
	 * 使用 FutureTask 类的构造函数并且存储了 ConcurrentCommand 对象
	 * @param 
	 */
	public ServerTask(ConcurrentCommand command) {
		super(command, null);
		this.command=command;
	}

	/**
	 * @return
	 */
	public ConcurrentCommand getCommand() {
		return command;
	}

	/**
	 * @param command
	 */
	public void setCommand(ConcurrentCommand command) {
		this.command = command;
	}

	/**
	 * 用于比较两个 ServerTask 实例存储的命令
	 */
	@Override
	public int compareTo(ServerTask<V> other) {
		return command.compareTo(other.getCommand());
	}
}
