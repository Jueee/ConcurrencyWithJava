package com.app.jueee.concurrency.chapter03.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that implements a concurrent logger system
 * @author author
 *
 */
public class Logger {

    // 用于存放日志数据
    private static ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<String>();
    
    /**
     * Thread to execute the Log Task 
     */
    private static Thread thread;
    
    /**
     * Route to the file where we will write the log
     */
    private static final String ROUTE = "server.log";

    /**
     *  初始化并启动执行 LogTask 的线程
     */
    static {
        LogTask task = new LogTask();
        thread = new Thread(task);
        thread.start();
    }

    /**
     *  接收一个字符串作为参数并且将该消息存放在队列之中。
     *	@param message
     */
    public static void sendMessage(String message) {
        StringWriter writer = new StringWriter();

        writer.write(new Date().toString());
        writer.write(": ");
        writer.write(message);

        logQueue.offer(writer.toString());
    }

    /**
     *  使用 ConcurrentLinkedQueue 类的 poll() 方法获取并删除队列中存储的所有日志消息，并将它们写入文件。
     */
    public static void writeLogs() {
        String message;
        Path path = Paths.get(ROUTE);
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path,StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            while ((message = logQueue.poll()) != null) {
                StringWriter writer = new StringWriter();
                writer.write(new Date().toString());
                writer.write(": ");
                writer.write(message);
                fileWriter.write(writer.toString());
                fileWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  用于删减日志文件
     */
    public static void initializeLog() {
        Path path = Paths.get(ROUTE);
        if (Files.exists(path)) {
            try (OutputStream out = Files.newOutputStream(path,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  结束日志系统的执行器
     */
    public static void shutdown() {
        writeLogs();
        // 中断执行 LogTask 的线程。
        thread.interrupt();
    }
}
