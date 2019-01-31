package com.app.jueee.concurrency.chapter03.command;

import java.net.Socket;

/**
 * Class that implements the concurrent version of the Query Command. The format of 
 * this query is: q;codCountry;codIndicator;year where codCountry is the code of the country, 
 * codIndicator is the code of the indicator and the year is an optional parameter with the year 
 * you want to query
 * @author author
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
