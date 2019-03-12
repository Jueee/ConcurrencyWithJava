package com.app.jueee.concurrency.chapter06.common;

/**
 *  用于存放单词字符串以及将该单词作为关键字的文档数量。
 *	
 *	@author hzweiyongqiang
 */
public class Keyword implements Comparable<Keyword>{

    /**
     *  按照文件数量由多到小的顺序排列关键字
     */
    @Override
    public int compareTo(Keyword o) {
        return Integer.compare(o.getDf(), this.getDf());
    }
    
    // 完整的单词
    private String word;
    // 将该单词作为关键字的文档数
    private int df;
    
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public int getDf() {
        return df;
    }
    public void setDf(int df) {
        this.df = df;
    }

}
