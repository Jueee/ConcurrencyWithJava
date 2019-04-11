package com.app.jueee.concurrency.chapter07.example1;

import java.util.concurrent.RecursiveAction;

import com.app.jueee.concurrency.chapter07.common1.DocumentCluster;

public class UpdateTask extends RecursiveAction{

    // 有关簇数据的 ConcurrentDocumentCluster 对象数组
    private DocumentCluster[] clusters;

    // 决定了任务要处理的文档数
    private int start, end;

    // 存放的是一个任务所能处理的最大文档数
    private int maxSize;

    public UpdateTask(DocumentCluster[] clusters, int start, int end, int maxSize) {
        this.clusters = clusters;
        this.start = start;
        this.end = end;
        this.maxSize = maxSize;
    }
    
    @Override
    protected void compute() {
        // 如果该数值小于或者等于 maxSize 属性的值，则该方法将处理这些簇并且更新其质心。
        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                DocumentCluster cluster = clusters[i];
                cluster.calculateCentroid();
            }
        } else {
            int mid = (start + end) / 2;
            UpdateTask task1 = new UpdateTask(clusters, start, mid, maxSize);
            UpdateTask task2 = new UpdateTask(clusters, mid, end, maxSize);
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    
}
