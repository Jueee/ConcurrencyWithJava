package com.app.jueee.concurrency.chapter04.advanced;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter04.basic.NewsTask;
import com.app.jueee.concurrency.chapter04.common.NewsBuffer;
import com.app.jueee.concurrency.chapter04.common.NewsWriter;

/**
 * 完成执行器和任务的初始化，然后等待执行结束。
 * 
 * @author hzweiyongqiang
 */
public class NewsAdvancedSystem implements Runnable {

    // 含有 RSS 源的文件路径
    private String route;

    private ScheduledThreadPoolExecutor executor;
    // 存放新闻的缓存
    private NewsBuffer buffer;
    // 控制其执行结束的 CountDownLatch 对象
    // CountDownLatch 类是一种同步机制， 允许存在一个线程等待某一事件。
    private CountDownLatch latch = new CountDownLatch(1);

    public NewsAdvancedSystem(String route) {
        this.route = route;
        executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        buffer = new NewsBuffer();
    }

    /**
     *  1. 读取了所有的 RSS 源，为每个 RSS 源创建了一个 NewsTask 类
     *  2. 将它们发送给 ScheduledThreadPool 执行器
     *  3. 使用 Executors 类的 newScheduledThreadPool()方法创建执行器，并使用 scheduleAtFixedDelay() 方法将任务发送给该执行器，也将 NewsWriter实例作为一个线程启动。
     *  4. 等待通知消息，在收到通知后采用 CountDownLatch 类的 await() 方法结束其执行
     *  5. 结束 NewsWriter 任务和 ScheduledExecutor 的执行。
     */
    @Override
    public void run() {
        Path file = Paths.get(route);
        NewsWriter newsWriter = new NewsWriter(buffer);
        Thread t = new Thread(newsWriter);
        t.start();
        try (InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String data[] = line.split(";");
                NewsTask task = new NewsTask(data[0], data[1], buffer);
                System.out.println("Task " + task.getName());
                executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (this) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Shutting down the executor.");
        executor.shutdown();
        t.interrupt();
        System.out.println("The system has finished.");
    }

    /**
     *  使用 CountDownLatch 类的 countDown() 方法停止其执行过程
     *  该方法将会唤醒 run() 方法，这样就可以关闭正在运行 NewsTask 对象的执行器。
     */
    public void shutdown() {
        latch.countDown();
    }
}
