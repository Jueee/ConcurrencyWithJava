package com.app.jueee.concurrency.chapter07.example3;

import java.util.concurrent.CountedCompleter;

public class MergeSortTask extends CountedCompleter<Void> {
    
    private static final long serialVersionUID = 7732025803421800307L;

    // 存放待排序数据的数组
    private Comparable[] data;
    // 任务必须进行排序操作的这部分数组的起始位置和终止位置
    private int start, end;
    
    private int middle;

    public MergeSortTask(Comparable[] data, int start, int end, MergeSortTask parent) {
        super(parent);
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    public void compute() {
        // 如果起始索引和终止索引之间的差距大于或等于 1024 ，那么使用 compute() 方法
        // 将任务分割成两个子任务来分别处理原集合的两个子集。  
        // 两个任务采用 fork() 方法以异步方式将任务发送给  ForkJoinPool 。
        if (end - start >= 1024) {
            middle = (end + start) >>> 1;
            MergeSortTask task1 = new MergeSortTask(data, start, middle, this);
            MergeSortTask task2 = new MergeSortTask(data, middle, end, this);
            addToPendingCount(1);
            task1.fork();
            task2.fork();
        // 否则，执行 SerialMergeSorg.mergeSort() 对数组（具有小于或等于 1024 个元素）进行排序 
        // 然后调用 tryComplete() 方法。子任务执行完毕之后，该方法将从内部调用 onCompletion() 方法。
        } else {
            new J3MergeSortSerialMain().mergeSort(data, start, end);
            tryComplete();
        }
    }
    
    @Override
    public void onCompletion(CountedCompleter<?> caller) {
        if (middle == 0) {
            return;
        }
        int length = end - start + 1;
        Comparable[] tmp = new Comparable[length];
        int i, j, index;
        i = start;
        j = middle;
        index = 0;
        while ((i < middle) && (j < end)) {
            if (data[i].compareTo(data[j]) <= 0) {
                tmp[index] = data[i];
                i++;
            } else {
                tmp[index] = data[j];
                j++;
            }
            index ++;
        }
        while (i < middle) {
            tmp[index] = data[i];
            i++;
            index++;
        }
        while (j < end) {
            tmp[index] = data[j];
            j++;
            index++;
        }
        for (int x = 0; x < (end - start); x++) {
            data[x + start] = tmp[x];
        }
        super.onCompletion(caller);
    }
}
