package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.app.jueee.concurrency.chapter03.command.Command;
import com.app.jueee.concurrency.chapter03.command.ErrorCommand;
import com.app.jueee.concurrency.chapter03.command.QueryCommand;
import com.app.jueee.concurrency.chapter03.command.ReportCommand;
import com.app.jueee.concurrency.chapter03.command.StopCommand;

/**
 *      客户端/服务器：串行版 
 *	
 *	@author hzweiyongqiang
 */
public class J3SerialVersionServer {

    public static int SERIAL_PORT = 8088;

    public static void main(String[] args) throws IOException {
        boolean stopServer = false;
        System.out.println("Initialization completed.");
        try (ServerSocket serverSocket = new ServerSocket(SERIAL_PORT)) {
            do {
                try (Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                    String line = in.readLine();
                    if (line == null) {
                        System.out.println("line is null...");
                        continue;
                    }
                    Command command;
                    String[] commandData = line.split(";");
                    System.out.println("Command: " + commandData[0]);
                    switch (commandData[0]) {
                        case "q":
                            System.out.println("Query");
                            command = new QueryCommand(commandData);
                            break;
                        case "r":
                            System.out.println("Report");
                            command = new ReportCommand(commandData);
                            break;
                        case "z":
                            System.out.println("Stop");
                            command = new StopCommand(commandData);
                            stopServer = true;
                            break;
                        default:
                            System.out.println("Error");
                            command = new ErrorCommand(commandData);
                    }
                    String response = command.execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!stopServer);
        }
    }
}
