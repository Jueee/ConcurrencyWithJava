package com.app.jueee.concurrency.chapter07.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DocumentLoader {

    /**
     *  加载有关文档的信息
     *	@param path
     *	@param vocIndex 存放词汇表
     *	@return
     *	@throws IOException
     */
    public static Document[] load(Path path, Map<String, Integer> vocIndex) throws IOException {
        List<Document> list = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line = null;
            while ((line = reader.readLine()) != null) {
                Document document = processItem(line, vocIndex);
                list.add(document);
            }
        }
        Document[] ret = new Document[list.size()];
        return list.toArray(ret);
    }
    
    /**
     *  将文本文件的某一行转换成一个 Document 对象
     *	@param line
     *	@param vocIndex
     *	@return
     */
    private static Document processItem(String line, Map<String, Integer> vocIndex) {
        String[] tokens = line.split(",");
        int size = tokens.length - 1;
        Document document = new Document(tokens[0], size);
        Word[] data = document.getData();
        
        for (int i = 1; i < tokens.length; i++) {
            String[] wordInfo = tokens[i].split(":");
            Word word = new Word();
            word.setIndex(vocIndex.get(wordInfo[0]));
            word.setTfidf(Double.parseDouble(wordInfo[1]));
            data[i - 1] = word;
        }
        Arrays.sort(data);
        return document;
    }
}
