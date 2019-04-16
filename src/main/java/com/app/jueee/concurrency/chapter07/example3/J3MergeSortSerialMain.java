package com.app.jueee.concurrency.chapter07.example3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

import com.app.jueee.concurrency.chapter07.common3.AmazonMetaData;
import com.app.jueee.concurrency.chapter07.common3.AmazonMetaDataLoader;

public class J3MergeSortSerialMain {
    
    /**
     *  如果该方法仅需要处理一个元素，则其立即返回。
     *  
     *  否则，它将两次递归调用 mergeSort() 方法。
     *  第一次调用处理前一半元素，第二次调用处理后一半元素。
     *  
     *  最后，调用 merge() 方法合并两部分元素，并且获得一个经过排序的元素列表。
     *  
     *	@param data 含有所有待排序数据的数组
     *	@param start 该方法要处理的第一个元素（包含）
     *	@param end 该方法要处理的最后一个元素（不包含）
     */
    public static void mergeSort(Comparable[] data, int start, int end) {
        if (end - start < 2) {
            return;
        }
        // 使用 (end+start)>>>1 操作符获取位于数组中间位置的元素，进而分割数组。
        // 采用 (end+start)/2 的方法有可能 int 溢出，结果得到一个负数值的数组。
        int middle = (end + start) >>> 1;
        mergeSort(data, start, middle);
        mergeSort(data, middle, end);
        merge(data, start, middle, end);
    }
    
    
    /**
     *  将两个元素列表合并以得到一个排序的列表
     *  
     *  创建了一个临时数组来对元素进行排序
     *  然后，处理列表的两部分时，会在数组中对元素进行排序，并且在原始数组相同的位置上存放已排序的列表。
     *	@param data
     *	@param start
     *	@param middle
     *	@param end
     */
    public static void merge(Comparable[] data, int start,int middle, int end) {
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
    }
    
    public static void main(String[] args) {
        for (int j=0; j< 10; j++) {
            Path path = Paths.get("data//chapter07","amazon-meta.txt");
            AmazonMetaData[] data = AmazonMetaDataLoader.load(path);
            AmazonMetaData data2[] = data.clone();
            
            Date start, end;
            // 使用 Arrays 类的 sort() 方法对第一个数组进行排序
            start = new Date();
            Arrays.sort(data);
            end = new Date();
            System.out.println("Execution Time Java Arrays.sort(): " + (end.getTime() - start.getTime()));
            // 使用归并排序算法实现对第二个数组的排序
            start = new Date();
            mergeSort(data2, 0, data2.length);
            end = new Date();
            System.out.println("Execution Time Java SerialMergeSort: " + (end.getTime() - start.getTime()));
            
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
