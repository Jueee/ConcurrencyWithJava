package com.app.jueee.concurrency.chapter05;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter05.common.Document;

public class J3ConcurrentIndexing {
    
    public static int test(File source) {
        return test(source.listFiles());
    }
    
    public static int test(File[] files) {
        int numCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Math.max(numCores-1, 1));
        
        ExecutorCompletionService<Document> completionService = new ExecutorCompletionService<>(executor);
        ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex = new ConcurrentHashMap<>();
        
        for(File file: files) {
            // 处理数组中的所有文件，为每个文件创建一个 InvertedTask 对象
            IndexingTask task = new IndexingTask(file);
            // 使用 submit() 方法将其发送给 CompletionService 类
            completionService.submit(task);
            
            // 检查待处理任务队列的规模，如果该队列的规模大于 1000 ，就将该线程休眠，队列规模不再减小之时，我们就不再发送更多任务了。
            if (executor.getQueue().size() > 1000) {
                do {
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (executor.getQueue().size() > 1000);
            }
        }
        
        // 创建两个 InvertedIndexTask 对象来处理由 InvertedTask 任务返回的结果，并且将其作为常规 Thread 对象来执行。
        InvertedIndexTask invertedIndexTask = new InvertedIndexTask(completionService, invertedIndex);
        Thread thread1 = new Thread(invertedIndexTask);
        thread1.start();
        
        InvertedIndexTask invertedIndexTask2 = new InvertedIndexTask(completionService, invertedIndex);
        Thread thread2 = new Thread(invertedIndexTask2);
        thread2.start();
        
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
            thread1.interrupt();
            thread2.interrupt();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return invertedIndex.size();
    }
    
    public static void main(String[] args) {
        File source = new File("data//chapter05//data");
        // 存储所有的文档
        File[] files = source.listFiles();
        System.out.println("invertedIndex: " + test(files));
    }
}
