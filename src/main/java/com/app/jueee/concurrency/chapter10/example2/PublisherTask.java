package com.app.jueee.concurrency.chapter10.example2;

public class PublisherTask implements Runnable{

    private ConsumerData consumerData;
    // 想要发送给订阅者的新闻的 News 对象
    private News news;
    
    public PublisherTask(ConsumerData consumerData, News news) {
        this.consumerData = consumerData;
        this.news = news;
    }
    
    /**
     *  该方法将检查是否必须将 News 对象发送给订阅者。
     *  
     *  它将检查以下三个条件。
     *  1、订阅没有取消：使用 subscription 对象的 isCancelled() 方法。
     *  2、订阅者请求了更多的条目：使用 subscription 对象的 getRequested() 方法。
     *  3、News 对象的类别存在于与该订阅者关联的类别集中：使用 subscription 对象的 hasCategory()方法。
     */
    @Override
    public void run() {
        MySubscription subscription = consumerData.getSubscription();
        if (!(subscription.isCanceled()) && (subscription.getRequested() > 0)
            && (subscription.hasCategory(news.getCategory()))) {
            // 使用 onNext() 方法将其发送给订阅者。
            consumerData.getConsumer().onNext(news);
            // 使用 decreaseRequested() 方法来减少该订阅者请求的条目数。
            subscription.decreaseRequested();
        }
    }
}
