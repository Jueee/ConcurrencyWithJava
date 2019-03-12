package com.app.jueee.concurrency.chapter06.common;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;

/**
 *  读取一个文本文件的内容并且将其转换成一个 Document 对象。
 *	
 *	@author hzweiyongqiang
 */
public class DocumentParser {

    public static Document parse(String path) {
        Document ret = new Document();
        Path file = Paths.get(path);
        ret.setFileName(file.toString());
        
        try (BufferedReader reader = Files.newBufferedReader(file)){
            for(String line: Files.readAllLines(file)) {
                parseLine(line, ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    /**
     *  
     *	@param line 待解析的行
     *	@param ret 用于存放单词的 Document 对象
     */
    public static void parseLine(String line, Document ret) {
        // 使用 Normalizer 类删除每一行的重音符号，并将其转换成小写形式。
        line = Normalizer.normalize(line, Normalizer.Form.NFKD);
        line = line.replaceAll("[^\\p{ASCII}]", "");
        line = line.toLowerCase();
        
        // 分词程序
        for(String w: line.split("\\W+")) {
            ret.addWord(w);
        }
    }
}
