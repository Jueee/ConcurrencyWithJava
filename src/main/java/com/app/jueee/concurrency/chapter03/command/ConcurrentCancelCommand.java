package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;

import com.app.jueee.concurrency.chapter03.J3ConcurrentVersionServer;
import com.app.jueee.concurrency.chapter03.log.Logger;

/**
 * 用于撤销某一用户任务的执行
 */
public class ConcurrentCancelCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class. It receives the socket to communicate with the client and the parameters of the command
	 * @param socket Socket to communicate with the client
	 * @param command Parameters of the command
	 * @param in The Stream used to read the data from the socket. We have to close it
	 */
	public ConcurrentCancelCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	/**
	 * Main method of the command. Cancel the tasks of the client and return the message to write to the client
	 */
	@Override
	public String execute() {
	    // 停止执行与参数中指定用户相关的所有待处理任务
	    J3ConcurrentVersionServer.cancelTasks(getUsername());
		String message = "Tasks of user "
				+getUsername()
				+" has been cancelled.";
		Logger.sendMessage(message);
		return message;
	}

}
