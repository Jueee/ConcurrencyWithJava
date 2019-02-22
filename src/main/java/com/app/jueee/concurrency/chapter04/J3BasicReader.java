package com.app.jueee.concurrency.chapter04;

import java.util.concurrent.TimeUnit;

import com.app.jueee.concurrency.chapter04.basic.NewsSystem;

public class J3BasicReader {

    public static void main(String[] args) {
        // 创建 system 并将其作为一个线程执行
        NewsSystem system = new NewsSystem("data\\chapter04\\sources.txt");
        Thread t = new Thread(system);
        t.start();
        // 等待 10 分钟
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 通知 system 终止
        system.shutdown();
    }
}
