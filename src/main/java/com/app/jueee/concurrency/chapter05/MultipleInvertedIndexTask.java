package com.app.jueee.concurrency.chapter05;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Future;

import com.app.jueee.concurrency.chapter05.common.Document;

public class MultipleInvertedIndexTask implements Runnable{

    // 访问由 IndexingTask 对象返回的对象。
    private CompletionService<List<Document>> completionService;
    // 存储倒排索引
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex;

    public MultipleInvertedIndexTask(CompletionService<List<Document>> completionService,
        ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex) {
        this.completionService = completionService;
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void run() {
        try {
            // 在线程中断之前该循环将一直运行
            while (!Thread.interrupted()) {
                try {
                    // take() 方法获取与某一任务相关联的 Future 对象。
                    List<Document> documents = completionService.take().get();
                    for(Document document: documents) {
                        updateInvertedIndex(document.getVoc(), invertedIndex, document.getFileName());
                    }
                } catch (Exception e) {
                    break;
                }
            }

            // 当该线程中断之后，它会再次使用 poll() 方法处理所有待处理的 Future 对象。
            while (true) {
                Future<List<Document>> future = completionService.poll();
                if (future == null) {
                    break;
                }
                List<Document> documents = future.get();
                for(Document document: documents) {
                    updateInvertedIndex(document.getVoc(), invertedIndex, document.getFileName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将从各个文档获取的词汇表添加到倒排索引中
    private void updateInvertedIndex(Map<String, Integer> voc,
        ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> invertedIndex, String fileName) {
        for (String word : voc.keySet()) {
            if (word.length() >= 3) {
                invertedIndex.computeIfAbsent(word, k -> new ConcurrentLinkedDeque<>()).add(fileName);
            }
        }
    }
}
