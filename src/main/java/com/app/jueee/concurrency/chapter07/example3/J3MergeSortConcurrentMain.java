package com.app.jueee.concurrency.chapter07.example3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

import com.app.jueee.concurrency.chapter07.common3.AmazonMetaData;
import com.app.jueee.concurrency.chapter07.common3.AmazonMetaDataLoader;

public class J3MergeSortConcurrentMain {

    public static void mergeSort(Comparable[] data) {
        mergeSort(data, 0, data.length);
    }

    public static void mergeSort(Comparable[] data, int start, int end) {
        MergeSortTask task = new MergeSortTask(data, start, end, null);
        ForkJoinPool.commonPool().invoke(task);
    }
    
    public static void main(String[] args) {
        for (int j=0; j< 10; j++) {
            Path path = Paths.get("data//chapter07","amazon-meta.txt");
            AmazonMetaData[] data = AmazonMetaDataLoader.load(path);
            AmazonMetaData data2[] = data.clone();
            
            Date start, end;
            // 使用 Arrays 类的 sort() 方法对第一个数组进行排序
            start = new Date();
            Arrays.parallelSort(data);
            end = new Date();
            System.out.println("Execution Time Java Arrays.sort(): " + (end.getTime() - start.getTime()));
            // 使用归并排序算法实现对第二个数组的排序
            start = new Date();
            mergeSort(data2, 0, data2.length);
            end = new Date();
            System.out.println("Execution Time Java ConcurrentMergeSort: " + (end.getTime() - start.getTime()));
            
            // 检查发现排序后的数组相似
            for (int i = 0; i < data.length; i++) {
                if (data[i].compareTo(data2[i]) != 0) {
                    System.err.println("There's a difference is position " + i);
                    System.exit(-1);
                }
            }
            System.out.println("Both arrays are equal");
        }
    }
}
