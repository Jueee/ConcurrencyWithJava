package com.app.jueee.concurrency.chapter05;

import java.util.Date;
import java.util.List;

import com.app.jueee.concurrency.chapter05.common.BestMatchingData;
import com.app.jueee.concurrency.chapter05.common.WordsLoader;

/**
 *  加载 UKACD 文件，调用 getBestMatchingWords() 方法（该方法以接收到的字符串作为参数）
 *  然后在控制台显示结果以及算法执行时间。
 *	
 *	@author hzweiyongqiang
 */
public class J2BestMachingSerialMain {

    public static void main(String[] args) {
        
        String word = "java";
        
        Date startTime, endTime;
        List<String> dictionary = WordsLoader.load("data/chapter05/UK Advanced Cryptics Dictionary.txt");
        System.out.println("Dictionary Size:"+dictionary.size());
        
        startTime = new Date();
        BestMatchingData result = BestMatchingSerialCalculation.getBestMatchingWords(word, dictionary);
        List<String> results = result.getWords();
        endTime = new Date();
        
        System.out.println("Word: " + word);
        System.out.println("Minimum distance: " + result.getDistance());
        System.out.println("List of best matching words: " + results.size());
        results.forEach(System.out::println);
        System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()));
    }
}
