package com.app.jueee.concurrency.chapter05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.app.jueee.concurrency.chapter05.common.Document;
import com.app.jueee.concurrency.chapter05.common.DocumentParse;

public class MultipleIndexingTask implements Callable<List<Document>>{


    private List<File> files;
    
    public MultipleIndexingTask(List<File> files) {
        this.files = files;
    }
    
    @Override
    public List<Document> call() throws Exception {
        List<Document> documents = new ArrayList<>();
        DocumentParse parse = new DocumentParse();
        for(File file : files) {
            Map<String, Integer> voc = parse.parse(file.getAbsolutePath());
            
            Document document = new Document();
            document.setFileName(file.getName());
            document.setVoc(voc);
            
            documents.add(document);
        }
        
        return documents;
    }
}
