package com.app.jueee.concurrency.chapter05;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jueee.concurrency.chapter05.common.DocumentParse;

public class J3SerialIndexing {
    
    public static int test(File source) {
        return test(source.listFiles());
    }
    
    public static int test(File[] files) {
        // 键为单词，值是一个字符串对象列表，这些字符串表示的是含有该单词的文件的名称
        Map<String, List<String>> invertedIndex = new HashMap<>();

        for (File file : files) {
            DocumentParse parse = new DocumentParse();
            if (file.getName().endsWith(".txt")) {
                Map<String, Integer> voc = parse.parse(file.getAbsolutePath());
                updateInvertedIndex(voc, invertedIndex, file.getName());
            }
        }
        
        return invertedIndex.size();
    }

    public static void main(String[] args) {
        File source = new File("data//chapter05//data");
        // 存储所有的文档
        File[] files = source.listFiles();
        System.out.println("invertedIndex: " + test(files));
    }

    // 将从各个文档获取的词汇表添加到倒排索引中
    private static void updateInvertedIndex(Map<String, Integer> voc, Map<String, List<String>> invertedIndex, String fileName) {
        for (String word : voc.keySet()) {
            if (word.length() >= 3) {
                invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(fileName);
            }
        }
    }
}
