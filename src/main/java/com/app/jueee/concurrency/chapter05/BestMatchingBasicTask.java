package com.app.jueee.concurrency.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.app.jueee.concurrency.chapter05.common.BestMatchingData;
import com.app.jueee.concurrency.chapter05.common.LevenshteinDistance;

/**
 *  执行那些实现 Callable 接口并且将在执行器中执行的任务。
 *	
 *	@author hzweiyongqiang
 */
public class BestMatchingBasicTask implements Callable<BestMatchingData>{
    
    // 任务要分析的这一部分字典的起始位置（包含）
    private int startIndex;
    // 任务要分析的这一部分字典的结束位置（不包含）
    private int endIndex;
    // 以字符串对象列表形式表示的字典。
    private List<String> dictionary;
    // 参照输入字符串。
    private String word;

    public BestMatchingBasicTask(int startIndex, int endIndex, List<String> dictionary, String word) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dictionary = dictionary;
        this.word = word;
    }

    /**
     *  处理 startIndex 和 endIndex 属性值之间的所有单词，并且计算这些单词与输入字符串之间的 Levenshtein 距离。  
     */
    @Override
    public BestMatchingData call() throws Exception {
        List<String> results = new ArrayList<String>();
        int minDistance = Integer.MAX_VALUE;
        int distance;
        
        /**
         *  处理字典中的所有单词，计算这些单词与参照字符串之间的 Levenshtein 距离。
         *  如果针对一个单词计算得到的距离比实际最小距离更小，那么我们将清空结果列表并且将该单词存放在列表中。
         *  如果针对一个单词计算得到的距离与实际最小距离相等，那么将该单词添加到结果列表中。
         */
        for(int i = startIndex; i< endIndex; i++) {
            distance = LevenshteinDistance.calculate(word, dictionary.get(i));
            if (distance < minDistance) {
                results.clear();
                minDistance = distance;
            }
            if (distance == minDistance) {
                results.add(dictionary.get(i));
            }
        }
        
        BestMatchingData result = new BestMatchingData();
        result.setWords(results);
        result.setDistance(minDistance);
        return result;
    }
}
