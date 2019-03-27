package com.app.jueee.concurrency.chapter07.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 *  用于加载文档集中构成词汇表的单词列表。
 *	
 *	@author hzweiyongqiang
 */
public class VocabularyLoader {

    public static Map<String, Integer> load(Path path) throws IOException{
        int index = 0;
        Map<String, Integer> vocIndex = new HashMap<>();
        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line = null;
            while ((line = reader.readLine()) != null) {
                vocIndex.put(line, index);
                index ++;
            }
        }
        return vocIndex;
    }
    
}
