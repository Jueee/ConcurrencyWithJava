package com.app.jueee.concurrency.chapter05.common;

import java.util.Map;


/**
 *  用于存放文档中所含单词的列表。
 *	
 *	@author hzweiyongqiang
 */
public class Document {

    // 文件名
    private String fileName;
    // 词汇表（也就是在文档中用到的单词的列表）
    // 其键为单词，其值为该单词在文档中出现的次数。
    private Map<String, Integer> voc;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Integer> getVoc() {
        return voc;
    }

    public void setVoc(Map<String, Integer> voc) {
        this.voc = voc;
    }

}
