package com.app.jueee.concurrency.chapter04.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.app.jueee.concurrency.chapter04.common.CommonInformationItem;

/**
 * 实现了缓存
 * 
 * @author hzweiyongqiang
 */
public class NewsBuffer {

    // 带有阻塞操作的并发数据结构
    // 如果从列表中获取某个项但是列表为空，那么调用方法的线程就会被阻塞，直到列表中有元素为止。
    private LinkedBlockingQueue<CommonInformationItem> buffer;

    // 存储之前在缓存中存放的各新闻项的 ID
    private ConcurrentHashMap<String, String> storedItems;

    public NewsBuffer() {
        buffer = new LinkedBlockingQueue<>();
        storedItems = new ConcurrentHashMap<String, String>();
    }

    /**
     * 将某一项存储到缓存，并预先检查该项此前是否已经插入
     * 
     * @param
     */
    public void add(CommonInformationItem item) {
        // 使用 compute() 方法将元素插入  ConcurrentHashMap 。
        // 该方法接收一个 lambda 表达式作为参数，其中含有键和与键相关联的实际值（如果该键没有相关联的值则为 null）。
        storedItems.compute(item.getId(), (id, oldSource) -> {
            if (oldSource == null) {
                buffer.add(item);
                return item.getSource();
            } else {
                System.out.println("Item " + item.getId() + " has been processed before");
                return oldSource;
            }
        });
    }

    /**
     * 从缓存中获取下一项
     * @return
     * @throws InterruptedException
     */
    public CommonInformationItem get() throws InterruptedException {
        return buffer.take();
    }
}
