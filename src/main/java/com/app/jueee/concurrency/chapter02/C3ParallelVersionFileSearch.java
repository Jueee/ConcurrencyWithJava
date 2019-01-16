package com.app.jueee.concurrency.chapter02;

import java.io.File;
import java.lang.Thread.State;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 第三个并发版本：线程的数量由处理器决定
 * 
 * @author hzweiyongqiang
 */
public class C3ParallelVersionFileSearch {

    /**
     *	@param file  指向搜索基本路径的 File 对象
     *	@param fileName  存储当前查找文件名称的 fileName 的 String
     *	@param parallelResult    存放操作结果的 Result 对象作为参数
     */
    public static void searchFiles(File file, String fileName, Result parallelResult) {

        // 将基本路径所包含的所有目录存放在其中
        ConcurrentLinkedQueue<File> directories = new ConcurrentLinkedQueue<>();
        File[] contents = file.listFiles();

        for (File content : contents) {
            if (content.isDirectory()) {
                directories.add(content);
            }
        }

        // 创建与 JVM 可用处理器数量相同的线程
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];
        ParallelGroupFileTask[] tasks = new ParallelGroupFileTask[numThreads];

        for (int i = 0; i < numThreads; i++) {
            tasks[i] = new ParallelGroupFileTask(fileName, parallelResult, directories);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        boolean finish = false;
        int numFinished = 0;

        // 如果其中一个线程找到了正在查找的文件，该线程会立即终止执行。
        while (!finish) {
            numFinished = 0;
            for (int i = 0; i < threads.length; i++) {
                // 使用 Thread 类的 getState() 方法检查各个线程是否已完成执行
                if (threads[i].getState() == State.TERMINATED) {
                    numFinished++;
                    if (tasks[i].getFound()) {
                        finish = true;
                    }
                }
            }
            if (numFinished == threads.length) {
                finish = true;
            }
        }

        // 在这种情况下，使用  interrupt() 方法结束其他线程的执行。
        if (numFinished != threads.length) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }

    }
    public static int MATRIX_SIZE = 1000;

    /**
     * 测试并发版文件搜索
     * 
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("C:\\Windows\\");
        String regex = "hosts";
        Date start, end;

        Result result = new Result();
        start = new Date();
        C3ParallelVersionFileSearch.searchFiles(file, regex, result);
        end = new Date();


        System.out.printf("Parallel Search: Execution Time: %d%n", end.getTime() - start.getTime());
    }
}

/**
 * 实现所有将用于查找文件的线程。
 * @author hzweiyongqiang
 */
class ParallelGroupFileTask implements Runnable {

    // 用于存储待查找文件的名称
    private final String fileName;
    // 存储初始路径所包含的目录
    private final ConcurrentLinkedQueue<File> directories;
    // 用于存储搜索结果
    private final Result parallelResult;
    // 标记是否发现了正在寻找的文件
    private boolean found;

    public ParallelGroupFileTask(String fileName, Result parallelResult, ConcurrentLinkedQueue<File> directories) {
        this.fileName = fileName;
        this.parallelResult = parallelResult;
        this.directories = directories;
        this.found = false;
    }

    @Override
    public void run() {
        while (directories.size() > 0) {
            // 每个线程将从队列中获取一条路径，并处理该目录及其所有子目录和其中的文件。
            File file = directories.poll();
            try {
                processDirectory(file, fileName, parallelResult);
                if (found) {
                    System.out.printf("%s has found the file%n", Thread.currentThread().getName());
                    System.out.printf("Parallel Search: Path: %s%n", parallelResult.getPath());
                    return;
                }
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted%n", Thread.currentThread().getName());
            }
        }
    }

    private void processDirectory(File file, String fileName, Result parallelResult) throws InterruptedException {
        File[] contents;
        contents = file.listFiles();

        if ((contents == null) || (contents.length == 0)) {
            return;
        }

        for (File content : contents) {
            if (content.isDirectory()) {
                processDirectory(content, fileName, parallelResult);
                // 使用 isInterrupted() 方法来验证线程是否被中断。
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                // 检查 found 属性是否为 true 。如果为 true ，立即返回以完成线程的执行。
                if (found) {
                    return;
                }
            } else {
                processFile(content, fileName, parallelResult);
                // 使用 isInterrupted() 方法来验证线程是否被中断。
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                // 检查 found 属性是否为 true 。如果为 true ，立即返回以完成线程的执行。
                if (found) {
                    return;
                }
            }
        }
    }

    /**
     *  如果找到了作为参数的文件， processFile() 方法接收存储待处理文件的 File 对象、待查找文件的名称、存放操作结果的 Result 对象。
     *	@param content
     *	@param fileName
     *	@param parallelResult
     */
    private void processFile(File content, String fileName, Result parallelResult) {
        if (content.getName().equals(fileName)) {
            parallelResult.setPath(content.getAbsolutePath());
            this.found = true;
        }
    }

    public boolean getFound() {
        return found;
    }
}