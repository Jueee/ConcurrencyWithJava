package com.app.jueee.concurrency.chapter03.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.app.jueee.concurrency.chapter03.J3ConcurrentVersionServer;
import com.app.jueee.concurrency.chapter03.cache.ParallelCache;
import com.app.jueee.concurrency.chapter03.log.Logger;

/**
 * Abstract base class of the concurrent commands
 * @author author
 *
 */
public abstract class ConcurrentCommand extends Command implements Comparable<ConcurrentCommand>, Runnable{

	/**
	 * Username that executes the command
	 */
	private String username;
	
	/**
	 * Priority of the command
	 */
	private byte priority;
	
	/**
	 * Socket to write to the client 
	 */
	private Socket socket;
	
	
	/**
	 * Constructor of the class
	 * @param socket Socket to communicate with the client
	 * @param command Parameters of the command
	 * @param in The Stream used to read the data of the socket. We have to close it
	 */
	public ConcurrentCommand(Socket socket, String[] command) {
		super(command);
		username=command[1];
		priority=Byte.parseByte(command[2]);
		this.socket=socket;
	}

	/**
	 * Abstract method with the logic of the command
	 */
	@Override
	public abstract String execute();
	
	/**
	 * Method that executes the command as a concurrent task. Write a message in the log, execute the
	 * logic of the command and writes the output of the command to the client using the socket
	 */
	@Override
	public void run() {
		
		String message="Running a Task: Username: "
				+username
				+"; Priority: "
				+priority;
		Logger.sendMessage(message);
		
		String ret=execute();
		
		ParallelCache cache = J3ConcurrentVersionServer.getCache();

		if (isCacheable()) {
			cache.put(String.join(";",command), ret);
		}
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(ret);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(ret);
	}
	

	/**
	 * Compare one command with another using the Priority
	 */
	@Override
	public int compareTo(ConcurrentCommand o) {
		return Byte.compare(o.getPriority(), this.getPriority());
	}

	/**
	 * Method that returns the username
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Method that stablish the username
	 * @param username The username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Method that returns the priority
	 * @return The priority
	 */
	public byte getPriority() {
		return priority;
	}

	/**
	 * Method that stablish the priority
	 * @param priority The priority
	 */
	public void setPriority(byte priority) {
		this.priority = priority;
	}

	/**
	 * Method that returns the socket
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Method that stablish the socket
	 * @param socket the socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	

}
