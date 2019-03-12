package com.app.jueee.concurrency.chapter06.common;

import java.util.HashMap;

/**
 *  用于存放含有文档以及构成文档的单词的文件名。
 *	
 *	@author hzweiyongqiang
 */
public class Document {

    // 文件名
    private String fileName;
    // 构成该文档的单词集合
    // HashMap 类并不是同步的， 但是仍然可以在并发应用程序中使用，因为不同任务并不会共享该类。
    // 一个 Document 对象只能由一个任务生成，因此使用 HashMap 类时并不会导致并发版应用程序中的竞争条件。
    private HashMap <String, Word> voc;
    
    public Document() {
        voc = new HashMap<>();
    }
    
    // 向词汇表添加单词的方法。
    // 如果单词不在词汇表中，则将其加入词汇表。
    // 如果单词在词汇表中，则增加该单词的 tf 属性值。
    public void addWord(String string) {
        voc.computeIfAbsent(string, k -> new Word(k)).addTf();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HashMap<String, Word> getVoc() {
        return voc;
    }

    public void setVoc(HashMap<String, Word> voc) {
        this.voc = voc;
    }
    
}
