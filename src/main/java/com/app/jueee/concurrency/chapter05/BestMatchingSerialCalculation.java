package com.app.jueee.concurrency.chapter05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.app.jueee.concurrency.chapter05.common.BestMatchingData;
import com.app.jueee.concurrency.chapter05.common.LevenshteinDistance;

public class BestMatchingSerialCalculation {

    /**
     *  计算与输入字符串最相似的单词的列表
     *	@param word 作为参照的有序字符串
     *	@param dictionary 含有字典中所有单词的字符串对象列表
     *	@return 算法执行结果
     */
    public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) {
        List<String> results = new ArrayList<String>();
        int minDistance = Integer.MAX_VALUE;
        int distance;
        
        /**
         *  处理字典中的所有单词，计算这些单词与参照字符串之间的 Levenshtein 距离。
         *  如果针对一个单词计算得到的距离比实际最小距离更小，那么我们将清空结果列表并且将该单词存放在列表中。
         *  如果针对一个单词计算得到的距离与实际最小距离相等，那么将该单词添加到结果列表中。
         */
        for(String str:dictionary) {
            distance = LevenshteinDistance.calculate(word, str);
            if (distance < minDistance) {
                results.clear();
                minDistance = distance;
            }
            if (distance == minDistance) {
                results.add(str);
            }
        }
        
        BestMatchingData result = new BestMatchingData();
        result.setWords(results);
        result.setDistance(minDistance);
        return result;
    }
    
    
    public static void main(String[] args) {
        BestMatchingData result = getBestMatchingWords("abc", Arrays.asList("abb","aaa","acc","bbc"));
        System.out.println(result.getDistance());
        System.out.println(result.getWords());
    }
}
