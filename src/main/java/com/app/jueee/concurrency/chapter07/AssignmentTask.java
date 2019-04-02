package com.app.jueee.concurrency.chapter07;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import com.app.jueee.concurrency.chapter07.common.Document;
import com.app.jueee.concurrency.chapter07.common.DocumentCluster;

public class AssignmentTask extends RecursiveAction{

    private DocumentCluster[] clusters;

    private Document[] documents;

    private int start, end;

    private AtomicInteger numChanges;

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
        // TODO Auto-generated method stub
        
    }

}
