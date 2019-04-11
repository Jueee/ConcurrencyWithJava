package com.app.jueee.concurrency.chapter07.example1;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import com.app.jueee.concurrency.chapter07.common1.DistanceMeasurer;
import com.app.jueee.concurrency.chapter07.common1.Document;
import com.app.jueee.concurrency.chapter07.common1.DocumentCluster;

public class AssignmentTask extends RecursiveAction{

    // 有关簇数据的 ConcurrentDocumentCluster 对象数组
    private DocumentCluster[] clusters;

    // 有关文档数据的 ConcurrentDocument 对象数组
    private Document[] documents;

    // 决定了任务要处理的文档数
    private int start, end;

    // 存放的是从上一轮执行到当前执行的过程中改变了为其指派的簇的文档数
    // 该原子变量可以同时由多个线程更新且无须同步机制
    private AtomicInteger numChanges;

    // 存放的是一个任务所能处理的最大文档数
    private int maxSize;

    public AssignmentTask(DocumentCluster[] clusters,
            Document[] documents, int start, int end,
            AtomicInteger numChanges, int maxSize) {
        this.clusters = clusters;
        this.documents = documents;
        this.start = start;
        this.end = end;
        this.numChanges = numChanges;
        this.maxSize = maxSize;
    }

    
    @Override
    protected void compute() {
        // 检查任务必须处理的文档数。如果该值小于或等于 maxSize 属性的值，那么处理这些文档
        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                Document document = documents[i];
                double distance = Double.MAX_VALUE;
                DocumentCluster selectedCluster = null;
                for(DocumentCluster cluster: clusters) {
                    double curDistance = DistanceMeasurer.euclideanDistance(document.getData(), cluster.getCentroid());
                    if (curDistance < distance) {
                        distance = curDistance;
                        selectedCluster = cluster;
                    }
                }
                selectedCluster.addDocument(document);
                boolean result = document.setCluster(selectedCluster);
                if (result) {
                    numChanges.incrementAndGet();
                }
            }
            
        // 如果该任务要处理的文档数量太多，那么将该集合分割成两个部分，并且创建两个新的任务来分别处理这两个部分
        } else {
            int mid = (start + end) / 2;
            AssignmentTask task1 = new AssignmentTask(clusters, documents, start, mid, numChanges, maxSize);
            AssignmentTask task2 = new AssignmentTask(clusters, documents, mid, end, numChanges, maxSize);
            // 为了在 Fork/Join池中执行上述任务，使用了 invokeAll() 方法。该方法在任务结束其执行后返回。
            invokeAll(task1, task2);
        }
    }

    public DocumentCluster[] getClusters() {
        return clusters;
    }

    public void setClusters(DocumentCluster[] clusters) {
        this.clusters = clusters;
    }

    public Document[] getDocuments() {
        return documents;
    }

    public void setDocuments(Document[] documents) {
        this.documents = documents;
    }
    
}
