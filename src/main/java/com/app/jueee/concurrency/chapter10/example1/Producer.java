package com.app.jueee.concurrency.chapter10.example1;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable{

    // 存储 SubmissionPublisher 对象，将事件发送给消费者
    private SubmissionPublisher<Event> publisher;
    // 存储了生产者的名称
    private String name;
    
    public Producer(SubmissionPublisher<Event> publisher, String name) {
        this.publisher = publisher;
        this.name = name;
    }
    
    
    // 该方法中，生成 10 个事件。在一个事件和下一事件之间，随机等待一个随机秒数（0 到 10 之间）。
    @Override
    public void run() {
        Random random = new Random();
        
        for (int i = 0; i < 10; i++) {
            Event event = new Event();
            event.setMsg("Event number:" + i);
            event.setSource(this.name);
            event.setDate(new Date());
            
            publisher.submit(event);
            
            int number = random.nextInt(10);
            
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
