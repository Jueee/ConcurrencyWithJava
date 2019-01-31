package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;

import com.app.jueee.concurrency.chapter03.J3ConcurrentVersionServer;

/**
 * Class that implements the concurrent version of the Stop command.
 * Stops the server
 * @author author
 *
 */
public class ConcurrentStopCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 * @param in The Stream used to read the data from the socket. We have to close it
	 */
	public ConcurrentStopCommand (Socket socket, String [] command) {
		super (socket, command);
		setCacheable(false);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
	    J3ConcurrentVersionServer.shutdown();
		return "Server stopped";
	}

}
