package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 模拟了同时存在多个客户端的情况。 对于这种情形，我们为每个 SerialClient 创建一个线程，并且同时运行这些客户端以查看服务器的性能。 我们测试了 1 到 5 个并发客户端。
 * 
 * @author hzweiyongqiang
 */
public class J3Test_MultipleConcurrentClients {

    public static void main(String[] args) {
        final int NUM_CLIENTS = 5;

        ThreadPoolExecutor executor =
            (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 1; i <= NUM_CLIENTS; i++) {
            System.out.println("Number of Simultaneous Clients: " + i);
            Thread[] threads = new Thread[i];
            for (int j = 0; j < i; j++) {
                String username = "USER_" + (j + 1);
                J3Test_ConcurrentClient client = new J3Test_ConcurrentClient(username, executor);
                threads[j] = new Thread(client);
                threads[j].start();
            }

            for (int j = 0; j < i; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        try (Socket echoSocket = new Socket("localhost", J3ConcurrentVersionServer.CONCURRENT_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            StringWriter writer = new StringWriter();
            writer.write("s");
            writer.write(";");
            writer.write("USER_1");
            writer.write(";");
            writer.write(String.valueOf(10));
            writer.write(";");

            String command = writer.toString();
            out.println(command);
            String output = in.readLine();
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Socket echoSocket = new Socket("localhost", J3ConcurrentVersionServer.CONCURRENT_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            StringWriter writer = new StringWriter();
            writer.write("c");
            writer.write(";");
            writer.write("USER_2");
            writer.write(";");
            writer.write(String.valueOf(10));
            writer.write(";");

            String command = writer.toString();
            out.println(command);
            String output = in.readLine();
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        try (Socket echoSocket = new Socket("localhost", J3ConcurrentVersionServer.CONCURRENT_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            StringWriter writer = new StringWriter();
            writer.write("z");
            writer.write(";");
            writer.write("admin");
            writer.write(";");
            writer.write(String.valueOf(10));
            writer.write(";");

            String command = writer.toString();
            out.println(command);
            String output = in.readLine();
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
