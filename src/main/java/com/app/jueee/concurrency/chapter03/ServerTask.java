package com.app.jueee.concurrency.chapter03;

import java.util.concurrent.FutureTask;

import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;

/**
 * The tasks of the executor are always instances of the FutureTask class. We extends this class to
 * implement our task instances and store the commands on it and have access to its data
 * @author author
 *
 * @param <V> This param is not used
 */
public class ServerTask<V> extends FutureTask<V> implements Comparable<ServerTask<V>>{

	/**
	 * Command that is executed by this task
	 */
	private ConcurrentCommand command;
	
	/**
	 * Constructor of the class
	 * @param command Command that will be executed by this task
	 */
	public ServerTask(ConcurrentCommand command) {
		super(command, null);
		this.command=command;
	}

	/**
	 * Method that returns the command executed by this task
	 * @return
	 */
	public ConcurrentCommand getCommand() {
		return command;
	}

	/**
	 * Method that establish the command executed by this task
	 * @param command
	 */
	public void setCommand(ConcurrentCommand command) {
		this.command = command;
	}

	/**
	 * Method that compares two ServerTask objects
	 */
	@Override
	public int compareTo(ServerTask<V> other) {
		return command.compareTo(other.getCommand());
	}
}
