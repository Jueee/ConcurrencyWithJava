package com.app.jueee.concurrency.chapter06;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Phaser;

import com.app.jueee.concurrency.chapter06.common.Word;

public class J2ConcurrentKeywordExtraction {

    public static void main(String[] args) {
        Date start, end;
        ConcurrentHashMap<String, Word> globalVoc = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> globalKeywords = new ConcurrentHashMap<>();

        start = new Date();
        File source = new File("data//chapter05//data");
        File[] files = source.listFiles(f -> f.getName().endsWith(".txt"));
        if (files == null) {
            System.err.println("The 'data' folder not found!");
            return;
        }
        ConcurrentLinkedDeque<File> concurrentFileListPhase1 = new ConcurrentLinkedDeque<>(Arrays.asList(files));
        ConcurrentLinkedDeque<File> concurrentFileListPhase2 = new ConcurrentLinkedDeque<>(Arrays.asList(files));
        int numDocuments = files.length;
        int factor = 1;
        if (args.length > 0) {
            factor = Integer.valueOf(args[0]);
        }
        int numTasks = factor * Runtime.getRuntime().availableProcessors();
        Phaser phaser = new Phaser();
        Thread[] threads = new Thread[numTasks];
        KeywordExtractionTask[] tasks = new KeywordExtractionTask[numTasks];

        // 将创建的第一个任务其主参数置为 true ，其他任务的主参数置为 false 。
        // 每个任务创建完毕后，我们使用 Phaser 类的 register() 方法在分段器中注册一个新的参与方
        for (int i = 0; i < numTasks; i++) {
            tasks[i] = new KeywordExtractionTask(concurrentFileListPhase1, concurrentFileListPhase2, phaser, globalVoc,
                globalKeywords, concurrentFileListPhase1.size(), "Task" + i, i == 0);
            phaser.register();
            System.out.println(phaser.getRegisteredParties() + "tasks arrived to the Phaser.");
        }

        // 创建并启动运行该任务的线程对象，并且等待其结束。
        for (int i = 0; i < numTasks; i++) {
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        for (int i = 0; i < numTasks; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 在控制台输出有关执行情况的统计结果，包括执行时间。
        System.out.println("Is Terminated: " + phaser.isTerminated());
        end = new Date();
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
        System.out.println("Vocabulary Size: " + globalVoc.size());
        System.out.println("Number of Documents: " + numDocuments);
    }
}
