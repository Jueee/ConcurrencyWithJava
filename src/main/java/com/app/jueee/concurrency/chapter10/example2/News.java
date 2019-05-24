package com.app.jueee.concurrency.chapter10.example2;

import java.util.Date;

public class News {
    
    public static final int SPORTS=0;
    public static final int WORLD=1;
    public static final int ECONOMIC=2;
    public static final int SCIENCE=3;

    // 存储新闻类别,采用数值0、1、2和3分别表示体育、世界、经济和科学类别的新闻。
    private int category;
    // 存储新闻文本
    private String txt;
    // 存储新闻日期
    private Date date;
    
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public String getTxt() {
        return txt;
    }
    public void setTxt(String txt) {
        this.txt = txt;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
}
