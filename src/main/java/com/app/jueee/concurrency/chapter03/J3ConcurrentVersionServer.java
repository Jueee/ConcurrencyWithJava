package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter03.cache.ParallelCache;
import com.app.jueee.concurrency.chapter03.command.ConcurrentCancelCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentErrorCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentQueryCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentReportCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentStatusCommand;
import com.app.jueee.concurrency.chapter03.command.ConcurrentStopCommand;
import com.app.jueee.concurrency.chapter03.log.Logger;

/**
 * 客户端/服务器：并行版本
 * 
 * @author hzweiyongqiang
 */
public class J3ConcurrentVersionServer {

    public static final int CONCURRENT_PORT = 8089;

    // 调用缓存系统
    private static ParallelCache cache;
    // 从客户端获取连接
    private static ServerSocket serverSocket;
    
    // 指明该类何时要停止执行。 被声明为 volatile 型，因为另一个线程可以更改它。
    private static volatile boolean stopped = false;
    // 存储那些向服务器发送消息的客户端的套接字。
    private static LinkedBlockingQueue<Socket> pendingConnections;
    // 存放执行器执行的每个任务所关联的 Future 对象
    private static ConcurrentMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>> taskController;
    // 创建命令并且将它们发送给执行器
    private static Thread requestThread;
    // 执行 RequestTask 对象
    private static RequestTask task;

    public static void main(String[] args) {
        cache = new ParallelCache();
        Logger.initializeLog();
        pendingConnections = new LinkedBlockingQueue<Socket>();
        taskController = new ConcurrentHashMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>>();
        task = new RequestTask(pendingConnections, taskController);
        requestThread = new Thread(task);
        requestThread.start();
        System.out.println("Initialization completed.");
        try {
            serverSocket = new ServerSocket(CONCURRENT_PORT);
            do {
                try {
                    Socket clientSocket = serverSocket.accept();
                    pendingConnections.put(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!stopped);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finishServer();
        System.out.println("Shutting down cache");
        cache.shutdown();
        System.out.println("Cache ok");
        System.out.println("Main server thread ended");
    }

    public static ParallelCache getCache() {
        return cache;
    }

    public static void cancelTasks(String username) {

        ConcurrentMap<ConcurrentCommand, ServerTask<?>> userTasks = taskController.get(username);
        if (userTasks == null) {
            return;
        }
        int taskNumber = 0;
        Iterator<ServerTask<?>> it = userTasks.values().iterator();
        while (it.hasNext()) {
            ServerTask<?> task = it.next();
            ConcurrentCommand command = task.getCommand();
            if (!(command instanceof ConcurrentCancelCommand) && task.cancel(true)) {
                taskNumber++;
                Logger.sendMessage(
                    "Task with code " + command.hashCode() + "cancelled: " + command.getClass().getSimpleName());
                it.remove();
            }
        }
        String message = taskNumber + " tasks has been cancelled.";
        Logger.sendMessage(message);

    }

    /**
          *     当任务正常执行结束时，该方法从 ServerTask 对象的嵌套 Map 中清除与该任务相关的 Future 对象。
     *	@param username
     *	@param command
     */
    public static void finishTask(String username, ConcurrentCommand command) {

        ConcurrentMap<ConcurrentCommand, ServerTask<?>> userTasks = taskController.get(username);

        userTasks.remove(command);

        String message = "Task with code " + command.hashCode() + " has finished";
        Logger.sendMessage(message);

    }
    
    /**
     *  改变 stopped 变量的取值并且关闭 serverSocket 实例
     */
    public static void shutdown() {
        stopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *  用于停止执行器，中断执行 RequestTask 对象的线程，并且关闭 Logger 系统。
     */
    private static void finishServer() {
        System.out.println("Shutting down the server...");
        task.shutdown();
        task.terminate();
        System.out.println("Shutting down Request task");
        requestThread.interrupt();
        System.out.println("Request task ok");
        System.out.println("Closing socket");
        System.out.println("Shutting down logger");
        Logger.sendMessage("Shuttingdown the logger");
        Logger.shutdown();
        System.out.println("Logger ok");
        System.out.println("Main server thread ended");
    }
}

/**
 *  RequestTask 类是 ConcurrentServer 类与 Executor 类之间的中介
 *  ConcurrentServer类用于连接客户端
 *  Executor 类用于执行并发任务。 
 *  
 *  RequestTask 类打开与客户端连接的套接字，读取查询数据，创建适当的命令，并且将命令发送给执行器。
 *	
 *	@author hzweiyongqiang
 */
class RequestTask implements Runnable {

    // 存储客户端套接字
    private LinkedBlockingQueue<Socket> pendingConnections;

    // 将命令作为并发任务执行
    private ServerExecutor executor = new ServerExecutor();

    // 存储与任务相关的 Future 对象
    private ConcurrentMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>> taskController;

    /**
            *          构造函数初始化
     */
    public RequestTask(LinkedBlockingQueue<Socket> pendingConnections,
            ConcurrentMap<String, ConcurrentMap<ConcurrentCommand, ServerTask<?>>> taskController) {
        this.pendingConnections = pendingConnections;
        this.taskController = taskController;
    }

    /**
     *  执行循环直到该线程中断处理存放在 pendingConnections 对象中的套接字。
     */
    @Override
    public void run() {

        try {
            while (!Thread.currentThread().interrupted()) {
                try {
                    Socket clientSocket = pendingConnections.take();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String line = in.readLine();

                    Logger.sendMessage(line);

                    ConcurrentCommand command;

                    ParallelCache cache = J3ConcurrentVersionServer.getCache();
                    String ret = cache.get(line);
                    if (ret == null) {
                        String[] commandData = line.split(";");
                        System.out.println("Command: " + commandData[0]);
                        switch (commandData[0]) {
                        case "q":
                            System.out.println("Query");
                            command = new ConcurrentQueryCommand(clientSocket, commandData);
                            break;
                        case "r":
                            System.out.println("Report");
                            command = new ConcurrentReportCommand(clientSocket, commandData);
                            break;
                        case "s":
                            System.out.println("Status");
                            command = new ConcurrentStatusCommand(executor, clientSocket, commandData);
                            break;
                        case "z":
                            System.out.println("Stop");
                            command = new ConcurrentStopCommand(clientSocket, commandData);
                            break;
                        case "c":
                            System.out.println("Cancel");
                            command = new ConcurrentCancelCommand(clientSocket, commandData);
                            break;
                        default:
                            System.out.println("Error");
                            command = new ConcurrentErrorCommand(clientSocket, commandData);
                            break;
                        }

                        ServerTask<?> controller = (ServerTask<?>)executor.submit(command);
                        storeContoller(command.getUsername(), controller, command);
                    } else {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                        out.println(ret);
                        clientSocket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            // No Action Required
        }
        
    }

    private void storeContoller(String userName, ServerTask<?> controller, ConcurrentCommand command) {
        taskController.computeIfAbsent(userName, k -> new ConcurrentHashMap<>()).put(command, controller);
    }

    /**
     * Method that shutdown the task
     */
    public void shutdown() {
        String message="Request Task: "
                +pendingConnections.size()
                +" pending connections.";
        Logger.sendMessage(message);
        executor.shutdown();
    }

    /**
     * Method that waits for the termination of the executor
     */
    public void terminate() {
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
            executor.writeStatistics();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that returns the executor
     * 
     * @return The executor
     */
    public ServerExecutor getExecutor() {
        return executor;
    }
}
