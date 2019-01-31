package com.app.jueee.concurrency.chapter03;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;

/**
 * This class will process the tasks that arrives to the server when the server is shutting down
 * 
 * @author author
 *
 */
public class RejectedTaskController implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        ConcurrentCommand command = (ConcurrentCommand)task;
        Socket clientSocket = command.getSocket();
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message = "The server is shutting down. Your request can not be served." + " Shuting Down: "
                + String.valueOf(executor.isShutdown()) + ". Terminated: " + String.valueOf(executor.isTerminated())
                + ". Terminating: " + String.valueOf(executor.isTerminating());
            out.println(message);
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
