package com.app.jueee.concurrency.chapter10.example2;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.ThreadPoolExecutor;

public class MyPublisher implements Publisher<News>{

    // 用于存储该发布者的所有订阅者的信息
    private ConcurrentLinkedDeque<ConsumerData> consumers;
    // 用于执行 PublisherTask 对象
    private ThreadPoolExecutor executor;
    
    public MyPublisher() {
        consumers=new ConcurrentLinkedDeque<>();
        executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    // 该方法接收想要订阅该发布者的   Subscriber 对象作为参数。
    @Override
    public void subscribe(Subscriber<? super News> subscriber) {
        ConsumerData consumerData = new ConsumerData();
        consumerData.setConsumer((Subscriber<News>)subscriber);
        
        MySubscription subscription = new MySubscription();
        consumerData.setSubscription(subscription);
        
        subscriber.onSubscribe(subscription);
        
        consumers.add(consumerData);
    }
    
    
    // 该方法接收一个 News 对象作为参数，并尝试将其发送给该发布者的所有订阅者。
    public void publish(News news) {
        consumers.forEach(data -> {
            try {
                executor.execute(new PublisherTask(data, news));
            } catch (Exception e) {
                data.getConsumer().onError(e);
            }
        });
    }

    // 该方法将通知所有订阅者通信结束，并且完成内部 ThreadPoolExecutor 的执行。
    public void shutdown() {
        consumers.forEach(data -> {
            data.getConsumer().onComplete();
        });
        executor.shutdown();
    }
}
