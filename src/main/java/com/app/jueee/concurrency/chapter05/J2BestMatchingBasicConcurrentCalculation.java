package com.app.jueee.concurrency.chapter05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter05.common.BestMatchingData;
import com.app.jueee.concurrency.chapter05.common.WordsLoader;

/**
 *  创建了执行器和必要的任务，并且将任务发送给执行器。
 *	
 *	@author hzweiyongqiang
 */
public class J2BestMatchingBasicConcurrentCalculation {

    
    public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) throws InterruptedException, ExecutionException {
        // 将机器的核数作为在此使用的最大线程数
        int numCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numCores);
        
        int size = dictionary.size();
        int step = size / numCores;
        int startIndex, endIndex;
        List<Future<BestMatchingData>> results = new ArrayList<>();
        
        for (int i = 0; i < numCores; i++) {
            startIndex = i * step;
            if (i == numCores -1) {
                endIndex = dictionary.size();
            } else {
                endIndex = (i + 1) * step;
            }
            // 创建任务
            BestMatchingBasicTask task = new BestMatchingBasicTask(startIndex, endIndex, dictionary, word);
            // 使用 submit() 方法将其发送给执行器
            // submit() 方法会立即返回，它并不会一直等待任务执行。
            Future<BestMatchingData> future = executor.submit(task);
            results.add(future);
        }
        
        // 调用执行器的 shutdown() 方法来结束其执行，并且对 Future 对象列表执行迭代操作以获得每个任务的执行结果。
        executor.shutdown();
        List<String> words = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        for (Future<BestMatchingData> future : results) {
            BestMatchingData data = future.get();
            if (data.getDistance() < minDistance) {
                words.clear();
                minDistance = data.getDistance();
                words.addAll(data.getWords());
            } else if (data.getDistance() == minDistance) {
                words.addAll(data.getWords());
            }
        }

        BestMatchingData result = new BestMatchingData();
        result.setWords(words);
        result.setDistance(minDistance);
        return result;
    }
    
    public static void test() throws InterruptedException, ExecutionException {
        BestMatchingData result = getBestMatchingWords("abc", Arrays.asList("abb","aaa","acc","bbc"));
        System.out.println(result.getDistance());
        System.out.println(result.getWords());
    }
    
    /**
     *  加载 UKACD 文件，调用 getBestMatchingWords() 方法（该方法以接收到的字符串作为参数）
     *  然后在控制台显示结果以及算法执行时间。
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static void main() throws InterruptedException, ExecutionException {
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
        try {
            test();
            main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
