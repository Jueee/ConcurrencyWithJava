package com.app.jueee.concurrency.chapter03.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.app.jueee.concurrency.chapter03.J3ConcurrentVersionServer;
import com.app.jueee.concurrency.chapter03.cache.ParallelCache;
import com.app.jueee.concurrency.chapter03.log.Logger;

/**
 * 所有命令的基类
 */
public abstract class ConcurrentCommand extends Command implements Comparable<ConcurrentCommand>, Runnable{

	// 存储发送该查询的用户名称
	private String username;
	
	// 存储查询的优先级，它将决定查询的执行顺序
	private byte priority;
	
	// 用于与客户端通信的套接字
	private Socket socket;
	
	
	/**
	 * 构造函数
	 */
	public ConcurrentCommand(Socket socket, String[] command) {
		super(command);
		username=command[1];
		priority=Byte.parseByte(command[2]);
		this.socket=socket;
	}

	/**
	 * 计算和返回查询的结果
	 */
	@Override
	public abstract String execute();
	
	/**
	 * 调用 execute() 方法，将结果存储在缓存，写入套接字中，并且关闭在通信中使用的所有资源
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
	 * 使用其优先级属性确定任务执行的顺序
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
