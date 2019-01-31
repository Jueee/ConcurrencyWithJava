package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

/**
 *  模拟了同时存在多个客户端的情况。  
 *  对于这种情形，我们为每个 SerialClient 创建一个线程，并且同时运行这些客户端以查看服务器的性能。
 *  我们测试了 1 到 5 个并发客户端。
 *	
 *	@author hzweiyongqiang
 */
public class J3Test_MultipleSerialClients {

    public static void main(String[] args) {
        final int NUM_CLIENTS = 5;

        for (int i = 1; i <= NUM_CLIENTS; i++) {
            System.out.println("Number of Simultaneous Clients: " + i);
            Thread[] threads = new Thread[i];
            J3Test_SerialClient client = new J3Test_SerialClient();
            for (int j = 0; j < i; j++) {
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

        try (Socket echoSocket = new Socket("localhost", J3Test_SerialClient.SERIAL_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            StringWriter writer = new StringWriter();
            writer.write("z");
            writer.write(";");

            String command = writer.toString();
            out.println(command);
            String output = in.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
