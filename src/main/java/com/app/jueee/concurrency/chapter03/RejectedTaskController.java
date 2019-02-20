package com.app.jueee.concurrency.chapter03;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;

/**
 * 用以管理其拒绝的任务。如果在执行器已调用 shutdown() 或shutdownNow() 方法之后提交任务，则该任务会被执行器拒绝。
 * 
 * 每个被拒绝的任务都要调用一次 rejectedExecution() 方法，而该方法将接收被拒绝的任务和拒绝该任务的执行器作为参数。
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
