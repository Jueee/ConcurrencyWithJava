package com.app.jueee.concurrency.chapter10.example1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class J2CentralizedSystemMain {

    
    public static void main(String[] args) {
        // 使用该对象将事件发送给消费者。
        SubmissionPublisher<Event> publisher = new SubmissionPublisher<>();
        
        // 五个 Consumer 对象，它们将接收发布者创建的所有事件
        for (int i = 0; i < 5; i++) {
            Consumer consumer = new Consumer("Consumer " + i);
            publisher.subscribe(consumer);  // 向发布者订阅消费者
        }
        
        // 将生成事件，并使用 publisher 对象将事件发送给消费者。
        Producer system1 = new Producer(publisher, "System 1");
        Producer system2 = new Producer(publisher, "System 2");
        
        // 执行生产者对象，并使用 commonPool() 方法获取  ForkJoinPool 对象，并且使用 submit() 方法执行它们。
        ForkJoinTask<?> task1 = ForkJoinPool.commonPool().submit(system1);
        ForkJoinTask<?> task2 = ForkJoinPool.commonPool().submit(system2);
        
        // 该循环每 10 秒输出有关任务和发布者对象的信息
        // 为了完成循环的执行，要等待三个条件。
        // 1. 执行第一个生产者对象的任务完成执行。
        // 2. 执行第二个生产者对象的任务完成执行。
        // 3. SubmissionPublisher 对象中再没有未处理事件。使用 estimateMaximumLag() 方法获取该数值。
        do {
            System.out.println("Main: Task 1: "+task1.isDone());
            System.out.println("Main: Task 2: "+task2.isDone());
            System.out.println("Publisher: MaximunLag:" + publisher.estimateMaximumLag());
            System.out.println("Publisher: Max Buffer Capacity: " + publisher.getMaxBufferCapacity());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while ((!task1.isDone()) || (!task2.isDone()) || (publisher.estimateMaximumLag() > 0));
        
        // 通知订阅者执行结束
        publisher.close();
    }
}
