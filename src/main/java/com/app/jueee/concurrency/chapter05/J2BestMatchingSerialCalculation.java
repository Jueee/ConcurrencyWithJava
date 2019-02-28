package com.app.jueee.concurrency.chapter05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.app.jueee.concurrency.chapter05.common.BestMatchingData;
import com.app.jueee.concurrency.chapter05.common.LevenshteinDistance;
import com.app.jueee.concurrency.chapter05.common.WordsLoader;

public class J2BestMatchingSerialCalculation {

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
    
    public static void test() {
        BestMatchingData result = getBestMatchingWords("abc", Arrays.asList("abb","aaa","acc","bbc"));
        System.out.println(result.getDistance());
        System.out.println(result.getWords());
    }
    
    /**
     *  加载 UKACD 文件，调用 getBestMatchingWords() 方法（该方法以接收到的字符串作为参数）
     *  然后在控制台显示结果以及算法执行时间。
     */
    public static void main() {
        String word = "java";
        
        Date startTime, endTime;
        List<String> dictionary = WordsLoader.load("data/chapter05/UK Advanced Cryptics Dictionary.txt");
        System.out.println("Dictionary Size:"+dictionary.size());
        
        startTime = new Date();
        BestMatchingData result = getBestMatchingWords(word, dictionary);
        List<String> results = result.getWords();
        endTime = new Date();
        
        System.out.println("Word: " + word);
        System.out.println("Minimum distance: " + result.getDistance());
        System.out.println("List of best matching words: " + results.size());
        results.forEach(System.out::println);
        System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()));
    }
    
    
    
    
    public static void main(String[] args) {
        test();
        main();
    }
}
