package com.app.jueee.concurrency.chapter03.cache;

import java.util.Date;

/**
 *  该类用于描述在缓存中存放的每个元素
 *	
 *	@author hzweiyongqiang
 */
public class CacheItem {

    // 在缓存中存储的命令。我们将 Query 和 Report 命令存放在缓存之中。
    private String command;
    
    // 该命令所产生的响应。
    private String response;
    
    // 缓存中某一项的创建日期。
    private Date creationDate;
    
    // 该项在缓存中的最后访问时间。
    private Date accessDate;
    
    public CacheItem(String command, String response) {
        creationDate=new Date();
        accessDate=new Date();
        this.command=command;
        this.response=response;
    }

    public String getCommand() {
        return command;
    }

    public String getResponse() {
        return response;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }
    
}
