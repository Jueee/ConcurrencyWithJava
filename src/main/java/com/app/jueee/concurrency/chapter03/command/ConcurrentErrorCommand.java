package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;


/**
 * Concurrent version of the ErrorCommand. It's executed when an unknown command arrives
 * @author author
 *
 */
public class ConcurrentErrorCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 * @param in The Stream used to read the data from the socket. We have to close it
	 */
	public ConcurrentErrorCommand(Socket socket, String[] command) {
		super(socket,command);
		setCacheable(false);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
		return "Unknown command: "+command[0];
	}

}
