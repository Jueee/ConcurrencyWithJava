package com.app.jueee.concurrency.chapter05.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *  用于将一个以文件存储的文档转换成一个文档对象。
 *	
 *	@author hzweiyongqiang
 */
public class DocumentParse {

    /**
     * 
     *	@param route 文件路径
     *	@return 带有该文档词汇表的 HashMap
     */
    public Map<String, Integer> parse(String route) {
        Map<String, Integer> ret = new HashMap<>();
        Path file = Paths.get(route);
        try {
            // 使用 Files 类的 readAllLines() 方法逐行读取文件
            List<String> lines = Files.readAllLines(file);
            for(String line: lines) {
                // 将每一行转换成一个单词列表，并且将其添加到词汇表中
                parseLine(line, ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    private static final Pattern PATTERN = Pattern.compile("\\P{IsAlphabetic}+");
    
    /**
     *  处理当前行并抽取其中的单词
     *	@param line
     *	@param ret
     */
    private void parseLine(String line, Map<String, Integer> ret) {
        for(String word: PATTERN.split(line)) {
            if (!word.isEmpty()) {
                // 使用 Normalizer 类将单词转换成小写形式，并且删除元音的重音符号
                ret.merge(Normalizer.normalize(word, Normalizer.Form.NFKD), 1, (a,b) -> a + b);
            }
        }
    }
}
