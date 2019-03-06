package com.app.jueee.concurrency.chapter05;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.app.jueee.concurrency.chapter05.common.LevenshteinDistance;
import com.app.jueee.concurrency.chapter05.common.WordsLoader;

public class J2ExistSerialMain {

    /**
     *  @param word 要查找的单词
     *  @param dictionary 完整的单词列表
     *  @return
     */
    public static boolean existWord(String word, List<String> dictionary) {
        for(String string: dictionary) {
            if (LevenshteinDistance.calculate(word, string) == 0) {
                return true;
            }
        }
        return false;
    }

    public static void main() throws InterruptedException, ExecutionException {
        String word = "java";
        
        Date startTime, endTime;
        List<String> dictionary = WordsLoader.load("data/chapter05/UK Advanced Cryptics Dictionary.txt");
        System.out.println("Dictionary Size:"+dictionary.size());
        
        startTime = new Date();
        boolean result = existWord(word, dictionary);
        endTime = new Date();
        
        System.out.println("Exists: " + result);
        System.out.println("Execution Time: " + (endTime.getTime() - startTime.getTime()));
    }
    

    public static void main(String[] args) {
        try {
            main();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
