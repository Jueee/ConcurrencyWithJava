package com.app.jueee.concurrency.chapter04.basic;

import java.util.Date;
import java.util.List;

import com.app.jueee.concurrency.chapter04.common.CommonInformationItem;
import com.app.jueee.concurrency.chapter04.common.NewsBuffer;
import com.app.jueee.concurrency.chapter04.common.RSSDataCapturer;

public class NewsTask implements Runnable {
    // RSS 订阅的名称
    private String name;
    // RSS 订阅的URL
    private String url;
    // 新闻
    private NewsBuffer buffer;

    public NewsTask(String name, String url, NewsBuffer buffer) {
        this.name = name;
        this.url = url;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        System.out.println(name + " : Running. " + new Date());
        RSSDataCapturer capturer = new RSSDataCapturer(name);
        List<CommonInformationItem> items = capturer.load(url);
        for (CommonInformationItem item : items) {
            buffer.add(item);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NewsBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(NewsBuffer buffer) {
        this.buffer = buffer;
    }
    
}
