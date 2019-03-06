package com.app.jueee.concurrency.chapter05;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter05.common.WordsLoader;

/**
 *  创建了执行器和任务，并且将任务发送给执行器。  
 *	
 *	@author hzweiyongqiang
 */
public class J2ExistBasicConcurrentMain {


    /**
     *  @param word 要查找的单词
     *  @param dictionary 完整的单词列表
     *  @return
     * @throws Exception 
     */
    public static boolean existWord(String word, List<String> dictionary) throws Exception {
        // 将机器的核数作为在此使用的最大线程数
        int numCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numCores);
        
        int size = dictionary.size();
        int step = size / numCores;
        int startIndex, endIndex;
        List<ExistBasicTask> tasks = new ArrayList<>();
        
        for (int i = 0; i < numCores; i++) {
            startIndex = i * step;
            if (i == numCores -1) {
                endIndex = dictionary.size();
            } else {
                endIndex = (i + 1) * step;
            }
            // 创建任务
            ExistBasicTask task = new ExistBasicTask(startIndex, endIndex, dictionary, word);
            // 使用 submit() 方法将其发送给执行器
            // submit() 方法会立即返回，它并不会一直等待任务执行。
            tasks.add(task);
        }
        // 使用 invokeAny() 方法在执行器中执行这些任务。
        // 如果该方法返回一个布尔值，则单词存在，就返回该值。
        // 如果该方法抛出异常，则单词不存在，我们就在控制台打印异常并且返回 false 值。
        // 这两种情况下，我们都调用执行器的 shutdown() 方法来结束其执行
        try {
            Boolean result = executor.invokeAny(tasks);
            return result;
        } catch (Exception e) {
            if (e.getCause() instanceof NoSuchElementException) {
                return false;
            } 
            throw e;
        } finally {
            executor.shutdown();
        }
    }
    


    public static void main() throws Exception {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
