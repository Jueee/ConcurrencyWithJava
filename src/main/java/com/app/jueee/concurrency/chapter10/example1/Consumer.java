package com.app.jueee.concurrency.chapter10.example1;

import java.util.Random;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.TimeUnit;

public class Consumer implements Subscriber<Event>{

    // 存储消费者的名称
    private String name;
    // 存储 Flow.Subscription 实例，该实例负责管理消费者与生产者之间的通信。
    private Subscription subscription;
    
    public Consumer (String name) {
        this.name = name;
    }
    
    // 当消费者希望订阅其通知时， SubmissionPublisher 类将调用 onSubscribe() 方法
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
        this.showMessage("Subscription OK");
    }

    // 对于每个事件， SubmissionPublisher 类都将调用 onNext() 方法。
    @Override
    public void onNext(Event event) {
        this.showMessage("An event has arrived: "+event.getSource()+":"+event.getDate()+": "+event.getMsg());
        this.subscription.request(1);
        processEvent(event);
    }

    @Override
    public void onError(Throwable throwable) {
        this.showMessage("An error has ocurred");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        this.showMessage("No more events");
    }
    
    private void showMessage (String txt) {
        System.out.println(Thread.currentThread().getName()+":"+this.name+":"+txt);
    }

    // 使用 processEvent() 方法模拟消费者处理事件的时间。
    // 随机等待 0 到 3 秒以实现这一行为。
    private void processEvent(Event event) {
        Random random = new Random();
        int number = random.nextInt(3);
        try {
            TimeUnit.SECONDS.sleep(number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
