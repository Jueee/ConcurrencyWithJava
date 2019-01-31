package com.app.jueee.concurrency.chapter03.cache;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelCache {

    private ConcurrentHashMap<String, CacheItem> cache;
    /**
     * Task to clean the cache
     */
    private CleanCacheTask task;
    /**
     * Thread to execute the clean task
     */
    private Thread thread;

    /**
             *  构造函数初始化了该缓存的元素。  
             *  创建了 ConcurrentHashMap 对象并且启动了一个执行 CleanCacheTask 类的线程
     */
    public ParallelCache() {
        cache = new ConcurrentHashMap<String, CacheItem>();
        task = new CleanCacheTask(this);
        thread = new Thread(task);
        thread.start();
    }

    /**
     *  将元素插入HashMap
     *	@param command
     *	@param response
     */
    public void put(String command, String response) {
        CacheItem item = new CacheItem(command, response);
        cache.put(command, item);
    }

    /**
     *  在 HashMap 中检索元素
     *	@param command
     *	@return
     */
    public String get(String command) {
        CacheItem item = cache.get(command);
        if (item == null) {
            return null;
        }
        item.setAccessDate(new Date());
        return item.getResponse();
    }

    /**
     *  清理缓存
     */
    public void cleanCache() {
        Enumeration<String> keys = cache.keys();
        Date revisionDate = new Date();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            CacheItem item = cache.get(key);
            if (revisionDate.getTime() - item.getAccessDate().getTime() > 600000) {
                cache.remove(key);
            }
        }
    }

    /**
     *  关闭缓存
     */
    public void shutdown() {
        thread.interrupt();
    }

    /**
     *  返回缓存中存储的元素数
     *	@return
     */
    public int getItemNumber() {
        return cache.size();
    }
}
