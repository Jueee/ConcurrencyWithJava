package com.app.jueee.concurrency.chapter05;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

import com.app.jueee.concurrency.chapter05.common.Document;
import com.app.jueee.concurrency.chapter05.common.DocumentParse;

/**
 *  解析一个文档来获取其词汇表
 *	
 *	@author hzweiyongqiang
 */
public class IndexingTask implements Callable<Document>{

    private File file;
    
    public IndexingTask(File file) {
        this.file = file;
    }
    
    @Override
    public Document call() throws Exception {
        DocumentParse parse = new DocumentParse();
        Map<String, Integer> voc = parse.parse(file.getAbsolutePath());
        
        Document document = new Document();
        document.setFileName(file.getName());
        document.setVoc(voc);
        return document;
    }

}
