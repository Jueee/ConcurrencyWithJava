package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;

/**
 * Class that implements the concurrent version of the Report command. 
 * Report: The format of this query is: r:codIndicator where codIndicator 
 * is the code of the indicator you want to report
 * @author author
 *
 */
public class ConcurrentReportCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 * @param in The Stream used to read data of the socket. We have to close it
	 */
	public ConcurrentReportCommand (Socket socket, String [] command) {
		super(socket, command);
	}
	
	/**
	 * Method that executes the command
	 */
	@Override
	public String execute() {
	
		WDIDAO dao=WDIDAO.getDAO();
		return dao.report(command[3]);
	}

}
