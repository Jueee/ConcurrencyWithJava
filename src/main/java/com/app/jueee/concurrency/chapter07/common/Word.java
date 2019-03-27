package com.app.jueee.concurrency.chapter07.common;

/**
 *  用于存放单词字符串和度量该单词的指标（ TF 、 DF 和 TF-IDF ）。
 *	
 *	@author hzweiyongqiang
 */
public class Word implements Comparable<Word>{
    
    // 单词的索引
    private int index;
    // 在文档中的 TF-IDF 值
    private double tfidf;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getTfidf() {
        return tfidf;
    }

    public void setTfidf(double tfidf) {
        this.tfidf = tfidf;
    }

    // 根据两个单词的 TF-IDF 值对它们进行排序
    @Override
    public int compareTo(Word o) {
        if (this.getIndex() < o.getIndex()) {
            return -1;
        } else if (this.getIndex() > o.getIndex()) {
            return 1;
        }
        return 0;
    }

}
