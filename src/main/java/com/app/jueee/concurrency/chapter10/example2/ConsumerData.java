package com.app.jueee.concurrency.chapter10.example2;

import java.util.concurrent.Flow.Subscriber;

public class ConsumerData {

    // 使用 News 类参数化的 Subscriber 值。它将存储新闻消费者的关联关系。
    private Subscriber<News> consumer;
    // 与发布者和订阅者之间的订阅关系相关的 MySubscription 值
    private MySubscription subscription;

    public Subscriber<News> getConsumer() {
        return consumer;
    }

    public void setConsumer(Subscriber<News> consumer) {
        this.consumer = consumer;
    }

    public MySubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(MySubscription subscription) {
        this.subscription = subscription;
    }
    
    
}
