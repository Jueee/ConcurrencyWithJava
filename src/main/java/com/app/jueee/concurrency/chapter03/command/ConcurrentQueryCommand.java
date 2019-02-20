package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;

/**
 * 用于获取有关某个国家、某个指数以及某个年份（可选）的信息
 *
 */
public class ConcurrentQueryCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 * @param in The Stream used to read data. We have to close it
	 */
	public ConcurrentQueryCommand (Socket socket, String [] command) {
		super(socket,command);
	}
	

	/**
	 * Method that executes the query 
	 */
	@Override
	public String execute() {

		WDIDAO dao=WDIDAO.getDAO();
		
		if (command.length==5) {
			return dao.query(command[3], command[4]);
		} else if (command.length==6) {
			try {
				return dao.query(command[3], command[4], Short.parseShort(command[5]));
			} catch (NumberFormatException e) {
				return "ERROR;Bad Command";
			}
		} else {
			return "ERROR;Bad Command";
		}
	} 

}
