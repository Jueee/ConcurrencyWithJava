package com.app.jueee.concurrency.chapter06;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Phaser;

import com.app.jueee.concurrency.chapter06.common.Document;
import com.app.jueee.concurrency.chapter06.common.DocumentParser;
import com.app.jueee.concurrency.chapter06.common.Keyword;
import com.app.jueee.concurrency.chapter06.common.Word;

/**
 * 以并发方式实现准备计算关键字的任务。
 * 
 * @author hzweiyongqiang
 */
public class KeywordExtractionTask implements Runnable {

    // 存放全局词汇表
    private ConcurrentHashMap<String, Word> globalVoc;
    
    // 存放全局关键字
    private ConcurrentHashMap<String, Integer> globalKeywords;
    
    // 存放文档集合文件列表
    // 之所以采用两个 ConcurrentLinkedDeque 是因为必须要对整个文档集合解析两次。
    private ConcurrentLinkedDeque<File> concurrentFileListPhase1;
    private ConcurrentLinkedDeque<File> concurrentFileListPhase2;
    
    // 用于控制任务执行
    private Phaser phaser;
    
    private String name;
    
    // 使用布尔值区分主任务与其他任务
    private boolean main;
    
    // 集合中的文档总数：需要该值计算 TF-IDF 指标
    private int parsedDocuments;
    private int numDocuments;

    public KeywordExtractionTask(ConcurrentLinkedDeque<File> concurrentFileListPhase1,
        ConcurrentLinkedDeque<File> concurrentFileListPhase2, Phaser phaser, ConcurrentHashMap<String, Word> globalVoc,
        ConcurrentHashMap<String, Integer> globalKeywords, int numDocuments, String name, boolean main) {
        
        this.concurrentFileListPhase1 = concurrentFileListPhase1;
        this.concurrentFileListPhase2 = concurrentFileListPhase2;
        this.globalVoc = globalVoc;
        this.globalKeywords = globalKeywords;
        this.phaser = phaser;
        this.main = main;
        this.name = name;
        this.numDocuments = numDocuments;
    }

    @Override
    public void run() {
        File file;
        
        // 第一阶段
        // 调用分段器的 arriveAndAwaitAdvance() 方法等待其他任务的创建。所有任务都会同时开始执行。
        phaser.arriveAndAwaitAdvance();
        System.out.println(name + " : Phase 1");
        // poll() 方法检索并且删除 Deque 的第一个元素，这样下一个任务将获取不同的文件进行解析，并且没有文件会被解析两次。
        while ((file = concurrentFileListPhase1.poll()) != null) {
            Document document = DocumentParser.parse(file.getAbsolutePath());
            for(Word word: document.getVoc().values()) {
                // 解析所有文档并且构建 globalVoc，其中含有所有单词及其全局 TF 值和 DF 值。
                globalVoc.merge(word.getWord(), word, Word::merge);
            }
            parsedDocuments ++;
        }
        System.out.println(name + ": " + parsedDocuments + " parsed.");
        // 再次调用 arriveAndAwaitAdvance() 方法，在第二阶段开始之前等待其他任务结束。
        phaser.arriveAndAwaitAdvance();
        
        
        // 第二阶段
        // 计算每个文档最优的 10 个关键字，然后将其插入 ConcurrentHashMap 类。
        System.out.println(name + " : Phase 2");
        while ((file = concurrentFileListPhase2.poll()) != null) {
            Document document = DocumentParser.parse(file.getAbsolutePath());
            List<Word> keywords = new ArrayList<>(document.getVoc().values());
            for(Word word: keywords) {
                Word globalWord = globalVoc.get(word.getWord());
                word.setDf(globalWord.getDf(), numDocuments);
            }
            Collections.sort(keywords);
            if (keywords.size() > 10) {
                keywords = keywords.subList(0, 10);
            }
            for (Word word: keywords) {
                addKeyword(globalKeywords, word.getWord());
            }
        }
        System.out.println(name + ": " + parsedDocuments + " parsed.");
        
        
        // 第三阶段
        if (main) {
            // 使用 Phaser 类的 arriveAndAwaitAdvance() 方法等待所有任务的第二阶段结束。
            phaser.arriveAndAwaitAdvance();
            Iterator<Entry<String, Integer>> iterator = globalKeywords.entrySet().iterator();
            Keyword[] orderedGlobalKeywords = new Keyword[globalKeywords.size()];
            int index = 0;
            while (iterator.hasNext()) {
                Entry<String, Integer> entry = iterator.next();
                Keyword keyword = new Keyword();
                keyword.setWord(entry.getKey());
                keyword.setDf(entry.getValue());
                orderedGlobalKeywords[index] = keyword;
                index ++;
            }
            System.out.println("Keyword Size:" + orderedGlobalKeywords.length);
            Arrays.parallelSort(orderedGlobalKeywords);
            int counter = 0;
            for (int i = 0; i < orderedGlobalKeywords.length; i++) {
                Keyword keyword = orderedGlobalKeywords[i];
                System.out.println(keyword.getWord() + " : " + keyword.getDf());
                counter ++;
                if (counter == 100) {
                    break;
                }
            }
        }
        // 当所有的任务完成工作后，都将从分段器中注销。
        phaser.arriveAndDeregister();
        System.out.println("Thread " + name + " has finished.");
    }


    /**
     *  用于更新 globalKeywords 中某个关键字的信息。如果该单词存在，则该类更新其 DF 值；如果不存在，则将其插入。
     *  @param globalKeywords
     *  @param word
     */
    private static void addKeyword(ConcurrentHashMap<String, Integer> globalKeywords, String word) {
        globalKeywords.merge(word, 1, Integer::sum);
    }
}
