package com.app.jueee.concurrency.chapter10.example2;

import java.util.Set;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class Consumer implements Subscriber<News>{
    
    // 存储了订阅者和发布者之间的订阅关系
    private MySubscription subscription;
    // 存储订阅者名称
    private String name;
    // 存储了该订阅者想要接收的消息的类别
    private Set<Integer> categories;
    
    public Consumer(String name, Set<Integer> categories) {
        this.name=name;
        this.categories = categories;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = (MySubscription)subscription;
        this.subscription.setCategories(this.categories);
        this.subscription.request(1);
        System.out.printf("%s: Consumer - Subscription\n", Thread.currentThread().getName());
    }

    @Override
    public void onNext(News item) {
        System.out.printf("%s - %s: Consumer - News\n", name, Thread.currentThread().getName());
        System.out.printf("%s - %s: Text: %s\n", name, Thread.currentThread().getName(),item.getTxt());
        System.out.printf("%s - %s: Category: %s\n", name, Thread.currentThread().getName(), item.getCategory());
        System.out.printf("%s - %s: Date: %s\n", name, Thread.currentThread().getName(),item.getDate());
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.printf("%s - %s: Consumer - Error: %s\n", name, Thread.currentThread().getName(), throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.printf("%s - %s: Consumer - Completed\n", name, Thread.currentThread().getName());
    }

}
