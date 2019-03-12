package com.app.jueee.concurrency.chapter06;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jueee.concurrency.chapter06.common.Document;
import com.app.jueee.concurrency.chapter06.common.DocumentParser;
import com.app.jueee.concurrency.chapter06.common.Keyword;
import com.app.jueee.concurrency.chapter06.common.Word;

public class J2SerialKeywordExtraction {

    public static void main(String[] args) {
        Date start, end;
        File source = new File("data//chapter05//data");
        File[] files = source.listFiles();
        // 存放文档集合全局词汇表
        HashMap<String, Word> globalVoc = new HashMap<>();
        // 存放关键字
        HashMap<String, Integer> globalKeywords = new HashMap<>();
        // 度量有关执行情况统计数据
        int totalCalls = 0;
        int numDocuments = 0;
        
        start = new Date();
        
        if (files == null) {
            System.err.println("Unable to read the 'data' folder");
            return;
        }
        System.out.println("Files size : " + files.length);
        
        for(File file: files) {
            if (file.getName().endsWith(".txt")) {
                // 解析所有文档
                Document document = DocumentParser.parse(file.getAbsolutePath());
                for(Word word: document.getVoc().values()) {
                    // 使用 HashMap 类的 merge() 方法将文档词汇表添加到全局词汇表。
                    // 如果单词不存在，则将它插入 HashMap 。
                    // 如果该单词已存在，则将两个单词对象合并到一起，并且对 Tf 属性和 Df 属性求和。
                   globalVoc.merge(word.getWord(), word, Word::merge);
                }
                numDocuments ++;
            }
        }
        System.out.println("Corpus : " + numDocuments + " documents.");
        
        // 此阶段之后， globalVoc 包含了文档集合的所有单词，以及单词的全局 TF （单词在文档集合中出现的总次数）和 DF 值。
        
        // 算法的第二阶段
        for(File file: files) {
            if (file.getName().endsWith(".txt")) {
                // 解析所有文档
                Document document = DocumentParser.parse(file.getAbsolutePath());
                List<Word> keywords = new ArrayList<>(document.getVoc().values());
                for(Word word: keywords) {
                    Word globalWord = globalVoc.get(word.getWord());
                    word.setDf(globalWord.getDf(), numDocuments);
                }
                
                Collections.sort(keywords);
                
                for(Word word: keywords) {
                    addKeyword(globalKeywords, word.getWord());
                    totalCalls ++;
                } 
            }
        }
        
        // 算法的第三阶段
        List<Keyword> orderedGlobalKeywords = new ArrayList<>();
        for(Map.Entry<String, Integer> entry:globalKeywords.entrySet()) {
            Keyword keyword = new Keyword();
            keyword.setWord(entry.getKey());
            keyword.setDf(entry.getValue());
            orderedGlobalKeywords.add(keyword);
        }
        
        Collections.sort(orderedGlobalKeywords);
        
        if (orderedGlobalKeywords.size() > 100) {
            orderedGlobalKeywords = orderedGlobalKeywords.subList(0, 100);
        }
        for(Keyword keyword : orderedGlobalKeywords) {
            System.out.println(keyword.getWord() + ": "+ keyword.getDf());
        }
        end = new Date();
        
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
        System.out.println("Vocabulary Size: " + globalVoc.size());
        System.out.println("Keyword Size: " + globalKeywords.size());
        System.out.println("Number of Documents: " + numDocuments);
        System.out.println("Total calls: " + totalCalls);
    }

    /**
     *  用于更新 globalKeywords 中某个关键字的信息。如果该单词存在，则该类更新其 DF 值；如果不存在，则将其插入。
     *	@param globalKeywords
     *	@param word
     */
    private static void addKeyword(HashMap<String, Integer> globalKeywords, String word) {
        globalKeywords.merge(word, 1, Integer::sum);
    }
}
