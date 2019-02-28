package com.app.jueee.concurrency.chapter05.common;

import java.util.List;

/**
 *  用于存放最佳匹配算法的结果。它存储了单词列表以及这些单词与输入字符串之间的距离
 *	
 *	@author hzweiyongqiang
 */
public class BestMatchingData {

    // 存放了这些单词与输入字符串之间的距离
    private int distance;
    
    // 存储单词列表的字符串对象列表
    private List<String> words;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
    
}
