package com.app.jueee.concurrency.chapter06.common;

/**
 *  用于存放单词字符串和度量该单词的指标（ TF 、 DF 和 TF-IDF ）。
 *	
 *	@author hzweiyongqiang
 */
public class Word implements Comparable<Word>{
    
    /**
     *  对 word （接收作为参数的单词）和 df 属性（取值为 1 ）进行了初始化
     *  @param word
     */
    public Word(String word) {
        this.word = word;
        this.tf = 1;
    }
    
    /**
     *  用于增加 tf 属性的值
     */
    public void addTf() {
        this.tf ++ ;
    }
    
    /**
     *  接收一个 Word 对象作为参数，对来自两个不同文档的同一单词进行合并
     *	@param word
     */
    public Word merge(Word word) {
        if (this.word.equals(word.word)) {
            this.tf += word.tf;
            this.df += word.df;
        }
        return this;
    }

    /**
     *  计算得出 tfIdf 属性的值。
     *	@param df df 属性值
     *	@param N 集合中文档的总数
     */
    public void setDf(int df, int N) {
        this.df = df;
        this.tfIdf = this.tf * Math.log(Double.valueOf(N) /df);
    }

    /**
     *  按照 tfIdf 属性值从高到底的顺序对单词进行排序
     */
    @Override
    public int compareTo(Word o) {
        return Double.compare(o.getTfIdf(), this.getTfIdf());
    }

    private String word;
    private int tf;
    private int df;
    private double tfIdf;
    
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }

    public double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }

}
