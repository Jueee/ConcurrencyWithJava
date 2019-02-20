package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter03.command.WDI;
import com.app.jueee.concurrency.chapter03.command.WDIDAO;

/**
 * 该类实现了一个可用的串行服务器客户端。 
 * 该客户端产生了 9 个使用 Query 消息的请求和一个使用 Report 消息的查询。 该客户端将重复该过程 10 次，这样就会请求 90 次 Query 查询和 10 次 Report 查询。
 * 
 * @author hzweiyongqiang
 */
public class J3Test_ConcurrentClient implements Runnable {

    private String username;

    private ThreadPoolExecutor executor;

    public J3Test_ConcurrentClient(String username, ThreadPoolExecutor executor) {
        this.username = username;
        this.executor = executor;
    }

    public void run() {
        WDIDAO dao = WDIDAO.getDAO();
        List<WDI> data = dao.getData();
        long globalStart;
        long globalEnd;
        Random randomGenerator = new Random();

        globalStart = System.nanoTime();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                QueryTask task = new QueryTask(data, username);
                executor.submit(task);
            }
            ReportTask task = new ReportTask(data, username);
            executor.submit(task);
        }

        globalEnd = System.nanoTime();
        System.out.println("Total Time: " + (globalEnd - globalStart) + " nano seconds.");

    }

}

class QueryTask implements Runnable {

    private List<WDI> data;
    private String username;

    public QueryTask(List<WDI> data, String username) {
        this.data = data;
        this.username = username;
    }

    @Override
    public void run() {
        Random randomGenerator = new Random();

        try (Socket echoSocket = new Socket("localhost", J3ConcurrentVersionServer.CONCURRENT_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            WDI wdi = data.get(randomGenerator.nextInt(data.size()));

            StringWriter writer = new StringWriter();
            writer.write("q");
            writer.write(";");
            writer.write(username);
            writer.write(";");
            writer.write(String.valueOf(5));
            writer.write(";");
            writer.write(wdi.getCountryCode());
            writer.write(";");
            writer.write(wdi.getIndicatorCode());

            String command = writer.toString();
            out.println(command);
            String output = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class ReportTask implements Runnable {

    private List<WDI> data;
    private String username;

    public ReportTask(List<WDI> data, String username) {
        this.data = data;
        this.username = username;
    }

    @Override
    public void run() {
        Random randomGenerator = new Random();

        try (Socket echoSocket = new Socket("localhost", J3ConcurrentVersionServer.CONCURRENT_PORT);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            WDI wdi = data.get(randomGenerator.nextInt(data.size()));

            StringWriter writer = new StringWriter();
            writer.write("r");
            writer.write(";");
            writer.write(username);
            writer.write(";");
            writer.write(String.valueOf(10));
            writer.write(";");
            writer.write(wdi.getIndicatorCode());

            String command = writer.toString();
            out.println(command);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}