package com.app.jueee.concurrency.chapter10.example1;

import java.util.Date;

public class Event {

    // 存储消息
    private String msg;
    // 存储生成 Event 对象的类的名称
    private String source;
    // 存储 Event 生成的日期
    private Date date;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
