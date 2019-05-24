package com.app.jueee.concurrency.chapter10.example2;

import java.util.Set;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicLong;

public class MySubscription implements Subscription{

    private boolean canceled;
    
    private AtomicLong requested = new AtomicLong(0);
    
    private Set<Integer> categories;

    @Override
    public void request(long value) {
        requested.addAndGet(value);
    }

    @Override
    public void cancel() {
        canceled = true;
    }
    
    // 返回 cancelled 属性的值
    public boolean isCanceled() {
        return canceled;
    }
    
    // 使用 get() 方法返回 requested 属性的值
    public long getRequested() {
        return requested.get();
    }
    
    // 使用 decrementAndGet() 方法减少 requested 属性的值
    public void decreaseRequested() {
        requested.decrementAndGet();
    }
    
    // 设定 categories 属性的值
    public void setCategories(Set<Integer> categories) {
        this.categories = categories;
    }
    
    // 指明参数中的类别（一个 int 值）是否与当前订阅相关联
    public boolean hasCategory(int category) {
        return categories.contains(category);
    }
}
